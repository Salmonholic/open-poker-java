package main.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.connection.Packet;
import main.connection.Update;
import main.core.Table;

public class PlayerController implements Runnable {

	private Socket socket;
	private TableController tableController;
	int id;
	private int tableId;
	private String username;
	private String type;

	private ObjectInputStream in;
	private ObjectOutputStream out;

	public PlayerController(Socket socket, Server server) throws Exception {
		this.socket = socket;

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());

		Packet packet = (Packet) in.readObject();
		HashMap<String, Object> data = packet.getData();
		tableId = (int) data.get("room");
		username = (String) data.get("username");
		tableController = server.getTableController(tableId);
		type = (String) data.get("type");

		id = tableController.addPlayerController(this);

		System.out.println("New " + type + " connected with id " + id + " on table "
				+ tableId + " with name " + username);
		
		packet = new Packet("accept", new HashMap<String, Object>());
		try {
			out.writeObject(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {

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
			e.printStackTrace();
		}
	}

}
