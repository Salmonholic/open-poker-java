package main.core;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import handChecker.HandChecker;

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
	 * 
	 * @return
	 */
	public HashMap<Integer, ArrayList<Player>> getWinners() {
		HashMap<Integer, ArrayList<Player>> winners = new HashMap<>();
		for (Player player : players) {
			if (player.isFold() && (!player.isAllIn()))
				continue;
			// Get the HandValue
			HandValue playerValue = checkPlayer(player);

			if (winners.isEmpty()) {
				// First iteration
				winners.put(1, getArrayListWithPlayer(player));
				continue;
			}

			for (int i = 1; i <= winners.size(); i++) {
				boolean cancel = false;
				switch (playerValue.compareTo(checkPlayer(winners.get(i)
						.iterator().next()))) {
				case 1:
					// Player has more

					// Put all players behind the player 1 lower
					for (int j = winners.size() + 1; j > i; j++) {
						winners.put(j + 1, winners.get(j));
						winners.remove(j);
					}

					// Put the player into the i'th place
					winners.put(i, getArrayListWithPlayer(player));

					cancel = true;
					break;
				case 0:
					// Player has equal

					// Put the player into the i'th place
					winners.get(i).add(player);

					cancel = true;
					break;
				case -1:
					if (i == winners.size()) {
						winners.put(i + 1, getArrayListWithPlayer(player));
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
		return winners;
	}

	public void showDown() {

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
