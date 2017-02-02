package main.ui.graphical.states; 

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.ui.graphical.ClientGUI;

@SuppressWarnings("unused")
public class ErrorState extends State {

	private ClientGUI clientGUI;
	private Scene scene;
	private State nextState;
	
	public ErrorState(ClientGUI clientGUI, String error, State nextState) {
		this.clientGUI = clientGUI;
		this.nextState = nextState;
		
		final VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(5);
		
		Text errorText = new Text(error);
		
		Button okButton = new Button("Ok");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				clientGUI.setState(nextState);
			}
		});
		
		root.getChildren().addAll(errorText, okButton);
		scene = new Scene(root);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

}
