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
	HandChecker handChecker = new HandChecker();
	ArrayList<Card> cards = new ArrayList<>(5);
	private ArrayList<Integer> pot = new ArrayList<>(1);
	GameState gameState = GameState.SHOW_DOWN;

	int buttonId = -1;
	int smallBlindId;
	int bigBlindId;
	int smallBlind = 5;
	
	int lastBetId = 0;
	int currentBet;
	int currentPlayer = 0;
	int notFoldedPlayers;


	/**
	 * Poker table
	 * 
	 * @param players
	 *            Amount of players in game
	 * @param money
	 *            Start Money for each player
	 */
	public Table(int playerAmount, int money) {
		if (playerAmount <= 1 || money <= 0)
			throw new IllegalArgumentException();
		// Put players into TreeMap and set Id
		players = new TreeMap<>();
		for (int i = 0; i < playerAmount; i++) {
			players.put(i, new Player(this, i, money));
		}
		cardStack = new CardStack();
		reset();
		update();
	}

	private void update() {
		// TODO console output
		
		// check if only one player has not folded
		if (notFoldedPlayers == 1) {
			showDown();
			reset();
			gameState = GameState.PRE_FLOP;
			preFlop();
		}
		
		// game state transition
		if (currentPlayer == lastBetId) {
			gameState = GameState.values()[(gameState.ordinal()+1)
			                               % GameState.values().length];
			resetCurrentBet();
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
				preFlop();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Get next player in players, who has not folded yet
	 * 
	 * @param playerId
	 *            Player
	 */
	public int nextPlayer(int playerId) {
		// Integer because of null check below
		Integer nextId = playerId;
		do {
			nextId = players.ceilingKey(nextId + 1);
			if (nextId == null) {
				nextId = players.firstKey();
			}
		} while (players.get(nextId).isFold() || players.get(nextId).isAllIn());
		return nextId;
	}

	public void action(int playerId, Action action) {
		action(playerId, action, 0);
	}

	public void action(int playerId, Action action, int amount) {
		// TODO console output
		if (playerId == currentPlayer) {
			Player player = players.get(playerId);
			switch (action) {
			case BET:
				if (currentBet != 0 || player.getMoney() < amount)
					throw new IllegalArgumentException();
				player.bet(amount);
				break;
			case CALL:
				if (currentBet == 0 || player.getCurrentBet() == currentBet)
					throw new IllegalArgumentException();
				player.call();
				break;
			case CHECK:
				if (player.getCurrentBet() != currentBet)
					throw new IllegalArgumentException();
				player.check();
				break;
			case FOLD:
				player.fold();
				notFoldedPlayers--;
				break;
			case RAISE:
				if (currentBet == 0 ||
					player.getMoney() < (amount + currentBet - player.getCurrentBet()))
					throw new IllegalArgumentException();
				player.raise(amount);
				break;
			default:
				break;
			}
			currentPlayer = nextPlayer(playerId);
			update();
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
		players.get(smallBlindId).bet(smallBlind);
		players.get(bigBlindId).raise(smallBlind);
	}

	private void preFlop() {
		buttonId = nextPlayer(buttonId); // works, because no one could have folded yet
		if (players.size() == 2) {
			smallBlindId = buttonId;
			bigBlindId = nextPlayer(buttonId);
		} else {
			smallBlindId = nextPlayer(buttonId);
			bigBlindId = nextPlayer(smallBlindId);
		}
		// Give 2 cards to each player
		giveCards();
		// Get blinds from players
		blinds();
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
		TreeMap<HandValue, ArrayList<Player>> winningOrder = new TreeMap<>();
		// Sort out players who have fold
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (!entry.getValue().isFold()) {
				List<PokerCard> list = new ArrayList<PokerCard>(cards);
				list.addAll(entry.getValue().getCards());
				HandValue handValue = handChecker.check(list);
				if (winningOrder.containsKey(handValue)) {
					winningOrder.get(handValue).add(entry.getValue());
				} else {
					winningOrder.put(handValue, new ArrayList<Player>(
							Arrays.asList(entry.getValue())));
				}
			}
		}

		// Pay out the pot
		Iterator<ArrayList<Player>> winnersLists = winningOrder.values().iterator();
		while ((pot.get(pot.size()-1) != 0) && winnersLists.hasNext()) {
			List<Player> winners = winnersLists.next();
			while (!winners.isEmpty()) {
				// Get side pot in which all remaining winners are involved
				int maxsidepot = pot.size() - 1;
				for (Player player : winners) {
					if (player.getLastPot() == -1) {
						player.setLastPot(pot.size()-1);
					} else if (player.getLastPot() < maxsidepot)
						maxsidepot = player.getLastPot();
				}
				// Sum up all lower side pots
				int sidepot = 0;
				for (int i=0; i <= maxsidepot; i++) {
					sidepot += pot.get(i);
					pot.set(i, 0);
				}
				// Pay out money to each winner
				int profit = sidepot / (winners.size()); // Casino gets rest of splitpot
				for (Player player : winners) {
					player.addMoney(profit);
				}
				// Remove all winners who don't participate in higher side pots
				Iterator<Player> winnersIterator = winners.iterator();
				while (winnersIterator.hasNext()) {
					Player player = winnersIterator.next();
					if (player.getLastPot() == maxsidepot)
						winnersIterator.remove();
				}
			}
		}
		// Last winners payed but pot still not empty (stupid players :D)
		// Casino gets rest of pot
	}

	/**
	 * Resets player flags and (side) pots. Has to be called each new round.
	 */
	private void reset() {
		// Reset players
		for (Map.Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue().getMoney() < 0) {
				players.remove(entry.getKey());
			} else {
				entry.getValue().reset();
			}
		}
		// Reset card stack
		cardStack.initCards();
		// Reset pot and cards
		cards.clear();
		pot.clear();
		pot.add(0);
		// Reset number of folded players
		notFoldedPlayers = players.size();
	}
	
	private void resetCurrentBet() {
		currentBet = 0;
		for (Player player : players.values()) {
			player.resetCurrentBet();
		}
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
		for (Player player : players.values()) {
			int playerBet = player.getCurrentBet();
			if (playerBet > allInValue) {
				addToPot(allInValue - playerBet);
				newSidePot += allInValue - playerBet;
			}
		}
		pot.add(newSidePot);
	}

	public Player getPlayer(int playerId) {
		return players.get(playerId);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void setSmallBlind(int smallBlind) {
		this.smallBlind = smallBlind;
	}

	public int getSmallBlind() {
		return smallBlind;
	}

	public GameState getGameState() {
		return gameState;
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
	
	public void setLastBetId(int id) {
		lastBetId = id;
	}

	public int getLastBetId() {
		return lastBetId;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ArrayList<Integer> getPot() {
		return pot;
	}
}
