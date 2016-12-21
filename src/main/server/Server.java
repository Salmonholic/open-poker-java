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
	public void update() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new PlayerController(socket, this);
			} catch (IOException e) {
				System.out.println("Network error!");
				e.printStackTrace();
				//TODO server shut down?
			} catch (ClassNotFoundException e) {
				System.out.println("Recieved corrupt client paket.");
			} catch (IllegalStateException e) {
				System.out.println("A client tried to join a started table."); //TODO add room
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
