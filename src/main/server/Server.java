package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private HashMap<Integer, TableController> tables = new HashMap<>();
	private Thread thread;
	private boolean running = true;

	public Server(int port, int players) throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Server started");
		
		tables.put(0, new TableController(players, 1000, 0));
		
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Start main update loop
	 */
	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				new PlayerController(socket, this);
			} catch (IOException e) {
				if (running)
					System.out.println("Network error!");
			} catch (ClassNotFoundException e) {
				System.out.println("Recieved corrupt client paket.");
			} catch (IllegalStateException e) {
				System.out.println("A client tried to join a started table.");
				//TODO add room?
			} catch (IllegalArgumentException e) {
				System.out.println("A client tried to join a not existing room.");
				//TODO add room?
			}
		}
		// Close server
		if (!serverSocket.isClosed()) {
			//TODO send info to clients
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (TableController t : tables.values())
			t.close();
	}

	public TableController getTableController(int id) throws IllegalArgumentException {
		if (!tables.containsKey(id)) {
			throw new IllegalArgumentException();
		}
		return tables.get(id);
	}
	
	public HashMap<Integer, TableController> getTables() {
		return tables;
	}
	
	public void close() {
		//TODO send info to clients
		running = false;
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}
}
