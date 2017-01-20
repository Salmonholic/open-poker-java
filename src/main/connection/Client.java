package main.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.core.Action;

public class Client implements Runnable {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private final SimpleBooleanProperty running = new SimpleBooleanProperty(false);

	private String username;
	
	private final SimpleObjectProperty<Update> update = new SimpleObjectProperty<>();
	private final ObservableList<Table> tables = FXCollections.observableArrayList();
	private final SimpleObjectProperty<Packet> accept = new SimpleObjectProperty<>();
	private final SimpleObjectProperty<Packet> decline = new SimpleObjectProperty<>();

	public Client(String host, int port) throws Exception {
		socket = new Socket(host, port);
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		
		thread = new Thread(this);
		thread.start();
		running.set(true);
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
	@SuppressWarnings("unchecked")
	private void parsePacket(Packet packet) {
		HashMap<String, Object> data = packet.getData();
		switch (packet.getType()) {
		case "accept":
			accept.set(packet);
			break;
		case "decline":
			decline.set(packet);
			break;
		case "update":
			update.set((Update) data.get("update"));
			break;
		case "tables": 
			tables.clear();
			ArrayList<Table> tablesArray = (ArrayList<Table>) data.get("tables");
			for (Table table : tablesArray) {
				tables.addAll(table);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Get the most recent update
	 */
	public Update getUpdate() {
		return update.get();
	}

	/**
	 * Get update property
	 */
	public SimpleObjectProperty<Update> getUpdateProperty() {
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
		while (running.get()) {
			try {
				read();
			} catch (ClassNotFoundException e) {
				System.out.println("Recieved corrupt server paket.");
				try {
					in.reset();
				} catch (IOException e1) {
					System.out.println("Fatal: Could not reset input stream.");
					e1.printStackTrace();
					running.set(false);
				}
			} catch (IOException e) {
				if (running.get()) {
					System.out.println("Fatal: Network error!\n");
					e.printStackTrace();
					running.set(false);
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
		running.set(false);
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running.get();
	}
	
	public SimpleBooleanProperty getRunningProperty() {
		return running;
	}

	public SimpleObjectProperty<Packet> getAcceptProperty() {
		return accept;
	}

	public SimpleObjectProperty<Packet> getDeclineProperty() {
		return decline;
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

	public void sendGetTablesPacket() {
		sendPacket(new Packet("getTables", null));
	}

	public ObservableList<Table> getTables() {
		return tables;
	}
	
	
}
