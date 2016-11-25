package tests.core.cardstack;

import java.util.ArrayList;

import main.core.Card;
import main.core.CardStack;

public class TestCardStack extends CardStack {
	
	ArrayList<Card> cards;
	int offset = -1;
	
	public TestCardStack(ArrayList<Card> cards) {
		this.cards = cards;
	}
	
	@Override
	public void initCards() {
	}
	
	@Override
	public Card getCard() {
		offset++;
		return cards.get(offset);
	}
	
}
