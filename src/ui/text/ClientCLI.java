package ui.text;

import java.io.ObjectOutputStream;

import main.connection.Client;
import main.connection.Player;
import main.connection.Update;
import main.core.Action;
import main.core.Card;

public class ClientCLI extends CLI {

	Client client;
	
	public static void main(String[] args) throws Exception {
		new ClientCLI();
	}

	public ClientCLI() throws Exception {
		super();
		System.out.println("Host / IP:");
		String host = scanner.nextLine();
		System.out.println("Port (10101):");
		int port = Integer.parseInt(scanner.nextLine());
		System.out.println("Username:");
		String username = scanner.nextLine();
		System.out.println("Room (0):");
		int room = Integer.parseInt(scanner.nextLine());
		client = new Client(host, port, username, room);

		startCLI();
	}

	@Override
	void onCommand(String command, String[] args) throws Exception {
		switch (command) {
		case "action":
			System.out.println("Action");
			switch (args.length) {
			case 1:
				client.sendAction(Action.valueOf(args[0]));
				break;
			case 2:
				client.sendAction(Action.valueOf(args[0]), Integer.parseInt(args[1]));
				break;
			default:
				System.out.println("Wrong use!");
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
				System.out.println("    " + card.getColor()+ " " + card.getValue());
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
				System.out.println("  " + card.getColor()+ " " + card.getValue());
			}
			break;
		default:
			// Help
			System.out.println("Command not found!");
			System.out.println("Commands:");
			System.out.println("action <action> [amount] - Send an action to the server");
			break;
		}
	}

}
