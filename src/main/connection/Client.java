package main.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.core.Action;
import main.core.Table;

public class Client {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Table table;

	public Client(String host, int port, String username, int room)
			throws Exception {
		socket = new Socket(host, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		
		HashMap<String, Object> data = new HashMap<>();
		data.put("username", username);
		data.put("table", room);
		data.put("type", "player");
		out.writeObject(new Packet("connect", data));
	}

	/**
	 * Read all arrived packets
	 * 
	 * @throws Exception
	 */
	public void read() throws Exception {
		while (in.available() > 0) {
			Packet packet = (Packet) in.readObject();
			parsePacket(packet);
		}
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
			if (data.containsKey("table") && data.get("table") instanceof Table) {
				table = (Table) data.get(table);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Get the most recent table
	 * 
	 * @return Table
	 * @throws Exception
	 */
	public Table getTable() throws Exception {
		read();
		return table;
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

}
