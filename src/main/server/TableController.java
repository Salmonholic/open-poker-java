package main.server;

import java.util.ArrayList;

import main.core.Action;
import main.core.Table;

public class TableController {
	
	private Table table;
	private ArrayList<PlayerController> playerControllers = new ArrayList<>();
	int playerAmount;
	int money;
	int currentPlayer = 0;
	boolean started = false;
	
	public TableController(int playerAmount, int money) {
		this.playerAmount = playerAmount;
		this.money = money;
	}
	
	public void addPlayerController(PlayerController playerController) {
		if (started) {
			throw new IllegalStateException();
		}
		playerController.setId(currentPlayer);
		currentPlayer++;
		playerControllers.add(playerController);
		if (currentPlayer == playerAmount) {
			started = true;
			table = new Table(this, playerAmount, money);
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
				//TODO inform client
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
	
}
