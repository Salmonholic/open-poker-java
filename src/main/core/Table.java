package main.core;

public class Table {
	
	Player[] players;
	CardStack cardStack;
	Card[] cards;
	int buttonId = 0;
	int smallBlindId = 1;
	int bigBlindId = 1;
	int currentBet = 0;
	int pot = 0;
	int smallBlind = 5;

	/**
	 * Poker table
	 * @param players Players
	 */
	public Table(Player[] players) {
		this.players = players;
		// Loop for table
		while (true) {
			// Give the button around
			buttonId++;
			if (buttonId > players.length) {
				buttonId = 0;
			}
			//Get the blinds from all players
			blinds();
			// Give 2 Cards to every player
			giveCards();
			waitForBets();
			// Flop
			flop();
			waitForBets();
			// Turn
			turn();
			waitForBets();
			// River
			river();
			waitForBets();
			// ShowDown
			showDown();
			reset();
		}
	}
	
	public void reset() {
		for (int i = 0; i < players.length; i++) {
			if (players[i].getMoney() < 0) {
				removePlayer(i);
				i--;
			} else {
				players[i].reset();
			}
			
		}
	}
	
	/**
	 * Adds a card to cards
	 * @param card Card to add
	 */
	public void addCard(Card card) {
		Card[] newCards = new Card[cards.length + 1];
		for (int i = 0; i < players.length; i++) {
			newCards[i] = cards[i];
		}
		newCards[newCards.length] = card;
		setCards(newCards);
	}
	
	/**
	 * Adds 3 Cards
	 */
	public void flop() {
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
	}
	
	/**
	 * Adds 1 Card
	 */
	public void turn() {
		addCard(cardStack.getCard());
	}
	
	/**
	 * Adds 1 Card
	 */
	public void river() {
		addCard(cardStack.getCard());
	}
	
	public void showDown() {
		
	}
	
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
	
	/**
	 * Get the blinds from all players
	 */
	public void blinds() {
		players[bigBlindId].addMoney(smallBlind * -2);
		players[smallBlindId].addMoney(smallBlind * -1);
	}
	
	/**
	 * Waits until all the players have either set the same bet or have fold
	 */
	public void waitForBets() {
		
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

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}
	
	public void addToPot(int amount) {
		setPot(getPot() + amount);
	}
}
