package main.ui.graphical.states;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.connection.Client;
import main.ui.graphical.ClientGUI;

public class ConnectState extends State {

	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_PORT = "10101";

	@SuppressWarnings("unused")
	private ClientGUI clientGUI;
	private Scene scene;

	public ConnectState(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;

		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);
		
		Text text = new Text("Welcome to open-poker-java");
		
		TextField hostTextField = new TextField(DEFAULT_HOST);
		hostTextField.setTooltip(new Tooltip("Host"));
		hostTextField.setPromptText("Username");
		TextField portTextField = new TextField(DEFAULT_PORT);
		portTextField.setTooltip(new Tooltip("Port"));
		portTextField.setPromptText("Port");
		
		Button connectButton = new Button("Connect");
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					clientGUI.setClient(new Client(hostTextField.getText(), Integer.parseInt(portTextField.getText())));
					clientGUI.setState(new LobbyState(clientGUI));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		root.getChildren().addAll(text, hostTextField, portTextField, connectButton);
		scene = new Scene(root);
	}

	public Scene getScene() {
		return scene;
	}

}
