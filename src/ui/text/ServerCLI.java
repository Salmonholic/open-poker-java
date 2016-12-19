package ui.text;

import main.server.Server;
import main.server.TableController;

public class ServerCLI extends CLI {
	
	private Server server;
	
	public static void main(String[] args) throws Exception {
		new ServerCLI();
	}
	
	public ServerCLI() throws Exception {
		super();
		System.out.println("Port (10101):");
		int port = Integer.parseInt(scanner.nextLine());
		System.out.println("Players:");
		int players = Integer.parseInt(scanner.nextLine());
		server = new Server(port, players);

		startCLI();
	}

	@Override
	void onCommand(String command, String[] args) throws Exception {
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
