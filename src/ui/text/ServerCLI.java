package ui.text;

import main.server.Server;

public class ServerCLI extends CLI {
	
	private Server server;
	
	public static void main(String[] args) {
		new ServerCLI();
	}
	
	public ServerCLI() {
		super();
		System.out.println("Port (10101):");
		int port = Integer.parseInt(scanner.nextLine());
		System.out.println("Players:");
		int players = Integer.parseInt(scanner.nextLine());
		try {
			server = new Server(port, players);
		} catch (Exception e) {
			System.out.println("Start up error!");
			e.printStackTrace();
			return;
		}

		startCLI();
	}

	@Override
	void onCommand(String command, String[] args) {
		System.out.println();
		switch (command) {
		case "info":
			System.out.println("Information");
			for (int tableId : server.getTables().keySet()) {
				// TODO TableController tableController = server.getTableController(tableId);
				System.out.println("  " + tableId);
			}
			break;

		default:
			break;
		}
	}

}
