package main.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.ui.graphical.states.GameState;
import main.ui.graphical.states.game.TableInfo;

public class TestAplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Scene scene = new Scene();
		primaryStage.setScene(new GameState(null).getScene());
		primaryStage.show();
	}
}
