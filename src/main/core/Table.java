package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Table {

	private TreeMap<Integer, Player> players;
	private CardStack cardStack;
	private ArrayList<Card> cards;
	private int buttonId = 0;
	private int smallBlindId = 1;
	private int bigBlindId = 1;
	private int smallBlind = 5;
	private int currentBet = 0;
	private ArrayList<Integer> pot = new ArrayList<>(1);

	HandChecker handChecker = new HandChecker();

	/**
	 * Poker table
	 * 
	 * @param players
	 *            Players
	 */
	public Table(Player[] players) {
		// Put players into TreeMap and set Id
		for(int i=0; i< players.length; i++) {
			this.players.put(i, players[i]);
			players[i].setId(i);
		}
		// Loop for table
		while (true) {
			cardStack.initCards();
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
	
	private void waitForBets() {} //Will be removed

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
		/*// Get places
		HashMap<Integer, ArrayList<Player>> places = getPlaces();
		for (int i = 1; i <= places.size(); i++) {
			ArrayList<Player> players = places.get(i);
			// Split pot
			split(pot, players);
		}
		// Reset the pot
		pot = 0;*/
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
	 * @param cards
	 *            Card Array to convert
	 * @return Converted cards
	 */
/*	private List<PokerCard> asList(Card[] cards) {
		ArrayList<PokerCard> newCards = new ArrayList<>();
		for (PokerCard card : cards) {
			newCards.add(card);
		}
		return newCards;
	}*/

/*	private HandValue checkPlayer(Player player) {
		return handChecker.check(asList(player.getCards()));// Funktioniert nicht! check() benötigt auch die Karten auf dem Tisch...
	}*/

	/**
	 * Generate an ArrayList with just the player in it
	 * 
	 * @param player
	 *            Player to be in the Array list
	 * @return ArrayList with just the player in it
	 */
/*	private ArrayList<Player> getArrayListWithPlayer(Player player) {
		ArrayList<Player> list = new ArrayList<>();
		list.add(player);
		return list;
	}*/

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
	
	
	/**
	 * Generate an HashMap<Integer, ArrayList<Player>> displaying every place
	 * and it's players
	 * 
	 * @return HashMap<Integer, ArrayList<Player>>
	 */
/*	private HashMap<Integer, ArrayList<Player>> getPlaces() {
		HashMap<Integer, ArrayList<Player>> places = new HashMap<>();
		for (Player player : players.values()) {
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
	}*/

	/**
	 * Splits the given value between players
	 * @param value Amount to split
	 * @param players Players
	 */
	/*private void split(int value, ArrayList<Player> splitPlayers) {
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
	}*/

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
