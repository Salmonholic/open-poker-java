package tests.core;

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

}
