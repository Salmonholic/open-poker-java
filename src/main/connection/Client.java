package main.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.core.Action;

public class Client implements Runnable{

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;

	private String username;
	private int room;
	
	private Update update;

	public Client(String host, int port, String username, int room)
			throws Exception {
		this.username = username;
		this.room = room;
		
		socket = new Socket(host, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		
		HashMap<String, Object> data = new HashMap<>();
		data.put("username", username);
		data.put("room", room);
		data.put("type", "player");
		out.writeObject(new Packet("connect", data));
		out.flush();
		
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Read all arrived packets
	 * 
	 * @throws Exception
	 */
	private void read() throws Exception {
		Packet packet = (Packet) in.readObject();
		System.out.println(username + " got packet " + packet.getType());
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
		case "update":
			HashMap<String, Object> data = packet.getData();
			update = (Update) data.get("update");
			break;
		default:
			break;
		}
	}

	/**
	 * Get the most recent update
	 * 
	 * @return Update
	 * @throws Exception
	 */
	public Update getUpdate() throws Exception {
		return update;
	}

	/**
	 * Send an action to the server amount defaults to 0
	 * 
	 * @param action
	 *            Action
	 * @param amount
	 *            Amount of action
	 * @throws IOException
	 */
	public void sendAction(Action action, int amount) throws IOException {
		HashMap<String, Object> data = new HashMap<>();
		data.put("action", action);
		data.put("amount", amount);
		out.writeObject(new Packet("action", data));
		out.flush();
	}

	/**
	 * Send an action to the server amount defaults to 0
	 * 
	 * @param action
	 *            Action
	 * @throws IOException
	 */
	public void sendAction(Action action) throws IOException {
		sendAction(action, 0);
	}

	@Override
	public void run() {
		while (true) {
			try {
				read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
