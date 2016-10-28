package main.core;

public class Table {
	
	Player[] players;
	CardStack cardStack;

	/**
	 * Poker table
	 * @param players Players
	 */
	public Table(Player[] players) {
		this.players = players;
	}
	
	/**
	 * Give 2 Cards to the players
	 */
	public void giveCards() {
		cardStack.initCards();
		for (Player player : players) {
			Card[] playerCards = {cardStack.getCard(), cardStack.getCard()};
			player.setCards(playerCards);
		}
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
