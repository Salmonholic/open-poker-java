package main.ui.graphical;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(getLoginScene());
		primaryStage.setTitle("open-poker-java");
		primaryStage.show();
	}
	
	private Scene getLoginScene() {
		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);
		
		Text text = new Text("Welcome to open-poker-java");
		
		TextField hostTextField = new TextField("127.0.0.1");
		hostTextField.setTooltip(new Tooltip("Host"));
		TextField portTextField = new TextField("10101");
		hostTextField.setTooltip(new Tooltip("Port"));
		
		Button connectButton = new Button("Connect");
		
		root.getChildren().addAll(text, hostTextField, portTextField, connectButton);
		Scene scene = new Scene(root);
		return scene;
	}
	
}
