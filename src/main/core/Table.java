package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Table {

	Player[] players;
	CardStack cardStack;
	Card[] cards;
	int buttonId = 0;
	int smallBlindId = 1;
	int bigBlindId = 1;
	int currentBet = 0;
	int pot = 0;
	int smallBlind = 5;

	HandChecker handChecker = new HandChecker();

	/**
	 * Poker table
	 * 
	 * @param players
	 *            Players
	 */
	public Table(Player[] players) {
		this.players = players;
		// Loop for table
		while (true) {
			// Give the button around
			buttonId++;
			if (buttonId > players.length) {
				buttonId = 0;
			}
			// Get the blinds from all players
			blinds();
			// Give 2 Cards to every player
			giveCards();
			waitForBets();
			// Flop
			flop();
			waitForBets();
			// Turn
			turn();
			waitForBets();
			// River
			river();
			waitForBets();
			// ShowDown
			showDown();
			reset();
		}
	}

	public void reset() {
		for (int i = 0; i < players.length; i++) {
			if (players[i].getMoney() < 0) {
				removePlayer(i);
			} else {
				players[i].reset();
			}

		}
	}

	/**
	 * Adds a card to cards
	 * 
	 * @param card
	 *            Card to add
	 */
	public void addCard(Card card) {
		Card[] newCards = new Card[cards.length + 1];
		for (int i = 0; i < players.length; i++) {
			newCards[i] = cards[i];
		}
		newCards[newCards.length] = card;
		setCards(newCards);
	}

	/**
	 * Adds 3 Cards
	 */
	public void flop() {
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
		addCard(cardStack.getCard());
	}

	/**
	 * Adds 1 Card
	 */
	public void turn() {
		addCard(cardStack.getCard());
	}

	/**
	 * Adds 1 Card
	 */
	public void river() {
		addCard(cardStack.getCard());
	}

	/**
	 * Converts a Array of Cards to an List<Card>
	 * 
	 * @param cards
	 *            Card Array to convert
	 * @return Converted cards
	 */
	public List<PokerCard> asList(Card[] cards) {
		ArrayList<PokerCard> newCards = new ArrayList<>();
		for (PokerCard card : cards) {
			newCards.add(card);
		}
		return newCards;
	}

	public HandValue checkPlayer(Player player) {
		return handChecker.check(asList(player.getCards()));
	}

	/**
	 * Generate an ArrayList with just the player in it
	 * 
	 * @param player
	 *            Player to be in the Array list
	 * @return ArrayList with just the player in it
	 */
	public ArrayList<Player> getArrayListWithPlayer(Player player) {
		ArrayList<Player> list = new ArrayList<>();
		list.add(player);
		return list;
	}

	/**
	 * Generate an HashMap<Integer, ArrayList<Player>> displaying every place
	 * and it's players
	 * 
	 * @return HashMap<Integer, ArrayList<Player>>
	 */
	public HashMap<Integer, ArrayList<Player>> getPlaces() {
		HashMap<Integer, ArrayList<Player>> places = new HashMap<>();
		for (Player player : players) {
			if (player.isFold() && (!player.isAllIn()))
				continue;
			// Get the HandValue
			HandValue playerValue = checkPlayer(player);

			if (places.isEmpty()) {
				// First iteration
				places.put(1, getArrayListWithPlayer(player));
				continue;
			}

			for (int i = 1; i <= places.size(); i++) {
				boolean cancel = false;
				switch (playerValue.compareTo(checkPlayer(places.get(i).iterator().next()))) {
				case 1:
					// Player has more

					// Put all players behind the player 1 lower
					for (int j = places.size() + 1; j > i; j++) {
						places.put(j + 1, places.get(j));
						places.remove(j);
					}

					// Put the player into the i'th place
					places.put(i, getArrayListWithPlayer(player));

					cancel = true;
					break;
				case 0:
					// Player has equal

					// Put the player into the i'th place
					places.get(i).add(player);

					cancel = true;
					break;
				case -1:
					if (i == places.size()) {
						places.put(i + 1, getArrayListWithPlayer(player));
						cancel = true;
					} else {
						cancel = false;
					}
					break;
				default:
					System.err.println("API error");
					break;
				}
				if (cancel) {
					break;
				}
			}
		}
		return places;
	}

	/**
	 * Splits the given value between players
	 * @param value Amount to split
	 * @param players Players
	 */
	public void split(int value, ArrayList<Player> splitPlayers) {
		// TODO Handle all in correctly
		TreeMap<Integer, ArrayList<Player>> sidePots = new TreeMap<>();
		// Put players to their pseudo side pot value
		for (Player player : splitPlayers) {
			if (sidePots.containsKey(player.getCurrentBet())) {
				sidePots.get(player.getCurrentBet()).add(player);
			} else {
				sidePots.put(player.getCurrentBet(), getArrayListWithPlayer(player));
			}
		}
		// Calculate real side pot value
		for (int sidePot : sidePots.keySet()) {
			// Get least smaller side pot
			int leastSmallerPot = sidePot;
			for (int otherPot : sidePots.keySet()) {
				if (otherPot < leastSmallerPot) {
					leastSmallerPot = otherPot;
				}
			}
			// There is no smaller Pot
			if (leastSmallerPot == sidePot) leastSmallerPot = 0;
			ArrayList<Player> sidePotPlayers = sidePots.get(sidePot);
			// Remove players for old side pot value
			sidePots.remove(sidePot);
			// Put them in real pot
			sidePots.put(leastSmallerPot, sidePotPlayers);
		}
		for (int sidePot : sidePots.keySet()) {
			ArrayList<Player> sidePotPlayers = sidePots.get(sidePot);
			// Give contents of side pot to all remaining players
			for (Player player : splitPlayers) {
				player.addMoney(sidePot / splitPlayers.size());
			}
			// Remove players from future pot splitting
			for (Player player : sidePotPlayers) {
				splitPlayers.remove(player);
			}
		}
	}

	public void showDown() {
		// Get places
		HashMap<Integer, ArrayList<Player>> places = getPlaces();
		for (int i = 1; i <= places.size(); i++) {
			ArrayList<Player> players = places.get(i);
			// Split pot
			split(pot, players);
		}
		// Reset the pot
		pot = 0;
	}

	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	/**
	 * Get the blinds from all players
	 */
	public void blinds() {
		players[bigBlindId].addMoney(smallBlind * -2);
		players[smallBlindId].addMoney(smallBlind * -1);
	}

	/**
	 * Waits until all the players have either set the same bet or have fold
	 */
	public void waitForBets() {

	}

	public void removePlayer(int playerId) {
		Player[] newPlayers = new Player[players.length - 1];
		int newIndex = 0;
		for (int i = 0; i < players.length; i++) {
			if (i != playerId) {
				newPlayers[newIndex] = players[i];
				newIndex++;
			}
		}
		setPlayers(newPlayers);
	}

	/**
	 * Give 2 Cards to each player
	 */
	public void giveCards() {
		cardStack.initCards();
		for (Player player : players) {
			Card[] playerCards = { cardStack.getCard(), cardStack.getCard() };
			player.setCards(playerCards);
		}
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getPot() {
		return pot;
	}

	public void setPot(int pot) {
		this.pot = pot;
	}

	public void addToPot(int amount) {
		setPot(getPot() + amount);
	}
}
