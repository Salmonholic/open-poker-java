package tests.core;

import static org.junit.Assert.*;
import handChecker.PokerCard.Color;
import handChecker.PokerCard.Value;

import main.core.Card;

import org.junit.Test;

public class CardTest {

	@Test
	public void cardShouldBeComparable() {
		Card card = new Card(Color.DIAMONDS, Value.EIGHT);
		if (!card.getValue().equals(Value.EIGHT)
				|| !card.getColor().equals(Color.DIAMONDS)) {
			fail("Card has incorrect Color or Value");
		}
	}

}
