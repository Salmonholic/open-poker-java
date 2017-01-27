package main.ui.graphical.states;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import main.connection.Table;
import main.ui.graphical.ClientGUI;

public class SelectTableState extends State implements
		ListChangeListener<Table> {
	private ClientGUI clientGUI;
	private Scene scene;

	private TableView<PokerTable> tableView = new TableView<PokerTable>();

	private final ObservableList<PokerTable> data = FXCollections
			.observableArrayList();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SelectTableState(final ClientGUI clientGUI) {
		this.clientGUI = clientGUI;

		// Get info about table
		clientGUI.getClient().sendGetTablesPacket();
		clientGUI.getClient().getTables().addListener(this);

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
		joinButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				PokerTable pokerTable = tableView.getSelectionModel()
						.getSelectedItem();
				if (pokerTable != null) {
					clientGUI.getClient().joinTable(pokerTable.getId());
					clientGUI.setState(new GameState(clientGUI, pokerTable
							.getMaxPlayers()));// TODO info if res not found
				}
			}
		});

		root.getChildren().addAll(text, tableView, addTableButton, joinButton);
		scene = new Scene(root);
	}

	@Override
	public Scene getScene() {
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
					addPokerTable(new PokerTable(id, startMoney, 0, maxPlayers));
				} catch (Exception e) {
				} finally {
					stage.close();
				}
			}
		});

		final Text idInfoText = new Text("Id has to be unique!");
		final Text startMoneyInfoText = new Text(
				"Start money has to be creater or equal thon 0!");
		final Text maxPlayersInfoText = new Text(
				"Amount of players has to be greater than 1!");

		root.getChildren().addAll(text, idTextField, startMoneyTextField,
				maxPlayersTextField, addTableButton, idInfoText,
				startMoneyInfoText, maxPlayersInfoText);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.showAndWait();
	}

	public void addPokerTable(PokerTable pokerTable) {
		// Validate values

		if (pokerTable.getMaxPlayers() < 2)
			return;
		// TODO add fields for blinds and check start money accordingly (>10
		// until that)
		if (pokerTable.getStartMoney() <= 10)
			return;

		for (PokerTable otherPokerTable : data) {
			if (pokerTable.getId() == otherPokerTable.getId()) {
				return;
			}
		}
		// TODO add table on server
		data.add(pokerTable);
	}

	private void updateTable() {
		data.clear();
		for (Table table : clientGUI.getClient().getTables()) {
			PokerTable pokerTable = new PokerTable(table.getId(),
					table.getStartMoney(), table.getPlayersOnline(),
					table.getMaxPlayers());
			data.add(pokerTable);
		}
	}

	@Override
	public void onChanged(Change<? extends Table> c) {
		updateTable();
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
