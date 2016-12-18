package main.connection;

import java.io.Serializable;


public class Player implements Serializable {
	
	private static final long serialVersionUID = -6651862473998165800L;
	
	private int money;
	private boolean fold;
	private boolean allIn;
	private int currentBet;
	
	public Player(int money, boolean fold, boolean allIn, int currentBet) {
		this.money = money;
		this.fold = fold;
		this.allIn = allIn;
		this.currentBet = currentBet;
	}

	public int getMoney() {
		return money;
	}

	public boolean isFold() {
		return fold;
	}

	public boolean isAllIn() {
		return allIn;
	}

	public int getCurrentBet() {
		return currentBet;
	}
	
}
