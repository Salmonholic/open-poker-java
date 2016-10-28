package main.core;

public class Table {
	Player[] players;

	/**
	 * Poker table
	 * @param players Players
	 */
	public Table(Player[] players) {
		this.players = players;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
