package main.core;

import handChecker.PokerCard.Color;
import handChecker.PokerCard.Value;

import java.util.ArrayList;
import java.util.Random;

public class CardStack {
	
	private Random random = new Random();
	private ArrayList<Card> cards = new ArrayList<>();
	
	/**
	 * Card Stack for managing cards
	 */
	public CardStack() {
		initCards();
	}
	
	/**
	 * Add every possible combination of Color and Value
	 */
	public void initCards() {
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
		int index = random.nextInt(cards.size());
		Card card = cards.get(index);
		cards.remove(index);
		return card;
	}
	
}
