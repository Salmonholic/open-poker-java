package tests.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import main.core.Card;
import main.core.CardStack;

import org.junit.Test;

public class CardStackTest {

	@Test
	public void cardStackSouldNeverReturnSameCardTwice() {
		CardStack stack = new CardStack();
		ArrayList<Card> cards = new ArrayList<>();
		Card card;
		// Get cards until there are no more
		while ((card = stack.getCard()) != null) {
			for (Card oldCard : cards) {
				if (oldCard.getValue().equals(card.getValue())
						&& oldCard.getColor().equals(card.getColor())) {
					fail("CardStack returned same card twice");
					return;
				}
			}
		}
	}

}
