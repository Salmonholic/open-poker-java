package main.ui.graphical.states;

import java.util.HashMap;

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

	private TableInfo tableInfo;
	private VBox playersInfoVBox;
	private HashMap<Integer, PlayerInfo> playerInfos;

	public GameState(ClientGUI clientGUI, int playerAmount) {
		this.clientGUI = clientGUI;
		
		clientGUI.getClient().getUpdateProperty().addListener(this);
		
		BorderPane root = new BorderPane();

		VBox gameInfoVBox = new VBox();
		gameInfoVBox.setPadding(new Insets(20));
		gameInfoVBox.setSpacing(5);

		tableInfo = new TableInfo();

		playersInfoVBox = new VBox();
		playersInfoVBox.setSpacing(5);
		
		// Create info boxes for players
		
		playerInfos = new HashMap<>();
		for (int i = 0; i < playerAmount; i++) {
			PlayerInfo playerInfo = new PlayerInfo();
			playersInfoVBox.getChildren().add(playerInfo);
			playerInfos.put(i, playerInfo);
		}

		gameInfoVBox.getChildren().addAll(tableInfo, playersInfoVBox);

		root.setCenter(gameInfoVBox);

		scene = new Scene(root);
	}

	public Scene getScene() {
		return scene;
	}

	public void update(Update update) {
		// Update Information
		tableInfo.setCurrentPot(update.getCurrentPot());
		tableInfo.setCurrentBet(update.getCurrentBet());
		tableInfo.setCards(update.getCommunityCards());
		tableInfo.setGameState(update.getGameState());
		// Add players
		for (int playerId : update.getPlayers().keySet()) {
			PlayerInfo playerInfo = playerInfos.get(playerId);
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
		}
	}

	@Override
	public void changed(ObservableValue<? extends Update> observable, Update oldValue, Update newValue) {
		update(newValue);
	}

}
