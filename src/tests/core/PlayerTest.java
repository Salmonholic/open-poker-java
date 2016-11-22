package tests.core;

import static org.junit.Assert.*;

import main.core.Action;
import main.core.Player;
import main.core.Table;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {
	
	Table table;
	Player player;
	
	@Before
	public void setUp() throws Exception {
		table = new Table(1, 100);
		player = table.getPlayer(0);
	}
	
	@Test
	public void playerShouldHandleAllInCorrecly() {
		table.action(0, Action.BET, player.getMoney());
		assertTrue("Player goes all in correctly", player.isAllIn());
	}

}
