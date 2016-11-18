package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;
import handChecker.PokerCard.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Table {

	TreeMap<Integer, Player> players;
	CardStack cardStack;
	ArrayList<Card> cards;
	private ArrayList<Integer> pot = new ArrayList<>(1);
	int smallBlind = 5;
	int currentBet;
	GameState gameState = GameState.PRE_FLOP;
	
	int buttonId = 0;
	int smallBlindId;
	int bigBlindId;
	int lastBetId;
	int currentPlayer;

	HandChecker handChecker = new HandChecker();

	/**
	 * Poker table
	 * 
	 * @param players Amount of players in game
	 * @param money Start Money for each player
	 */
	public Table(int playerAmount, int money) {
		// Put players into TreeMap and set Id
		players = new TreeMap<>();
		for(int i=0; i< playerAmount; i++) {
			players.put(i, new Player(this, money));
		}
		update();
	}
	
	private void update() {
		if (currentPlayer == lastBetId) {
			gameState = GameState.values()[gameState.ordinal() % GameState.values().length];
			switch (gameState) {
			case PRE_FLOP:
				preFlop();
				break;
			case FLOP:
				flop();
				break;
			case TURN:
				turn();
				break;
			case RIVER:
				river();
				break;
			case SHOW_DOWN:
				// TODO showDown();
				gameState = GameState.PRE_FLOP;
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Get next player in players, or first player if no more player is in players
	 * @param playerId Player
	 */
	private int nextPlayer(int playerId) {
		if (players.ceilingKey(playerId) == playerId) {
			return players.firstKey();
		} else {
			return players.ceilingKey(playerId);
		}
		
	}
	
	public void action(int playerId, Action action) {
		action(playerId, action, 0);
	}
	
	public void action(int playerId, Action action, int value) {
		if (playerId == currentPlayer) {
			Player player = players.get(playerId);
			switch (action) {
			case BET:
				player.bet(value);
				break;
			case CALL:
				player.call();
				break;
			case CHECK:
				player.check();
				break;
			case FOLD:
				player.fold();
				break;
			case RAISE:
				player.raise(value);
				break;
			default:
				break;
			}
			currentPlayer = nextPlayer(playerId);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Give 2 Cards to each player
	 */
	private void giveCards() {
		for (Player player : players.values()) {
			player.setCards((ArrayList<Card>) Arrays.asList(cardStack.getCard(),
															cardStack.getCard()));
		}
	}

	/**
	 * Get the blinds from all players
	 */
	private void blinds() {
		players.get(bigBlindId).addMoney(smallBlind * -2);
		players.get(smallBlindId).addMoney(smallBlind * -1);
	}
	
	private void preFlop() {
		buttonId = nextPlayer(buttonId);
		if (players.size() == 2) {
			smallBlindId = buttonId;
			bigBlindId = nextPlayer(buttonId);
		} else {
			smallBlindId = nextPlayer(buttonId);
			bigBlindId = nextPlayer(smallBlindId);
		}
		cardStack.initCards();
		blinds();
		currentBet = smallBlind * 2;
		currentPlayer = nextPlayer(bigBlindId);
	}

	/**
	 * Adds 3 Cards
	 */
	private void flop() {
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
	}

	/**
	 * Adds 1 Card
	 */
	private void turn() {
		addCard(cardStack.getCard());
	}

	/**
	 * Adds 1 Card
	 */
	private void river() {
		addCard(cardStack.getCard());
	}

	/**
	 * Resets player flags and (side) pots. Has to be called each new round.
	 */
	private void reset() {
		for(Map.Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue().getMoney() < 0) {
				players.remove(entry.getKey());
			} else {
				entry.getValue().reset();
			}
		}
		
		pot.clear();
		pot.add(0);
	}

	/**
	 * Adds a card to the cards on the table
	 */
	private void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * Converts a Array of Cards to a List<Card>
	 * 
	 * @param cards Card Array to convert
	 * @return Converted cards
	 */
	private List<PokerCard> asList(ArrayList<Card> playerCards) {
		ArrayList<PokerCard> newCards = new ArrayList<>();
		for (PokerCard card : playerCards) {
			newCards.add(card);
		}
		return newCards;
	}

	/**
	 * Get the HandValue of an Player
	 * @param player Player
	 * @return HandValue of Player
	 */
	private HandValue checkPlayer(Player player) {
		ArrayList<Card> playerCards = player.getCards();
		playerCards.addAll(cards);
		return handChecker.check(asList(playerCards));
	}

	/**
	 * Generate an ArrayList with just the player in it
	 * 
	 * @param player Player to be in the Array list
	 * @return ArrayList with just the player in it
	 */
	private ArrayList<Player> getArrayListWithPlayer(Player player) {
		ArrayList<Player> list = new ArrayList<>();
		list.add(player);
		return list;
	}
	
	private void winningOrder() {
		TreeMap<HandValue, List<Player>> winningOrder = new TreeMap<>();
		// Sort out players who have fold
		players.entrySet().forEach((entry) -> {
			if(!entry.getValue().isFold()) {
				List<PokerCard> list = new ArrayList<>(cards);
				list.addAll(entry.getValue().getCards());
				HandValue handValue = handChecker.check(list);
				if(winningOrder.containsKey(handValue)) {
					winningOrder.get(handValue).add(entry.getValue());
				} else {
					winningOrder.put(handChecker.check(list), Arrays.asList(entry.getValue()));
				}
			}
		});
		
		// Pay out the pot
		while(pot > 0) {
			winningOrder.forEach((handValue, list) -> {
				// Check for All-In players
				list.forEach(action);
			});
		}
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}
	
	public int getPotIndex() {
		return pot.size()-1;
	}

	public void addToPot(int amount) {
		pot.set(pot.size()-1, pot.get(pot.size()-1)+amount);
	}
	
	/**
	 * @param playerId Id of player who went All-In
	 */
	public void startSidePot(int playerId) {
		int allInValue = players.get(playerId).getCurrentBet();
		int newSidePot = 0;
		players.entrySet().forEach((entry) -> {
			int playerBet = entry.getValue().getCurrentBet();
			if(!entry.getValue().isFold() && playerBet > allInValue) {
				addToPot(allInValue - playerBet);
				newSidePot += allInValue - playerBet;
			}
		});
		pot.add(newSidePot);
	}
}
