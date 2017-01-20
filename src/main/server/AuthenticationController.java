package main.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class AuthenticationController {
	
	public static String FILE_PATH = "users";
	HashMap<String, String> users;
	
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
		//TODO add info to client's sign up page
		if (username.length() < 3 || password.length() < 8 || users.containsKey(username)) {
			throw new IllegalArgumentException();
		} else {
			users.put(username, password);
		}
	}
	
	public boolean validate(String username, String password) {
		if (users.containsKey(username)) {
			return users.get(username).equals(password);
		}
		return false;
	}
	
}
