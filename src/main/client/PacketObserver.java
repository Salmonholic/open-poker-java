package main.client;

import main.connection.Packet;

public interface PacketObserver {
	
	public void onPacket(Packet packet);
	
}
