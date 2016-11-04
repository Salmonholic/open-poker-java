package main.core;

public class Player {
	private Table table;
	private int money;
	private Card[] cards;
	private boolean fold;
	private boolean allIn;
	
	/**
	 * 
	 * @param money
	 */
	public Player(Table table, int money) {
		this.table = table;
		this.money = money;
	}
	
	public void addMoney(int money) {
		setMoney(getMoney()+money);
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
	
	public boolean isFold() {
		return fold;
	}

	public void setFold(boolean fold) {
		this.fold = fold;
	}

	public boolean isAllIn() {
		return allIn;
	}

	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}

	public void fold() {
		setFold(true);
	}
	
	public void call() {
		
	}
	
	public void check() {
		
	}
	
	public void bet(int amount) {
		
	}
	
	public void raise(int amount) {
		
	}
}
