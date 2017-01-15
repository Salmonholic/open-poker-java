package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable {
	private ServerSocket serverSocket;
	private HashMap<Integer, TableController> tables = new HashMap<>();
	private AuthenticationController authenticationController;
	private Thread thread;
	private boolean running = true;

	public Server(int port) throws Exception {
		authenticationController = new AuthenticationController();
		serverSocket = new ServerSocket(port);
		System.out.println("Server started");

		System.out.println("Creating table with id 0 and 3 players");
		createTableController(0, 3, 1000);

		thread = new Thread(this);
		thread.start();
	}

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
			} catch (IllegalArgumentException e) {
				System.out
						.println("A client tried to join a not existing room.");
			}
		}
		// Close server
		if (!serverSocket.isClosed()) {
			// TODO send info to clients
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (TableController t : tables.values())
			t.close();
	}

	/**
	 * Create a new table
	 * 
	 * @param id
	 *            Id for new TableController
	 * @param money
	 *            Start-money for Players
	 */
	public void createTableController(int id, int players, int money) {
		// TODO check if id is used
		tables.put(id, new TableController(players, money, id));

	}

	public TableController getTableController(int id)
			throws IllegalArgumentException {
		if (!tables.containsKey(id)) {
			throw new IllegalArgumentException();
		}
		return tables.get(id);
	}

	public HashMap<Integer, TableController> getTables() {
		return tables;
	}

	public void close() {
		// TODO send info to clients
		running = false;
		// Safe user info
		getAuthenticationController().save();
		try {
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public int getPort() {
		return serverSocket.getLocalPort();
	}

	public AuthenticationController getAuthenticationController() {
		return authenticationController;
	}
	
}
