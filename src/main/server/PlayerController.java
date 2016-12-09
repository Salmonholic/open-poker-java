package main.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import main.connection.Packet;
import main.core.Table;

public class PlayerController implements Runnable{
	
	private Socket socket;
	private TableController tableController;
	private String username;
	private String type;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public PlayerController(Socket socket, Server server) throws Exception {
		this.socket = socket;
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		
		Packet packet = (Packet) in.readObject();
		HashMap<String, Object> data = packet.getData();
		username = (String) data.get("username");
		tableController = server.getTableController((int) data.get("table"));
		type = (String) data.get("type");
		
		tableController.addPlayerController(this);
	}
	
	@Override
	public void run() {
		while (true) {
			
		}
	}

	public void resend(Table table) {
		Packet packet = null;
		try {
			out.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
