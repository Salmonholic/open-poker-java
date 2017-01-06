package main.ui.graphical.states;

import main.ui.graphical.ClientGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ConnectState extends State {
	private ClientGUI clientGUI;
	
	public ConnectState(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}
	
	public Scene getScene() {
		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);
		
		Text text = new Text("Welcome to open-poker-java");
		
		TextField hostTextField = new TextField("127.0.0.1");
		hostTextField.setTooltip(new Tooltip("Host"));
		hostTextField.setPromptText("Username");
		TextField portTextField = new TextField("10101");
		portTextField.setTooltip(new Tooltip("Port"));
		portTextField.setPromptText("Port");
		
		Button connectButton = new Button("Connect");
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				clientGUI.setState(new LobbyState(clientGUI));
			}
		});
		
		root.getChildren().addAll(text, hostTextField, portTextField, connectButton);
		Scene scene = new Scene(root);
		return scene;
	}
	
}
