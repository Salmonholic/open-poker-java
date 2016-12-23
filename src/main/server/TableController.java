package main.server;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.plaf.TableHeaderUI;

import main.core.Action;
import main.core.Table;

public class TableController {
	
	private Table table;
	private int tableId;
	private ArrayList<PlayerController> playerControllers = new ArrayList<>();
	private int playerAmount;
	private int money;
	private int currentPlayer = 0;
	boolean started = false;
	
	public TableController(int playerAmount, int money, int tableId) {
		this.playerAmount = playerAmount;
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
		playerAmount--;
		Iterator<PlayerController> iterator = playerControllers.iterator();
		while (iterator.hasNext()) {
			PlayerController playerController = iterator.next();
			if (playerController.getId() == id) {
				iterator.remove();
				break;
			}
		}
		if (playerAmount == 1) {
			System.out.println("Table " + tableId + " finished.");
			//TODO inform client, delete room
		}
	}
	
	public void close() {
		while(playerControllers.size() > 0) {
			playerControllers.get(0).close();
		}
	}
}
