package main.ui.text;

import main.server.Server;
import main.server.TableController;

public class ServerCLI extends CLI {

	private Server server;

	public static void main(String[] args) {
		new ServerCLI();
	}

	public ServerCLI() {
		super();
		System.out.println("Port (10101):");
		int port = Integer.parseInt(scanner.nextLine());
		try {
			server = new Server(port);
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
		case "info":
			System.out.println("Information");
			System.out.println("Port: " + server.getPort());
			System.out.println("Tables:");
			for (int tableId : server.getTables().keySet()) {
				TableController tableController = server
						.getTableController(tableId);
				System.out.println("  id: " + tableId + ", money: "
						+ tableController.getMoney() + ", "
						+ tableController.getCurrentPlayer() + "/"
						+ tableController.getPlayerAmount() + " players");
			}
			break;
		case "create":
			if (args.length == 3) {
				int id = Integer.parseInt(args[0]);
				if (server.getTables().containsKey(id)) {
					System.out.println("Id has to be unique!");
					break;
				}
				int players = Integer.parseInt(args[1]);
				if (players <= 1) {
					System.out.println("There have to be more than 2 players!");
					break;
				}
				int money = Integer.parseInt(args[2]);
				if (money <= 0) {
					System.out.println("Start money has to be greater than 0!");
					break;
				}
				server.createTableController(id, players, money);
				System.out.println("Created table with id " + id);
			} else {
				System.out.println("Wrong usage!");
			}
			break;
		case "exit":
		case "q":
			server.close();
			break;
		case "help":
			System.out.println("Commands:");
			System.out.println("info - Display server information");
			System.out.println("create <id> <players> <money> - Display server information");
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
		return server.isRunning();
	}
}
