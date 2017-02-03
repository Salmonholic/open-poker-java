package main.ui.graphical;

import javafx.application.Platform;
import main.client.PacketObserver;
import main.connection.Packet;
import main.ui.graphical.states.State;

public class GUIPacketObserver implements PacketObserver {
	
	private ClientGUI clientGUI;
	private State acceptState;
	private State declineState;
	private String action;
	

	public GUIPacketObserver(ClientGUI clientGUI, State acceptState, State declineState, String action) {
		this.clientGUI = clientGUI;
		this.acceptState = acceptState;
		this.declineState = declineState;
		this.action = action;
		addPackageObserver();
	}
	
	public void addPackageObserver() {
		clientGUI.getClient().addPacketObserver(this);
	}
	
	public void removePackageObserver() {
		clientGUI.getClient().removePacketObserver(this);
	}

	@Override
	public void onPacket(Packet packet) {
		System.out.println(action);
		switch (packet.getType()) {
		case "accept":
			if (isRightAction(packet)) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("Switching state to " + acceptState);
						clientGUI.setState(acceptState);
					}
				});
				removePackageObserver();
			}
			break;
		case "decline":
			if (isRightAction(packet)) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						clientGUI.setState(declineState);
					}
				});
				removePackageObserver();
			}
			break;
		default:
			break;
		}
	}
	
	private boolean isRightAction(Packet packet) {
		return packet.getData().get("action").equals(action);
		
	}

}
