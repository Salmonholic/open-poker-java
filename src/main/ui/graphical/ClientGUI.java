package main.ui.graphical;

import javafx.application.Application;
import javafx.stage.Stage;
import main.ui.graphical.states.ConnectState;
import main.ui.graphical.states.State;

public class ClientGUI extends Application {
	
	Stage primaryStage;
	State state;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("open-poker-java");
		setState(new ConnectState(this));
		primaryStage.show();
	}
	
	public void setState(State state) {
		this.state = state;
		primaryStage.setScene(state.getScene());
	}
	
}
