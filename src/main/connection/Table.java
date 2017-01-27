package main.connection;

import java.io.Serializable;

public class Table implements Serializable {
	
	private static final long serialVersionUID = -2793928917346062006L;
	
	private int id;
	private int startMoney;
	private int playersOnline;
	private int maxPlayers;
	
	public Table(int id, int startMoney, int playersOnline, int maxPlayers) {
		super();
		this.id = id;
		this.startMoney = startMoney;
		this.playersOnline = playersOnline;
		this.maxPlayers = maxPlayers;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStartMoney() {
		return startMoney;
	}
	
	public int getPlayersOnline() {
		return playersOnline;
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}

}