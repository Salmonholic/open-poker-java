package main.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

public class AuthenticationController {
	
	private static String FILE_PATH = "users";
	private HashMap<String, String> users;
	private HashSet<String> loggedInUsers = new HashSet<>();
	
	public AuthenticationController() {
		try {
			readUsers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void readUsers() throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File(FILE_PATH);
		if (file.exists()) {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			users = (HashMap<String, String>) in.readObject();
			in.close();
		} else {
			users = new HashMap<>();
			save();
		}
		System.out.println("Finished loading of " + users.size() + " users.");
	}
	
	public void save() {
		try {
			File file = new File(FILE_PATH);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(users);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void registerUser(String username, String password) {
		if (username.length() < 3 || password.length() < 8 || users.containsKey(username)) {
			throw new IllegalArgumentException();
		} else {
			users.put(username, password);
			loggedInUsers.add(username);
		}
	}
	
	public boolean validate(String username, String password) {
		if (users.containsKey(username) && !loggedInUsers.contains(username)
				&& users.get(username).equals(password)) {
			loggedInUsers.add(username);
			return true;
		}
		return false;
	}
	
	public void logOut(String username) {
		loggedInUsers.remove(username);
	}
	
}
