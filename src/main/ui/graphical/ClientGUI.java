package main.ui.graphical;

import javafx.application.Application;
import javafx.stage.Stage;
import main.connection.Client;
import main.ui.graphical.states.ConnectState;
import main.ui.graphical.states.State;

public class ClientGUI extends Application {
	
	private Client client;
	
	private Stage primaryStage;
	private State state;

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
	
	public State getState() {
		return state;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
}
