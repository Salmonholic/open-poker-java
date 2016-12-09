package tests.core;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import handChecker.PokerCard.Color;
import handChecker.PokerCard.Value;

import java.util.ArrayList;

import main.core.Action;
import main.core.Card;
import main.core.GameState;
import main.core.Player;
import main.core.Table;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import tests.core.cardstack.TestCardStack;

public class TableTest {

	Table exceptionTablePreflop;
	Table exceptionTableFlop;

	@Before
	public void setUp() throws Exception {
		exceptionTablePreflop = new Table(3, 100);
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
		assertEquals("LastBetId", 0, table.getLastBetId());

		// Check money
		assertEquals("CurrentBet", 10, table.getCurrentBet());
		assertEquals("Pot", 15, table.getPot().get(0).intValue());
		assertEquals("Dealer", 100, table.getPlayer(0).getMoney());
		assertEquals("SmallBlind", 95, table.getPlayer(1).getMoney());
		assertEquals("BigBlind", 90, table.getPlayer(2).getMoney());

		// Finish preflop
		table.action(0, Action.CALL);
		table.action(1, Action.CALL);
		assertEquals(GameState.PRE_FLOP, table.getGameState());
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
	
	@Ignore
	@Test
	public void allInShouldWork() {
		Table table = new Table(3, 100);
		Player player0 = table.getPlayer(0);
		Player player1 = table.getPlayer(1);

		// Check IDs
		assertEquals("SmallBlindId", 1, table.getSmallBlindId());
		assertEquals("BigBlindId", 2, table.getBigBlindId());
		
		// Player 0 should go All-In due to CALL and therefore gets his money reduced to 10
		player0.setMoney(10);
		// Player 1 should go All-In due to Raise and therefore gets his money reduced to 15
		player1.setMoney(10);
		
		// Preflop
		table.action(0, Action.CALL);
		assertTrue("Player 0 goes All-In", player0.isAllIn());
		assertEquals("Player 0 should have no money left", 0, player0.getMoney());
		table.action(1, Action.RAISE, 5);
		assertTrue("Player 1 goes All-In", player1.isAllIn());
		assertEquals("Player 1 should have no money left", 0, player1.getMoney());
		table.action(2, Action.CALL);
		assertEquals(GameState.PRE_FLOP, table.getGameState());
		
		/*System.out.println("Player 0 money: "+player0.getMoney());
		System.out.println("Player 1 money: "+player1.getMoney());
		System.out.println("Player 2 money: "+table.getPlayer(2).getMoney());*/
	}
	
	@Test
	public void sidePotShouldWork() {
		ArrayList<Card> cards = new ArrayList<>();
		// Player 1
		cards.add(new Card(Color.CLUBS, Value.SEVEN));
		cards.add(new Card(Color.CLUBS, Value.KING));
		// Player 2
		cards.add(new Card(Color.CLUBS, Value.SEVEN));
		cards.add(new Card(Color.CLUBS, Value.EIGHT));
		// Player 3
		cards.add(new Card(Color.CLUBS, Value.ASS));
		cards.add(new Card(Color.DIAMONDS, Value.ASS));
		// Table
		cards.add(new Card(Color.HEARTS, Value.KING));
		cards.add(new Card(Color.DIAMONDS, Value.KING));
		cards.add(new Card(Color.SPADES, Value.JACK));
		cards.add(new Card(Color.HEARTS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));

		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		Table table = new Table(3, 100, new TestCardStack(cards));
		Player player0 = table.getPlayer(0);
		Player player1 = table.getPlayer(1);
		Player player2 = table.getPlayer(2);
		System.out.println(player0.getMoney());
		player0.setMoney(10);
		player1.setMoney(200);
		player2.setMoney(50);
		
		table.action(0, Action.CALL);
		assertTrue("Player 0 goes All-In", player0.isAllIn());
		table.action(1, Action.RAISE, 20);
		assertTrue(player1.isAllIn() == false);
		table.action(2, Action.RAISE, 30);
		assertTrue(player2.isAllIn());
		
		table.action(1, Action.CALL);
		assertEquals(GameState.FLOP, table.getGameState());
		table.action(1, Action.CHECK);
		assertEquals(GameState.TURN, table.getGameState());
		table.action(1, Action.CHECK);
		assertEquals(GameState.RIVER, table.getGameState());
		table.action(1, Action.CHECK);
		assertEquals(GameState.PRE_FLOP, table.getGameState());
		
		assertEquals(15, player0.getMoney());
		System.out.println(player0.getMoney());
		System.out.println(player1.getMoney());
		System.out.println(player2.getMoney());
		
	}
	
	public static boolean cardArrayContainsCards(ArrayList<Card> cards, ArrayList<Card> contained) {
		for (Card card : contained) {
			boolean contains = false;
			for (Card container : cards) {
				if (container.getValue().equals(card.getValue())) {
					contains = true;
					break;
				}
			}
			if (!contains) return false;
		}
		return true;
	}
	
	@Test
	public void testCardStackShouldGiveCards() {
		ArrayList<Card> cards = new ArrayList<>();
		// Player 1
		cards.add(new Card(Color.CLUBS, Value.SEVEN));
		cards.add(new Card(Color.CLUBS, Value.EIGHT));
		// Player 2
		cards.add(new Card(Color.CLUBS, Value.NINE));
		cards.add(new Card(Color.CLUBS, Value.TEN));
		// Player 3
		cards.add(new Card(Color.CLUBS, Value.TEN));
		cards.add(new Card(Color.CLUBS, Value.JACK));
		// Table
		cards.add(new Card(Color.CLUBS, Value.QUEEN));
		cards.add(new Card(Color.CLUBS, Value.KING));
		cards.add(new Card(Color.DIAMONDS, Value.ASS));
		cards.add(new Card(Color.DIAMONDS, Value.SEVEN));
		cards.add(new Card(Color.DIAMONDS, Value.NINE));
		Table table = new Table(3, 100, new TestCardStack(cards));
		
		ArrayList<Card> player1Cards = new ArrayList<>();
		player1Cards.add(new Card(Color.CLUBS, Value.SEVEN));
		player1Cards.add(new Card(Color.CLUBS, Value.EIGHT));
		assertTrue(cardArrayContainsCards(table.getPlayer(0).getCards(), player1Cards));
		
		ArrayList<Card> table1Cards = new ArrayList<>();
		cards.add(new Card(Color.CLUBS, Value.QUEEN));
		cards.add(new Card(Color.CLUBS, Value.KING));
		cards.add(new Card(Color.DIAMONDS, Value.ASS));
		assertTrue(cardArrayContainsCards(table.getCards(), table1Cards));
	}
	
	@Test
	public void blindsShouldBeCorrectOn2PlayerTable() {
		Table table = new Table(2, 100);
		
		assertEquals("ButtonId", 0, table.getButtonId());
		assertEquals("SmallBlindId", 0, table.getSmallBlindId());
		assertEquals("BigBlindId", 1, table.getBigBlindId());
		assertEquals("CurrentPlayer", 0, table.getCurrentPlayer());
		
		assertEquals("Player 0 money", 95, table.getPlayer(0).getMoney());
		assertEquals("Player 1 money", 90, table.getPlayer(1).getMoney());
		
	}

	@Test
	public void getNextPlayerShouldReturnNextPlayer() {
		assertEquals(1, exceptionTablePreflop.nextPlayer(0));
		assertEquals(0, exceptionTablePreflop.nextPlayer(4));
		assertEquals(2, exceptionTablePreflop.nextPlayer(1));
		assertEquals(0, exceptionTablePreflop.nextPlayer(-1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongBetActionShouldThrowException() {
		exceptionTablePreflop.action(0, Action.BET, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void wrongCheckActionShouldThrowException() {
		exceptionTablePreflop.action(0, Action.CHECK);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notEnoughMoneyToRaiseShouldThrowException() {
		exceptionTablePreflop.action(0, Action.RAISE, 101);
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
