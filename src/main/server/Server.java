package main.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
	private ServerSocket serverSocket;
	private HashMap<Integer, TableController> tables = new HashMap<>();

	public Server(int port) throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Server started");
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
		return tables.get(id);
	}

}
