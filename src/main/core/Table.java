package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Table {

	TreeMap<Integer, Player> players;
	CardStack cardStack;
	ArrayList<Card> cards;
	private ArrayList<Integer> pot = new ArrayList<>(1);
	int smallBlind = 5;
	int currentBet;
	GameState gameState = GameState.PRE_FLOP;
	boolean firstRound = true;

	int buttonId = 0;
	int smallBlindId;
	int bigBlindId;
	int lastBetId;
	int currentPlayer;

	HandChecker handChecker = new HandChecker();

	/**
	 * Poker table
	 * 
	 * @param players
	 *            Amount of players in game
	 * @param money
	 *            Start Money for each player
	 */
	public Table(int playerAmount, int money) {
		if (playerAmount <= 0 || money <= 0)
			throw new IllegalArgumentException();
		// Put players into TreeMap and set Id
		players = new TreeMap<>();
		cardStack = new CardStack();
		for (int i = 0; i < playerAmount; i++) {
			players.put(i, new Player(this, money));
		}
		update();
	}

	private void update() {
		if (firstRound || currentPlayer == lastBetId) {
			if (!firstRound) {
				gameState = GameState.values()[gameState.ordinal()
						% GameState.values().length];
			}
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
				showDown();
				reset();
				gameState = GameState.PRE_FLOP;
				break;
			default:
				break;
			}
			firstRound = false;
		}
	}

	/**
	 * Get next player in players, or first player if no more player is in
	 * players
	 * 
	 * @param playerId
	 *            Player
	 */
	public int nextPlayer(int playerId) {
		// Integer because of null check below
		Integer nextId = players.ceilingKey(playerId + 1);
		System.out.println(nextId);
		if (nextId == null) {
			System.out.println("wasss");
			return players.firstKey();
		} else {
			return nextId;
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
			ArrayList<Card> playerCards = new ArrayList<>();
			playerCards.add(cardStack.getCard());
			playerCards.add(cardStack.getCard());
			player.setCards(playerCards);
		}
	}

	/**
	 * Get the blinds from all players
	 */
	private void blinds() {
		System.out.println(bigBlindId);
		System.out.println(smallBlindId);
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
		// Reset the CardSTack
		cardStack.initCards();
		// Start first pot
		pot.add(0);
		// Give 2 cards to players
		giveCards();
		// Get blinds from players
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

	private void showDown() {
		TreeMap<HandValue, List<Player>> winningOrder = new TreeMap<>();
		// Sort out players who have fold
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (!entry.getValue().isFold()) {
				List<PokerCard> list = new ArrayList<PokerCard>(cards);
				list.addAll(entry.getValue().getCards());
				HandValue handValue = handChecker.check(list);
				if (winningOrder.containsKey(handValue)) {
					winningOrder.get(handValue).add(entry.getValue());
				} else {
					winningOrder.put(handChecker.check(list),
							Arrays.asList(entry.getValue()));
				}
			}
		}

		// Pay out the pot
		while (pot.get(pot.size() - 1) > 0) {
			Iterator<List<Player>> playerLists = winningOrder.values()
					.iterator();
			while (playerLists.hasNext()) {
				List<Player> playerList = playerLists.next();
				for (int i = 0; i < pot.size(); i++) {
					ArrayList<Player> involvedPlayers = new ArrayList<>();
					for (Player player : playerList) {
						if (player.getLastPot() >= i
								|| player.getLastPot() == -1) {
							involvedPlayers.add(player);
						}
					}
					for (Player player : involvedPlayers) {
						player.addMoney(pot.get(i) / involvedPlayers.size());
					}
					// TODO rest des splitpots
				}
			}
		}
	}

	/**
	 * Resets player flags and (side) pots. Has to be called each new round.
	 */
	private void reset() {
		for (Map.Entry<Integer, Player> entry : players.entrySet()) {
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

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public int getPotIndex() {
		return pot.size() - 1;
	}

	public void addToPot(int amount) {
		pot.set(pot.size() - 1, pot.get(pot.size() - 1) + amount);
	}

	/**
	 * @param playerId
	 *            Id of player who went All-In
	 */
	public void startSidePot(int playerId) {
		int allInValue = players.get(playerId).getCurrentBet();
		int newSidePot = 0;
		for (Entry<Integer, Player> entry : players.entrySet()) {
			int playerBet = entry.getValue().getCurrentBet();
			if (!entry.getValue().isFold() && playerBet > allInValue) {
				addToPot(allInValue - playerBet);
				newSidePot += allInValue - playerBet;
			}
		}
		pot.add(newSidePot);
	}

	public Player getPlayer(int playerId) {
		return players.get(playerId);
	}
}
