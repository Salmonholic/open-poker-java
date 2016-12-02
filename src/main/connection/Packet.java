package main.connection;

import java.util.HashMap;

public class Packet {

	private String type;
	private HashMap<String, Object> data;

	public Packet(String type, HashMap<String, Object> data) {
		super();
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

}
