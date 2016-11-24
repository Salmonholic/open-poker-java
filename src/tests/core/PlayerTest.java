package tests.core;

import static org.junit.Assert.*;

import main.core.Action;
import main.core.Player;
import main.core.Table;
import main.exception.NotEnoughMoneyException;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	
	Table table;
	Player player;
	
	@Before
	public void setUp() throws Exception {
		table = new Table(2, 100);
		player = table.getPlayer(0);
	}
	
	@Test
	public void playerShouldHandleAllInCorrecly() throws NotEnoughMoneyException {
		table.action(0, Action.BET, player.getMoney());
		assertTrue("Player sets All-In flag", player.isAllIn());
	}

}
