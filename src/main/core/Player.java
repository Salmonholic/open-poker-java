package main.core;

public class Player {
	private int money;
	private Card[] cards;
	
	/**
	 * 
	 * @param money
	 */
	public Player(int money) {
		super();
		this.money = money;
	}

	/**
	 * Gets money of Player
	 * @return Money of player
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Sets money of Player
	 * @param money Money for Player
	 */
	public void setMoney(int money) {
		this.money = money;
	}
	
	/**
	 * Get the cards of the Player
	 * @return List of 2 Cards
	 */
	public Card[] getCards() {
		return cards;
	}
	
	/**
	 * Set the cards of the Player
	 * @param cards Card for player
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
	
	
	
}
