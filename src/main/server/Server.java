package main.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import main.connection.Packet;

public class Server {
	ServerSocket serverSocket;
	HashMap<Integer, TableController> tables = new HashMap<>();
	
	public Server(int port) throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Server started");
		update();
	}
	
	/**
	 * Start main update loop
	 * @throws Exception 
	 */
	public void update() throws Exception {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Packet packet = (Packet) in.readObject();
				HashMap<String, Object> data = packet.getData();
				int username = (int) data.get("username");
				int table = (int) data.get("table");
				int type = (int) data.get("type");
				// TODO create / forward to table
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
