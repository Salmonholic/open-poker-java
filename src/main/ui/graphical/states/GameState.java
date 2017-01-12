package main.ui.graphical.states;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.connection.Player;
import main.connection.Update;
import main.ui.graphical.ClientGUI;
import main.ui.graphical.states.game.PlayerInfo;
import main.ui.graphical.states.game.TableInfo;

public class GameState extends State implements ChangeListener<Update> {

	private ClientGUI clientGUI;
	private Scene scene;

	TableInfo tableInfo;
	VBox playersInfoVBox;

	public GameState(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
		
		clientGUI.getClient().getUpdateProperty().addListener(this);
		
		BorderPane root = new BorderPane();

		VBox gameInfoVBox = new VBox();
		gameInfoVBox.setPadding(new Insets(20));
		gameInfoVBox.setSpacing(5);

		tableInfo = new TableInfo();

		playersInfoVBox = new VBox();
		playersInfoVBox.setSpacing(5);

		gameInfoVBox.getChildren().addAll(tableInfo, playersInfoVBox);

		root.setCenter(gameInfoVBox);

		scene = new Scene(root);
	}

	public Scene getScene() {
		return scene;
	}

	public void update(Update update) {
		// Clear Players
		playersInfoVBox.getChildren().clear();
		// Update Information
		tableInfo.setCurrentPot(update.getCurrentPot());
		tableInfo.setCurrentPot(update.getCurrentBet());
		tableInfo.setCards(update.getCommunityCards().get(0), update.getCommunityCards().get(1),
				update.getCommunityCards().get(2));
		tableInfo.setGameState(update.getGameState());
		// Add players
		for (int playerId : update.getPlayers().keySet()) {
			PlayerInfo playerInfo = new PlayerInfo();
			Player player = update.getPlayers().get(playerId);
			playerInfo.setMoney(player.getMoney());
			playerInfo.setAllIn(player.isAllIn());
			playerInfo.setFold(player.isFold());
			playerInfo.setCurrentBet(player.getCurrentBet());
			if (playerId == update.getYourId()) {
				playerInfo.setPrimaryPlayer(true);
				playerInfo.setCards(update.getYourCards().get(0), update.getYourCards().get(1));
			}
			playerInfo.setPrimaryPlayer(false);
			playersInfoVBox.getChildren().add(playerInfo);
		}
	}

	@Override
	public void changed(ObservableValue<? extends Update> observable, Update oldValue, Update newValue) {
	}

}
