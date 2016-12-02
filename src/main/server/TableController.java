package main.server;

import java.util.HashMap;
import java.util.Observable;

import main.connection.Packet;
import main.core.Table;

public class TableController extends Observable{
	
	private Table table;

	/**
	 * Resends all data to clients
	 */
	public void resend() {
		HashMap<String, Object> data = new HashMap<>();
		data.put("table", table);
		Packet packet = new Packet("update", data);
	}
	
}
