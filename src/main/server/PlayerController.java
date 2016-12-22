package main.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.connection.Packet;
import main.connection.Update;
import main.core.Action;
import main.core.Table;

public class PlayerController implements Runnable {

	private Socket socket;
	private TableController tableController;
	private Thread thread;
	private boolean running = true;
	
	private int id;
	private int tableId;
	private String username;
	private String type;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	public PlayerController(Socket socket, Server server) throws IOException,
			ClassNotFoundException, IllegalStateException {
		this.socket = socket;

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		Packet packet = (Packet) in.readObject();
		HashMap<String, Object> data = packet.getData();
		tableId = (int) data.get("room");
		username = (String) data.get("username");
		tableController = server.getTableController(tableId);//TODO catch
		type = (String) data.get("type");

		tableController.addPlayerController(this);

		System.out.println("New " + type + " connected with id " + id + " on table "
				+ tableId + " with name " + username);
		
		packet = new Packet("accept", null);
		out.writeObject(packet);
		out.flush();
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (running) {
			try {
				read();
			} catch (ClassNotFoundException e) {
				System.out.println("Recieved corrupt client paket.");
				try {
					in.reset();
				} catch (IOException e1) {
					System.out.println("Fatal: Could not reset input stream.");
					e1.printStackTrace();
					running = false;
				}
			} catch (IOException e) {
				System.out.println("Fatal: Network error!\n");
				e.printStackTrace();
				running = false;
				//TODO kick player?
			}
		}
		//TODO kick player
		//TODO send info to player
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Read all arrived packets
	 */
	private void read() throws ClassNotFoundException, IOException {
		Packet packet = (Packet) in.readObject();
		parsePacket(packet);
	}

	/**
	 * Parse a packet
	 * 
	 * @param packet
	 *            Packet to parse
	 */
	private void parsePacket(Packet packet) {
		switch (packet.getType()) {
		case "action":
			HashMap<String, Object> data = packet.getData();
			Action action = (Action) data.get("action");
			int amount = (Integer) data.get("amount");
			tableController.action(id, action, amount);
			break;
		default:
			break;
		}
	}

	public void resend(Table table) {
		System.out.println("Resend data to player " + id);
		HashMap<String, Object> data = new HashMap<>();
		data.put("update", new Update(table, id));
		Packet packet = new Packet("update", data);
		try {
			out.writeObject(packet);
			out.flush();
		} catch (IOException e) {
			System.out.println("Failed to send update to client " + id);
			try {
				out.reset();
				out.writeObject(packet);
				out.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
				//TODO kick
			}
		}
	}

	public void setId(int id) {
		this.id = id;
	}
}
