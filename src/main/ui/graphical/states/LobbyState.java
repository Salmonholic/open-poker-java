package main.ui.graphical.states;

import main.ui.graphical.ClientGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LobbyState extends State {
	private ClientGUI clientGUI;

	public LobbyState(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}

	@Override
	public Scene getScene() {
		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);

		Text text = new Text("Lobby");
		root.getChildren().add(text);

		Text loginText = new Text("Log in");
		TextField loginUsernameTextField = new TextField();
		loginUsernameTextField.setTooltip(new Tooltip("Username"));
		loginUsernameTextField.setPromptText("Username");
		PasswordField loginPasswordField = new PasswordField();
		loginPasswordField.setTooltip(new Tooltip("Password"));
		loginPasswordField.setPromptText("Password");
		
		Button loginButton = new Button("Log in");
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				clientGUI.setState(new SelectTableState(clientGUI));
			}
		});
		
		root.getChildren().addAll(loginText, loginUsernameTextField,
				loginPasswordField, loginButton);

		Text signupText = new Text("Sign up");
		TextField signupUsernameTextField = new TextField();
		signupUsernameTextField.setTooltip(new Tooltip("Username"));
		signupUsernameTextField.setPromptText("Username");
		PasswordField signupPasswordField = new PasswordField();
		signupPasswordField.setTooltip(new Tooltip("Password"));
		signupPasswordField.setPromptText("Password");
		PasswordField signupRepeatPasswordField = new PasswordField();
		signupRepeatPasswordField.setTooltip(new Tooltip("Repeat password"));
		signupRepeatPasswordField.setPromptText("Password");
		
		Button signupButton = new Button("Sign up");
		
		root.getChildren().addAll(signupText, signupUsernameTextField,
				signupPasswordField, signupRepeatPasswordField, signupButton);

		Scene scene = new Scene(root);
		return scene;
	}

}
