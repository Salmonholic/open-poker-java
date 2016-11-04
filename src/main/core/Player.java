package main.core;

public class Player {
	private int money;
	private Card[] cards;
	
	/**
	 * 
	 * @param money
	 */
	public Player(int money) {
		this.money = money;
	}
	
	public int addMoney(int money) {
		return this.money += money;
	}

	/**
	 * Get money of Player
	 * @return Money of player
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Set money of Player
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
	 * @param cards Cards for player
	 */
	public void setCards(Card[] cards) {
		this.cards = cards;
	}
	
	
	
}
