package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import java.util.TreeMap;

import main.server.TableController;

public class Table {
	private TreeMap<Integer, Player> players;
	private CardStack cardStack;
	private HandChecker handChecker = new HandChecker();
	private ArrayList<Card> cards = new ArrayList<>(5);
	// Pot: Each entry is a sidepot with
	//[value each player has to pay into sidepot, current value of sidepot]
	private ArrayList<int[]> pot = new ArrayList<>(1);
	private GameState gameState = GameState.SHOW_DOWN;

	private TableController tableController;

	private int buttonId = -1;
	private int smallBlindId;
	private int bigBlindId;
	private int smallBlind = 5;

	private int lastBetId = 0;
	private int currentBet;
	private int currentPlayer = 0;
	private int notFoldedOrAllInPlayers;

	private boolean bigBlindMadeDecision;
	private boolean delayNextGameState;

	/**
	 * Poker table
	 * 
	 * @param players
	 *            Amount of players in game
	 * @param money
	 *            Start Money for each player
	 * @param cardStack
	 *            CardStack generating Cards
	 */
	public Table(TableController tableController, int playerAmount, int money,
			CardStack cardStack) {
		if (playerAmount <= 1 || money <= 0)
			throw new IllegalArgumentException();
		// Put players into TreeMap and set Id
		players = new TreeMap<>();
		for (int i = 0; i < playerAmount; i++) {
			players.put(i, new Player(this, i, money));
		}
		this.cardStack = cardStack;
		this.tableController = tableController;
		reset();
		update();
	}

	public Table(TableController tableController, int playerAmount, int money) {
		this(tableController, playerAmount, money, new CardStack());
	}

	public Table(int playerAmount, int money, CardStack cardStack) {
		this(null, playerAmount, money, cardStack);
	}

	public Table(int playerAmount, int money) {
		this(null, playerAmount, money, new CardStack());
	}

	public boolean isDelayNextGameState() {
		return delayNextGameState;
	}

	public void setDelayNextGameState(boolean delayNextGameState) {
		this.delayNextGameState = delayNextGameState;
	}

	private void update() {
		if (delayNextGameState) return;

		// check if only one player has not folded
		if (notFoldedOrAllInPlayers <= 1) {
			for (int i=cards.size(); i<5; i++) {
				cards.add(cardStack.getCard());
			}
			showDown();
			reset();
			gameState = GameState.PRE_FLOP;
			preFlop();
		}

		// game state transition
		if ((gameState != GameState.PRE_FLOP && currentPlayer == lastBetId)
				|| (gameState == GameState.PRE_FLOP && bigBlindMadeDecision && currentPlayer == lastBetId)) {
			// if (currentPlayer == lastBetId) {
			gameState = GameState.values()[(gameState.ordinal() + 1)
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
		if (notFoldedOrAllInPlayers == 0) {
			return playerId;
		}
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
		if (playerId == currentPlayer) {
			delayNextGameState = false;

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
				notFoldedOrAllInPlayers--;
				break;
			case RAISE:
				if (currentBet == 0
						|| player.getMoney() < (amount + currentBet - player
								.getCurrentBet()))
					throw new IllegalArgumentException();
				player.raise(amount);
				break;
			default:
				break;
			}

			if (gameState == GameState.PRE_FLOP
					&& bigBlindMadeDecision == false
					&& currentPlayer == bigBlindId) {
				bigBlindMadeDecision = true;
			}

			currentPlayer = nextPlayer(playerId);
			update();
			resend();
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
		buttonId = nextPlayer(buttonId);
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
		bigBlindMadeDecision = false;
		delayNextGameState = false;
		lastBetId = nextPlayer(bigBlindId);
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
		TreeMap<HandValue, ArrayList<Player>> winningOrder = new TreeMap<>(Collections.reverseOrder());
		// Sort out players who have fold
		for (Entry<Integer, Player> entry : players.entrySet()) {
			if (!entry.getValue().isFold()) {
				List<PokerCard> list = new ArrayList<PokerCard>(cards);
				list.addAll(entry.getValue().getCards());
				HandValue handValue = handChecker.check(list);
				if (winningOrder.containsKey(handValue)) {
					winningOrder.get(handValue).add(entry.getValue());
				} else {
					winningOrder.put(
							handValue,
							new ArrayList<Player>(Arrays.asList(entry
									.getValue())));
				}
			}
		}

		// Pay out the pot
		Iterator<ArrayList<Player>> winnersLists = winningOrder.values()
				.iterator();
		while ((getPotValue() != 0) && winnersLists.hasNext()) {
			List<Player> winners = winnersLists.next();
			while (!winners.isEmpty()) {
				// Get side pot in which all remaining winners are involved
				int maxsidepot = pot.size() - 1;
				for (Player player : winners) {
					if (player.getLastPot() == -1) {
						player.setLastPot(pot.size() - 1);
					} else if (player.getLastPot() < maxsidepot)
						maxsidepot = player.getLastPot();
				}
				// Sum up all lower side pots
				int sidepot = 0;
				for (int i = 0; i <= maxsidepot; i++) {
					sidepot += pot.get(i)[1];
					pot.get(i)[1] = 0;
				}
				// Pay out money to each winner
				int profit = sidepot / (winners.size()); // Casino gets rest of
															// splitpot
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
		// Remove players
		Iterator<Player> playerIterator = players.values().iterator();
		while(playerIterator.hasNext()) {
			Player player = playerIterator.next();
			if (player.getMoney() == 0) {
				playerIterator.remove(); //TODO check if working
				tableController.removePlayer(player.getId());
			} else
				player.reset();
		}
		// Reset card stack
		cardStack.initCards();
		// Reset pot and cards
		cards.clear();
		pot.clear();
		pot.add(new int[] {0, 0});
		currentBet = 0;
		// Reset number of folded players
		notFoldedOrAllInPlayers = players.size();
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
		pot.get(pot.size() - 1)[0] += currentBet - this.currentBet;
		this.currentBet = currentBet;
	}

	public int getPotIndex() {
		return pot.size() - 1;
	}

	public void addToPot(int totalBet, int amount) {
		int sidepotSum = 0;
		for (int[] sidepot : pot) {
			sidepotSum += sidepot[0];
			if (sidepotSum > totalBet) {
				int amountForSidepot = sidepotSum - totalBet;
				if (amountForSidepot < amount) {
					sidepot[1] += amountForSidepot;
					amount -= amountForSidepot;
				} else {
					sidepot[1] += amount;
					amount = 0;
					break;
				}
			}
		}
		pot.get(pot.size() - 1)[1] += amount;
	}
	
	public int getPotValue() {
		int potValue = 0;
		for (int[] sidepot : pot) {
			potValue += sidepot[1];
		}
		return potValue;
	}

	/**
	 * @param allInBet
	 *            totalBet of player, who went all-In
	 */
	public void startSidePot(int playerId) {
		int allInBet = players.get(playerId).getTotalBet();
		int sidePotPayInSum = 0;
		for (int i=0; i<pot.size(); i++) {
			sidePotPayInSum += pot.get(i)[0];
			if (sidePotPayInSum > allInBet) {
				// split this sidePot into 2
				int[] newSidePot = {sidePotPayInSum - allInBet, 0};
				int[] oldSidePot = pot.get(i);
				oldSidePot[0] -= newSidePot[0];
				for (Player player : players.values()) {
					if (player.getId() != playerId) {
						int playerTotalBet = player.getTotalBet();
						if (playerTotalBet > allInBet) {
							if (playerTotalBet >= sidePotPayInSum) {
								oldSidePot[1] -= newSidePot[0];
								newSidePot[1] += newSidePot[0];
							} else {
								oldSidePot[1] -= playerTotalBet - allInBet;
								newSidePot[1] += playerTotalBet - allInBet;
							}
						}
					}
				}
				pot.add(i+1, newSidePot);
				// fix player.lastPot for every player
				for (Player player : players.values()) {
					if (player.getId() != playerId && player.getLastPot() >= i)
						player.setLastPot(player.getLastPot() + 1);
				}
				return;
			}
		}
		// add void sidePot at the end
		pot.add(pot.size(), new int[] {0, 0});
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

	public ArrayList<int[]> getPot() {
		return pot;
	}
	
	public void oneMoreFoldOrAllInPlayer() {
		notFoldedOrAllInPlayers--;
	}
	
	public TreeMap<Integer, Player> getPlayers() {
		return players;
	}
	
	/**
	 * Notify tableController to resend data if it exists
	 */
	public void resend() {
		if (tableController != null) {
			tableController.resend();
		}
	}
	
	public void removePlayer(int id) {
		Player player = players.get(id);
			if(player != null) {
			if (currentPlayer == id)
				currentPlayer = nextPlayer(currentPlayer);
			if (!(player.isAllIn()||player.isFold()))
				notFoldedOrAllInPlayers--;
			if (bigBlindId == id)
				bigBlindMadeDecision = true;
			delayNextGameState = false;
			players.remove(id);
		}
	}
}
