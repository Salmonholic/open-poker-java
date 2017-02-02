package main.ui.graphical;

import javafx.application.Platform;
import main.client.PacketObserver;
import main.connection.Packet;
import main.ui.graphical.states.State;

public class GUIPacketObserver implements PacketObserver {
	
	private ClientGUI clientGUI;
	private State acceptState;
	private State declineState;
	

	public GUIPacketObserver(ClientGUI clientGUI, State acceptState, State declineState) {
		this.clientGUI = clientGUI;
		this.acceptState = acceptState;
		this.declineState = declineState;
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
		switch (packet.getType()) {
		case "accept":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					clientGUI.setState(acceptState);
				}
			});
			removePackageObserver();
			break;
		case "decline":
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					clientGUI.setState(declineState);
				}
			});
			removePackageObserver();
			break;
		default:
			break;
		}
	}

}
