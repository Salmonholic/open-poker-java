package main.ui.graphical.states;

import java.util.Iterator;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.ui.graphical.ClientGUI;
import main.ui.graphical.states.SelectTableState.PokerTable;

public class SelectTableState extends State {
	ClientGUI clientGUI;

	TableView<PokerTable> tableView = new TableView<PokerTable>();

	private final ObservableList<PokerTable> data = FXCollections
			.observableArrayList(new PokerTable(0, 100, 0, 3), new PokerTable(
					1, 1000, 2, 4), new PokerTable(2, 23, 3, 3));

	public SelectTableState(ClientGUI clientGUI) {
		this.clientGUI = clientGUI;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Scene getScene() {
		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);

		Text text = new Text("Select or create a table");

		tableView.setEditable(true);

		tableView.setEditable(true);

		TableColumn idTableColumn = new TableColumn("Id");
		idTableColumn
				.setCellValueFactory(new PropertyValueFactory<PokerTable, Integer>(
						"id"));

		TableColumn starMoneyTableColumn = new TableColumn("Start money");
		starMoneyTableColumn
				.setCellValueFactory(new PropertyValueFactory<PokerTable, Integer>(
						"startMoney"));

		TableColumn playersOnlineTableColumn = new TableColumn("Players online");
		playersOnlineTableColumn
				.setCellValueFactory(new PropertyValueFactory<PokerTable, Integer>(
						"playersOnline"));

		TableColumn maxPlayersTableColumn = new TableColumn("Max. Players");
		maxPlayersTableColumn
				.setCellValueFactory(new PropertyValueFactory<PokerTable, Integer>(
						"maxPlayers"));

		tableView.setItems(data);
		tableView.getColumns().addAll(idTableColumn, starMoneyTableColumn,
				playersOnlineTableColumn, maxPlayersTableColumn);

		Button addTableButton = new Button("Add table");
		addTableButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				showAddTablePopup();
			}
		});

		Button joinButton = new Button("Join selected table");

		root.getChildren().addAll(text, tableView, addTableButton, joinButton);
		Scene scene = new Scene(root);
		return scene;
	}

	public void showAddTablePopup() {
		final Stage stage = new Stage();
		stage.setTitle("Add a new table");
		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(5);

		Text text = new Text("Add a new table");

		final TextField idTextField = new TextField("");
		idTextField.setTooltip(new Tooltip("Id (unique)"));
		idTextField.setPromptText("Id");

		final TextField startMoneyTextField = new TextField("1000");
		startMoneyTextField.setTooltip(new Tooltip("Start money"));
		startMoneyTextField.setPromptText("Start money");

		final TextField maxPlayersTextField = new TextField("");
		maxPlayersTextField.setTooltip(new Tooltip("Max players"));
		maxPlayersTextField.setPromptText("Max players");

		Button addTableButton = new Button("Add Table");
		addTableButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					int id = Integer.parseInt(idTextField.getText());
					int startMoney = Integer.parseInt(startMoneyTextField
							.getText());
					int maxPlayers = Integer.parseInt(maxPlayersTextField
							.getText());
					data.add(new PokerTable(id, startMoney, 0, maxPlayers));
				} catch (Exception e) {
				} finally {
					stage.close();
				}
			}
		});

		root.getChildren().addAll(text, idTextField, startMoneyTextField,
				maxPlayersTextField, addTableButton);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void addPokerTable(PokerTable pokerTable) {
		// Validate values
		
		if (pokerTable.getMaxPlayers() < 2) return;
		if (pokerTable.getStartMoney() <= 0) return;
		
		// Check if id is unique
		for (Iterator<PokerTable> iterator = data.iterator(); iterator.hasNext();) {
			PokerTable otherPokerTable = iterator.next();
			if (pokerTable.getId() == otherPokerTable.getId()) {
				return;
			}
		}
		data.add(pokerTable);
	}

	public class PokerTable {
		private final SimpleIntegerProperty id;
		private final SimpleIntegerProperty startMoney;
		private final SimpleIntegerProperty playersOnline;
		private final SimpleIntegerProperty maxPlayers;

		public PokerTable(int id, int startMoney, int playersOnline,
				int maxPlayers) {
			super();
			this.id = new SimpleIntegerProperty(id);
			this.startMoney = new SimpleIntegerProperty(startMoney);
			this.playersOnline = new SimpleIntegerProperty(playersOnline);
			this.maxPlayers = new SimpleIntegerProperty(maxPlayers);
		}

		public int getId() {
			return id.get();
		}

		public int getStartMoney() {
			return startMoney.get();
		}

		public int getPlayersOnline() {
			return playersOnline.get();
		}

		public int getMaxPlayers() {
			return maxPlayers.get();
		}

	}

}
