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
	public void bigTest() {
		int moneyAftertBlinds = table.getPlayer(1).getMoney();
		System.out.println(moneyAftertBlinds);
		// TODO player only has 85 after blind
		table.action(1, Action.BET, 10);
		assertEquals(90, table.getPlayer(1).getMoney() + (100 - moneyAftertBlinds));
		
	}
	
	@Test
	public void getNextPlayerShouldReturnNextPlayer() {
		assertEquals(1, table.nextPlayer(0));
		assertEquals(0, table.nextPlayer(4));
	}

}
