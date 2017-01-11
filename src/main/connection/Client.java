package main.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.core.Action;

public class Client implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private boolean running = true;

	private String username;
	
	private Update update;

	public Client(String host, int port) throws Exception {
		socket = new Socket(host, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void login(String username, String password) {
		this.username = username;
		HashMap<String, Object> data = new HashMap<>();
		data.put("username", username);
		data.put("password", password);
		sendPacket(new Packet("login", data));
	}
	
	public void signup(String username, String password) {
		this.username = username;
		HashMap<String, Object> data = new HashMap<>();
		data.put("username", username);
		data.put("password", password);
		sendPacket(new Packet("signup", data));
	}

	/**
	 * Read all arrived packets
	 */
	private void read() throws ClassNotFoundException, IOException {
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
	 */
	public Update getUpdate() {
		return update;
	}

	/**
	 * Send an action to the server
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
		sendPacket(new Packet("action", data));
	}

	/**
	 * Send an action to the server with amount 0
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
		while (running) {
			try {
				read();
			} catch (ClassNotFoundException e) {
				System.out.println("Recieved corrupt server paket.");
				try {
					in.reset();
				} catch (IOException e1) {
					System.out.println("Fatal: Could not reset input stream.");
					e1.printStackTrace();
					running = false;
				}
			} catch (IOException e) {
				if (running) {
					System.out.println("Fatal: Network error!\n");
					e.printStackTrace();
					running = false;
				}
			}
		}
		// Close client
		if (!socket.isClosed()) {
			//TODO send info to server
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		//TODO send info to server
		running = false;
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}
	
	public void sendPacket(Packet packet) {
		try {
			out.writeObject(packet);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void joinTable(int id) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("room", id);
		sendPacket(new Packet("join", data));
	}
}
