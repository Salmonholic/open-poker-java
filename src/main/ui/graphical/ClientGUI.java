package main.ui.graphical;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.connection.Client;
import main.ui.graphical.states.ConnectState;
import main.ui.graphical.states.State;

public class ClientGUI extends Application {
	
	private SimpleObjectProperty<Client> client;
	
	private Stage primaryStage;
	private State state;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		client = new SimpleObjectProperty<>();
		primaryStage.setTitle("open-poker-java");
		setState(new ConnectState(this));
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent windowEvent) {
				getClient().close();
			}
		});
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
		return client.get();
	}

	public void setClient(Client client) {
		this.client.set(client);
	}
	
	public SimpleObjectProperty<Client> getClientProperty() {
		return client;
	}
	
}
