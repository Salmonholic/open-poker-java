package main.ui.graphical.states;

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
import main.ui.graphical.ClientGUI;
import main.ui.graphical.GUIPacketObserver;

public class LobbyState extends State {

	private static final String DEFAULT_USERNAME = System
			.getProperty("user.name");

	private ClientGUI clientGUI;
	private Scene scene;

	public LobbyState(final ClientGUI clientGUI) {
		this.clientGUI = clientGUI;

		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(5);

		final Text text = new Text("Lobby");
		root.getChildren().add(text);

		final Text loginText = new Text("Log in");
		final TextField loginUsernameTextField = new TextField(DEFAULT_USERNAME);
		loginUsernameTextField.setTooltip(new Tooltip("Username"));
		loginUsernameTextField.setPromptText("Username");
		final PasswordField loginPasswordField = new PasswordField();
		loginPasswordField.setTooltip(new Tooltip("Password"));
		loginPasswordField.setPromptText("Password");

		final Button loginButton = new Button("Log in");
		loginButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				createPacketObserver("login");
				clientGUI.getClient().login(loginUsernameTextField.getText(),
						loginPasswordField.getText());
			}
		});

		root.getChildren().addAll(loginText, loginUsernameTextField,
				loginPasswordField, loginButton);

		final Text signupText = new Text("Sign up");
		final TextField signupUsernameTextField = new TextField(
				DEFAULT_USERNAME);
		signupUsernameTextField.setTooltip(new Tooltip("Username"));
		signupUsernameTextField.setPromptText("Username");
		final PasswordField signupPasswordField = new PasswordField();
		signupPasswordField.setTooltip(new Tooltip("Password"));
		signupPasswordField.setPromptText("Password");
		final PasswordField signupRepeatPasswordField = new PasswordField();
		signupRepeatPasswordField.setTooltip(new Tooltip("Repeat password"));
		signupRepeatPasswordField.setPromptText("Password");

		final Button signupButton = new Button("Sign up");
		signupButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				createPacketObserver("signup");
				clientGUI.getClient().signup(signupUsernameTextField.getText(),
						signupPasswordField.getText());
			}
		});

		final Text usernameInfoText = new Text(
				"Username has to be longer than 2 characters and unique!");
		final Text passwordInfoText = new Text(
				"Password has to be longer than 7 characters!");

		root.getChildren().addAll(signupText, signupUsernameTextField,
				signupPasswordField, signupRepeatPasswordField, signupButton,
				usernameInfoText, passwordInfoText);

		scene = new Scene(root);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	private void createPacketObserver(String action) {
		new GUIPacketObserver(clientGUI, new SelectTableState(clientGUI),
				new ErrorState(clientGUI, "Failed to authorize", new ConnectState(
						clientGUI)), action);

	}

}
