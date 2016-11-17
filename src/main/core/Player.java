package main.core;

import java.util.ArrayList;

public class Player {
	private Table table;
	private int id;
	private int money;
	private ArrayList<Card> cards;
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
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	/**
	 * Set the cards of the Player
	 * @param cards Cards for player
	 */
	public void setCards(ArrayList<Card> cards) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void fold() {
		fold = true;
	}
	
	public void call() {
		int amountToBet = table.getCurrentBet() - this.currentBet;
		if(money - amountToBet > 0) { //Check for All-In
			currentBet += amountToBet;
			table.addToPot(amountToBet);
			addMoney(-amountToBet);
		} else {
			allIn();
		}
	}
	
	public void check() {
		
	}
	
	public void bet(int amount) {
		if(money > amount) {
			currentBet += amount;
			table.addToPot(amount);
			addMoney(-amount);
		} else if (money == amount) {
			allIn();
		} else {
			//TODO not enough money to bet (exception?)
		}
	}
	
	public void raise(int amount) {
		
	}
	
	private void allIn() {
		currentBet += money;
		table.addToPot(money);
		money = 0;
		allIn = true;
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

	public int getCurrentBet() {
		return currentBet;
	}
	
}
