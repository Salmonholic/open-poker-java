package main.server;

import java.util.ArrayList;

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
	
	public int addPlayerController(PlayerController playerController) {
		if (started) {
			throw new IllegalArgumentException();
		}
		currentPlayer++;
		playerControllers.add(playerController);
		if (currentPlayer == playerAmount) {
			started = true;
			table = new Table(this, playerAmount, money);
			table.resend();
		}
		return currentPlayer;
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
