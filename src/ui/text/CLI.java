package ui.text;

import java.util.Arrays;
import java.util.Scanner;

public abstract class CLI implements Runnable {
	
	protected Scanner scanner;
	protected Thread thread;
	
	public CLI() {
		scanner = new Scanner(System.in);
	}
	
	private void parseCommand() {
		String input = scanner.nextLine();
		String[] commandWithArgs = input.split(" ");
		// Get label of command
		String command = commandWithArgs[0];
		// Remove first entry which is the command
		String[] args = Arrays.copyOfRange(commandWithArgs, 1, commandWithArgs.length);
		
		onCommand(command, args);
	}
	
	public void startCLI() {
		thread = new Thread(this);
		thread.start();
	}
	
	abstract void onCommand(String command, String[] args);

	@Override
	public void run() {
		while (true) {
			parseCommand();
		}
	}
	
}
