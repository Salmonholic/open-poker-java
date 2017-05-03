package main.ui.graphical.states;

import java.io.IOException;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.connection.Player;
import main.connection.Update;
import main.core.Action;
import main.ui.graphical.ClientGUI;
import main.ui.graphical.states.game.PlayerInfo;
import main.ui.graphical.states.game.TableInfo;

public class GameState extends State implements ChangeListener<Update> {

	@SuppressWarnings("unused")
	private ClientGUI clientGUI;
	private Scene scene;

	private TableInfo tableInfo;
	private VBox playersInfoVBox;
	private HashMap<Integer, PlayerInfo> playerInfos;

	public GameState(final ClientGUI clientGUI, int playerAmount) {
		this.clientGUI = clientGUI;

		clientGUI.getClient().getUpdateProperty().addListener(this);

		BorderPane root = new BorderPane();

		VBox gameInfoVBox = new VBox();
		gameInfoVBox.setPadding(new Insets(10));
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

		ScrollPane gameInfoScrollPane = new ScrollPane(gameInfoVBox);

		root.setCenter(gameInfoScrollPane);

		VBox actionVBox = new VBox();
		actionVBox.setPadding(new Insets(10));
		actionVBox.setSpacing(5);

		final ComboBox<String> comboBox = new ComboBox<>();

		comboBox.setTooltip(new Tooltip("Select action"));
		for (Action action : Action.values()) {
			comboBox.getItems().add(action.toString());
		}

		final TextField moneyTextField = new TextField();
		moneyTextField.setTooltip(new Tooltip("Money"));
		moneyTextField.setPromptText("Money");
		moneyTextField.setEditable(false);
		
		moneyTextField.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                moneyTextField.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });

		comboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				switch (comboBox.getValue()) {
				case "FOLD":
				case "CHECK":
				case "CALL":
					moneyTextField.setEditable(false);
					moneyTextField.setText("0");
					break;
				default:
					moneyTextField.setEditable(true);
					break;
				}
			}
		});

		Button submitButton = new Button("Submit");

		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					int money = 0;
					if(!moneyTextField.getText().isEmpty())
						money = Integer.parseInt(moneyTextField.getText());
					
					clientGUI.getClient().sendAction(
							Action.valueOf(comboBox.getValue()), money);
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		actionVBox.getChildren().addAll(comboBox, moneyTextField, submitButton);

		root.setRight(actionVBox);

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
		tableInfo.setCurrentPlayer(update.getCurrentPlayer());
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
				playerInfo.setCards(update.getYourCards().get(0), update
						.getYourCards().get(1));
			}
			playerInfo.setPrimaryPlayer(false);
		}
	}

	@Override
	public void changed(ObservableValue<? extends Update> observable,
			Update oldValue, Update newValue) {
		update(newValue);
	}

}
