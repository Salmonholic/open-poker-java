package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
	private ServerSocket serverSocket;
	private HashMap<Integer, TableController> tables = new HashMap<>();

	public Server(int port, int players) throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Server started");
		
		tables.put(0, new TableController(players, 1000));
		
		update();
	}

	/**
	 * Start main update loop
	 * 
	 * @throws Exception
	 */
	public void update() throws Exception {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new PlayerController(socket, this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TableController getTableController(int id) {
		if (!tables.containsKey(id)) {
			throw new IllegalArgumentException();
		}
		return tables.get(id);
	}
	
	public HashMap<Integer, TableController> getTables() {
		return tables;
	}
	
	public void close() throws IOException {
		serverSocket.close();
	}

}
