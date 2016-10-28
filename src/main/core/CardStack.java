package main.core;

import handChecker.PokerCard.Color;
import handChecker.PokerCard.Value;

import java.util.ArrayList;
import java.util.Random;

public class CardStack {
	
	private Random r = new Random();
	private ArrayList<Card> cards = new ArrayList<>();
	
	/**
	 * Card Stack for managing cards
	 */
	public CardStack() {
		generateCards();
	}
	
	/**
	 * Add every possible combination of Color and Value
	 */
	private void generateCards() {
		for (Value value : Value.values()) {
			for (Color color : Color.values()) {
				cards.add(new Card(color, value));
			}
		}
	}
	
	/**
	 * Get a random remaining card
	 * @return Random remaining card
	 */
	public Card getCard() {
		int index = r.nextInt(cards.size());
		Card card = cards.get(index);
		cards.remove(index);
		return card;
	}
	
}
