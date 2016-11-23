package tests.core;

import static org.junit.Assert.*;
import main.core.Action;
import main.core.Table;

import org.junit.Before;
import org.junit.Test;

public class TableTest {

	Table table;

	@Before
	public void setUp() throws Exception {
		// Create Table with 3 Players and a start money of 100
		table = new Table(3, 100);
	}

	@Test
	public void cameShouldWorkCorrectly() {
		// Check IDs
		assertEquals("ButtonId", 0, table.getButtonId());
		assertEquals("SmallBlindId", 1, table.getSmallBlindId());
		assertEquals("BigBlindId", 2, table.getBigBlindId());
		assertEquals("CurrentPlayer", 0, table.getCurrentPlayer());

		// Check money
		assertEquals("Dealer calculates correctly", 100, table.getPlayer(0)
				.getMoney());
		assertEquals("SmallBlind calculates correctly", 95, table.getPlayer(1)
				.getMoney());
		assertEquals("BigBlind calculates correctly", 90, table.getPlayer(2)
				.getMoney());

		// Check BET action
		table.action(0, Action.BET, 10);
		assertEquals("BET Action works correctly", 90, table.getPlayer(0)
				.getMoney());
		table.action(1, Action.CHECK);
		assertEquals("CHECK Action works correctly", 85, table.getPlayer(1)
				.getMoney());
	}

	@Test
	public void getNextPlayerShouldReturnNextPlayer() {
		assertEquals(1, table.nextPlayer(0));
		assertEquals(0, table.nextPlayer(4));
		assertEquals(2, table.nextPlayer(1));
	}

}
