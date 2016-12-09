package main.connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

import main.core.Card;
import main.core.Table;

public class Update implements Serializable {

	private static final long serialVersionUID = 7051737781693426600L;
	private ArrayList<Card> communityCards = new ArrayList<>(5);
	private int currentPot;

	private int buttonId = -1;
	private int smallBlindId;
	private int bigBlindId;
	private int smallBlind = 5;

	private int currentBet;
	private int currentPlayer = 0;

	private ArrayList<Card> yourCards;

	TreeMap<Integer, Player> players;

	public Update(Table table, int id) {
		communityCards = table.getCards();
		for (int pot : table.getPot()) {
			currentPot += pot;
		}

		buttonId = table.getButtonId();
		smallBlindId = table.getSmallBlindId();
		bigBlindId = table.getBigBlindId();
		smallBlind = table.getSmallBlind();

		currentBet = table.getCurrentBet();
		currentPlayer = table.getCurrentPlayer();

		yourCards = table.getPlayer(id).getCards();

		for (int playerId : table.getPlayers().keySet()) {
			main.core.Player player = table.getPlayer(playerId);
			players.put(playerId, new Player(player.getMoney(),
					player.isFold(), player.isAllIn(), player.getCurrentBet()));
		}
	}

	public ArrayList<Card> getCommunityCards() {
		return communityCards;
	}

	public int getCurrentPot() {
		return currentPot;
	}

	public int getButtonId() {
		return buttonId;
	}

	public int getSmallBlindId() {
		return smallBlindId;
	}

	public int getBigBlindId() {
		return bigBlindId;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public ArrayList<Card> getYourCards() {
		return yourCards;
	}

	public TreeMap<Integer, Player> getPlayers() {
		return players;
	}

}
