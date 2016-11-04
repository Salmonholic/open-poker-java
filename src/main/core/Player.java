package main.core;

public class Player {
	private Table table;
	private int money;
	private Card[] cards;
	private boolean fold;
	private boolean allIn;
	private int currentBet;
	
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
		int amountToBet = table.getCurrentBet() - this.currentBet;
		if(getMoney() - amountToBet > 0) { //Check for All-In
			addMoney(-amountToBet);
			table.addToPot(amountToBet);
		} else {
			//All-In
			this.currentBet += this.getMoney();
			setMoney(0);
			setAllIn(true);
		}
	}
	
	public void check() {
		
	}
	
	public void bet(int amount) {
		
	}
	
	public void raise(int amount) {
		
	}
	
	/**
	 * Resets the vars of the player after the round
	 */
	public void reset() {
		cards = null;
		currentBet = 0;
		allIn = false;
		fold = false;
	}
	
}
