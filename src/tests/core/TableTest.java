package tests.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import main.core.Action;
import main.core.GameState;
import main.core.Table;

import org.junit.Before;
import org.junit.Test;

public class TableTest {

	Table exceptionTable;
	Table exceptionTableFlop;

	@Before
	public void setUp() throws Exception {
		exceptionTable = new Table(3, 100);
		exceptionTableFlop = new Table(3, 100);
		exceptionTableFlop.action(0, Action.CALL);
		exceptionTableFlop.action(1, Action.CALL);
		exceptionTableFlop.action(2, Action.CHECK);
	}

	@Test
	public void bigTest() {
		Table table = new Table(3, 100);

		// Check IDs
		assertEquals("ButtonId", 0, table.getButtonId());
		assertEquals("SmallBlindId", 1, table.getSmallBlindId());
		assertEquals("BigBlindId", 2, table.getBigBlindId());
		assertEquals("CurrentPlayer", 0, table.getCurrentPlayer());
		assertEquals("LastBetId", 2, table.getLastBetId());

		// Check money
		assertEquals("CurrentBet", 10, table.getCurrentBet());
		assertEquals("Pot", 15, table.getPot().get(0).intValue());
		assertEquals("Dealer", 100, table.getPlayer(0).getMoney());
		assertEquals("SmallBlind", 95, table.getPlayer(1).getMoney());
		assertEquals("BigBlind", 90, table.getPlayer(2).getMoney());

		// Finish preflop
		table.action(0, Action.CALL);
		table.action(1, Action.CALL);
		table.action(2, Action.CHECK);
		assertEquals(GameState.FLOP, table.getGameState());

		// Check money
		assertEquals("CurrentBet", 0, table.getCurrentBet());
		assertEquals("Pot", 30, table.getPot().get(0).intValue());
		assertEquals("Player 0 money", 90, table.getPlayer(0).getMoney());
		assertEquals("Player 1 money", 90, table.getPlayer(1).getMoney());
		assertEquals("Player 2 money", 90, table.getPlayer(2).getMoney());

		// Finish flop
		table.action(0, Action.CHECK);
		table.action(1, Action.BET, 15);
		assertEquals("CurrentBet", 15, table.getCurrentBet());
		table.action(2, Action.CALL);
		table.action(0, Action.CALL);
		assertEquals(GameState.TURN, table.getGameState());

		// Check money
		assertEquals("Pot", 75, table.getPot().get(0).intValue());
		assertEquals("Player 0 money", 75, table.getPlayer(0).getMoney());
		assertEquals("Player 1 money", 75, table.getPlayer(1).getMoney());
		assertEquals("Player 2 money", 75, table.getPlayer(2).getMoney());

		// Finish turn
		table.action(1, Action.BET, 10);
		assertEquals("CurrentBet", 10, table.getCurrentBet());
		table.action(2, Action.RAISE, 10);
		assertEquals("CurrentBet", 20, table.getCurrentBet());
		table.action(0, Action.FOLD);
		table.action(1, Action.CALL);
		assertEquals(GameState.RIVER, table.getGameState());

		// Check money
		assertEquals("Pot", 115, table.getPot().get(0).intValue());
		assertEquals("Player 0 money", 75, table.getPlayer(0).getMoney());
		assertEquals("Player 1 money", 55, table.getPlayer(1).getMoney());
		assertEquals("Player 2 money", 55, table.getPlayer(2).getMoney());

		// Finish river
		table.action(2, Action.CHECK);
		table.action(1, Action.CHECK);
		assertEquals(GameState.PRE_FLOP, table.getGameState());

		// Check money (player 2 is smallblind, player 0 is bigblind)
		assertEquals("New ButtonId", 1, table.getButtonId());
		assertEquals("New SmallBlindId", 2, table.getSmallBlindId());
		assertEquals("New BigBlindId", 0, table.getBigBlindId());
		assertEquals("Pot", 15, table.getPot().get(0).intValue());
		assertEquals("Player 0 money", 65, table.getPlayer(0).getMoney());
		assertThat("Player 1 money", table.getPlayer(1).getMoney(),
				anyOf(is(55), is(170), is(112)));
		assertThat("Player 2 money", table.getPlayer(2).getMoney(),
				anyOf(is(55 - 5), is(170 - 5), is(112 - 5)));

	}

	@Test
	public void getNextPlayerShouldReturnNextPlayer() {
		assertEquals(1, exceptionTable.nextPlayer(0));
		assertEquals(0, exceptionTable.nextPlayer(4));
		assertEquals(2, exceptionTable.nextPlayer(1));
		assertEquals(0, exceptionTable.nextPlayer(-1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongBetActionShouldThrowException() {
		exceptionTable.action(0, Action.BET, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongCheckActionShouldThrowException() {
		exceptionTable.action(0, Action.CHECK);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notEnoughMoneyToRaiseShouldThrowException() {
		exceptionTable.action(0, Action.RAISE, 101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongRaiseActionShouldThrowException() {
		exceptionTableFlop.action(0, Action.RAISE, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongCallActionShouldThrowException() {
		exceptionTableFlop.action(0, Action.CALL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notEnoughMoneyToBetShouldThrowException() {
		exceptionTableFlop.action(0, Action.BET, 101);
	}

}
