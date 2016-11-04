package main.core;

public class Table {
	
	Player[] players;
	CardStack cardStack;
	int buttonId = 0;
	
	int currentBet = 0;

	/**
	 * Poker table
	 * @param players Players
	 */
	public Table(Player[] players) {
		this.players = players;
		// Loop for table
		while (true) {
			buttonId++;
			giveCards();
			break;
		}
	}
	
	public void removePlayer(int playerId) {
		Player[] newPlayers = new Player[players.length - 1];
		int newIndex = 0;
		for (int i = 0; i < players.length; i++) {
			if (i != playerId) {
				newPlayers[newIndex] = players[i];
				newIndex++;
			}
		}
		setPlayers(newPlayers);
	}
	
	/**
	 * Give 2 Cards to each player
	 */
	public void giveCards() {
		cardStack.initCards();
		for (Player player : players) {
			Card[] playerCards = {cardStack.getCard(), cardStack.getCard()};
			player.setCards(playerCards);
		}
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
}
