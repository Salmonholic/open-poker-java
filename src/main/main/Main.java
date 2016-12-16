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
		Client client1 = new Client("127.0.0.1", 10101, "user1", 0);
		Client client2 = new Client("127.0.0.1", 10101, "user2", 0);
		Client client3 = new Client("127.0.0.1", 10101, "user3", 0);
	}

}
