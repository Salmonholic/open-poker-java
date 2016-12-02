package main.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import main.connection.Packet;

public class PlayerController implements Runnable, Observer{
	
	private Socket socket;
	private TableController tableController;
	private String username;
	private String type;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public PlayerController(Socket socket, Server server) throws Exception {
		this.socket = socket;
		this.tableController = tableController;
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		Packet packet = (Packet) in.readObject();
		HashMap<String, Object> data = packet.getData();
		username = (String) data.get("username");
		tableController = server.getTableController((int) data.get("table"));
		type = (String) data.get("type");
		
		tableController.addObserver(this);
	}
	
	@Override
	public void run() {
		while (true) {
			
		}
	}

	@Override
	public void update(Observable o, Object packetObject) {
		Packet packet = (Packet) packetObject;
		try {
			out.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
