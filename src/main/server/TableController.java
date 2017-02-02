package main.server;

import java.util.ArrayList;
import java.util.Iterator;

import main.core.Action;
import main.core.Table;

public class TableController {
	
	private Server server;
	private Table table;
	private int tableId;
	private ArrayList<PlayerController> playerControllers = new ArrayList<>();
	// Amount of players to start
	private int maxPlayerAmount;
	private int money;
	// Id of last Player connected (NOT necessarily amount of online players)
	private int currentPlayer = 0;
	private boolean started = false;

	public TableController(Server server, int maxPlayerAmount, int money, int tableId) {
		this.server = server;
		this.maxPlayerAmount = maxPlayerAmount;
		this.money = money;
		this.tableId = tableId;
	}
	
	public void addPlayerController(PlayerController playerController) {
		if (started) {
			throw new IllegalStateException();
		}
		playerController.setId(currentPlayer);
		currentPlayer++;
		playerControllers.add(playerController);
		if (getPlayerAmount() == maxPlayerAmount) {
			started = true;
			table = new Table(this, getPlayerAmount(), money);
			table.resend();
		}
	}
	
	public void action(int playerId, Action action, int amount) {
		if (started) {
			try {
				System.out.println("Player " + playerId + " does " + action + " amount " + amount);
				table.action(playerId, action, amount);
			} catch (IllegalArgumentException e) {
				System.out.println("Player " + playerId + " tried forbidden action.");
				//TODO send info to client
				resend();
			}
		}
	}

	/**
	 * Resends all data to clients
	 */
	public void resend() {
		for (PlayerController playerController : playerControllers) {
			playerController.resend(table);
		}
	}
	
	public void removePlayer(int id) {
		if (started)
			table.removePlayer(id);
		Iterator<PlayerController> iterator = playerControllers.iterator();
		while (iterator.hasNext()) {
			PlayerController playerController = iterator.next();
			if (playerController.getId() == id) {
				playerController.clearAfterRemovalFromTable();
				iterator.remove();
				break;
			}
		}
		if (started && getPlayerAmount() == 1) {
			System.out.println("Table " + tableId + " finished.");
			//TODO inform client (win)
			playerControllers.get(0).clearAfterRemovalFromTable();
			close();
		}
	}
	
	public void close() {
		for(PlayerController playerController : playerControllers) {
			playerController.clearAfterRemovalFromTable();
		}
		server.removeTable(tableId);
	}
	
	public int getMaxPlayerAmount() {
		return maxPlayerAmount;
	}
	
	public int getPlayerAmount() {
		return playerControllers.size();
	}

	public int getMoney() {
		return money;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean isStarted() {
		return started;
	}
	
}
