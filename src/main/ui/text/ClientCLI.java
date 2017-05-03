package main.ui.text;

import java.io.IOException;
import java.util.List;

import main.client.Client;
import main.connection.Player;
import main.connection.Table;
import main.connection.Update;
import main.core.Action;
import main.core.Card;

public class ClientCLI extends CLI {

	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final String DEFAULT_PORT = "10101";
	public static final String DEFAULT_USERNAME = System.getProperty("user.name");
	public static final boolean DEFAULT_SIGNUP = false;

	private Client client;

	public static void main(String[] args) {
		new ClientCLI();
	}

	public ClientCLI() {
		super();

		// Host
		System.out.println("Host / IP (" + DEFAULT_HOST + "):");
		String host = scanner.nextLine();
		if (host.isEmpty())
			host = DEFAULT_HOST;

		// Port
		System.out.println("Port (" + DEFAULT_PORT + "):");
		String portString = scanner.nextLine();
		if (portString.isEmpty())
			portString = DEFAULT_PORT;
		int port = Integer.parseInt(portString);

		// Username
		System.out.println("Username (" + DEFAULT_USERNAME + "):");
		String username = scanner.nextLine();
		if (username.isEmpty())
			username = DEFAULT_USERNAME;

		// Password
		System.out.println("Password:");
		String password = scanner.nextLine();

		// Sign up
		System.out.println("Sign up? (" + DEFAULT_SIGNUP + "):");
		String signUpString = scanner.nextLine();
		if (signUpString.isEmpty())
			signUpString = String.valueOf(DEFAULT_SIGNUP);
		boolean signUp = Boolean.valueOf(signUpString);

		try {
			client = new Client(host, port);
			if (signUp) {
				client.signup(username, password);
			} else {
				client.login(username, password);
			}
		} catch (Exception e) {
			System.out.println("Start up error!");
			e.printStackTrace();
			return;
		}

		startCLI();
	}

	@Override
	protected void onCommand(String command, String[] args) {
		System.out.println();
		switch (command) {
		case "tables":
			if (args.length == 1) {
				if (args[0].equals("get")) {
					System.out.println("Getting info about tables");
					client.sendGetTablesPacket();
				} else if (args[0].equals("info")) {
					List<Table> tables = client.getTables();
					if (tables == null) {
						System.out.println("No information");
					} else {
						System.out.println("Table information");
						for (Table pokerTable : tables) {
							System.out.println("  id: " + pokerTable.getId() + ", money: " + pokerTable.getStartMoney()
									+ ", " + pokerTable.getPlayersOnline() + "/" + pokerTable.getMaxPlayers()
									+ " players");
						}
					}
				}
			} else {
				System.out.println("Wrong usage!");
				onCommand("help", null);
			}
			break;
		case "join":
			if (args.length == 1) {
				client.joinTable(Integer.parseInt(args[0]));
			} else {
				System.out.println("Wrong usage!");
				onCommand("help", null);
			}
			break;
		case "action":
			System.out.println("Action");
			switch (args.length) {
			case 1:
				try {
					client.sendAction(Action.valueOf(args[0].toUpperCase()));
				} catch (IllegalArgumentException e) {
					System.out.println("Action not found!");
				} catch (IOException e) {
					System.out.println("Network error! Could not send Action.");
				}
				break;
			case 2:
				try {
					client.sendAction(Action.valueOf(args[0].toUpperCase()), Integer.parseInt(args[1]));
				} catch (IllegalArgumentException e) {
					System.out.println("Action not found!");
				} catch (IOException e) {
					System.out.println("Network error! Could not send Action.");
				}
				break;
			default:
				System.out.println("Wrong usage!");
				onCommand("help", null);
				break;
			}
			break;
		case "info":
			System.out.println("Information");
			Update update = client.getUpdate();
			System.out.println("Infos");
			System.out.println("  Small blind: " + update.getSmallBlind());
			System.out.println("IDs");
			System.out.println("  Current Player ID: " + update.getCurrentPlayer());
			System.out.println("  Button ID: " + update.getButtonId());
			System.out.println("  Big Blind ID: " + update.getBigBlindId());
			System.out.println("  Small Blind ID: " + update.getSmallBlindId());
			System.out.println("Table");
			System.out.println("  Current Pot: " + update.getCurrentPot());
			System.out.println("  Community Cards: ");
			for (Card card : update.getCommunityCards()) {
				System.out.println("    " + card.getColor() + " " + card.getValue());
			}
			System.out.println("Players");
			System.out.println("  id: CurrentBet Money");
			for (int playerId : update.getPlayers().keySet()) {
				Player player = update.getPlayers().get(playerId);
				System.out.println("  " + playerId + ": " + player.getCurrentBet() + " " + player.getMoney());
			}
			System.out.println("You");
			System.out.println("  ID: " + update.getYourId());
			for (Card card : update.getYourCards()) {
				System.out.println("  " + card.getColor() + " " + card.getValue());
			}
			break;
		case "exit":
		case "q":
			// terminate
			client.close();
			break;
		case "help":
			System.out.println("Commands:");
			System.out.println("tables <action> - Get information about tables");
			System.out.println("  <action> may be one of: info, get");
			System.out.println("join <id> - Join room with id");
			System.out.println("info - Display game information");
			System.out.println("action <action> [amount] - Send an action to the server");
			System.out.println("  <action> may be one of: check, call, bet, raise, fold");
			System.out.println("exit, q - Quit game");
			break;
		default:
			// Help
			System.out.println("Command not found!");
			onCommand("help", null);
			break;
		}
	}

	@Override
	protected boolean checkRunningCondition() {
		return client.isRunning();
	}

}
