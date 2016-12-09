package main.main;

import main.connection.Client;

public class Main {

	/**
	 * Start program
	 * 
	 * @param args
	 *            Arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Client("127.0.0.1", 10101, "user", 0);
	}

}
