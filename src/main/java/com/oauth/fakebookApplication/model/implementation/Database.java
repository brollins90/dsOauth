package com.oauth.fakebookApplication.model.implementation;

import java.util.HashMap;
import java.util.Map;

import com.oauth.fakebookApplication.model.FakebookDB;

public class Database implements FakebookDB {
	private Map<Integer, FakebookUser> usersByID = new HashMap<>();
	private Map<String, FakebookUser> usersByEmail = new HashMap<>();
	private int users = 0;
	
	public Database() {
		FakebookUser defaultUser = new FakebookUser("Default@fakebook.com", "Default User", "password");
		addUser(defaultUser);
	}
	
	@Override
	public FakebookUser getUser(int userId) {
		return usersByID.get(userId);
	}

	@Override
	public int addUser(FakebookUser user) {
		int id = users++;
		user.setUserId(id);
		usersByID.put(id, user);
		usersByEmail.put(user.getEmail(), user);
		return id;
		//asdlkjffdsab
	}

	@Override
	public void updateUser(int userId, FakebookUser user) {
		FakebookUser existingUser = usersByID.get(userId);
		existingUser.setEmail(user.getEmail());
		existingUser.setName(user.getName());
	}

	@Override
	public FakebookUser getUser(String email, String password) {

		FakebookUser user = usersByEmail.get(email);

		if(user != null) {
			if (password.equals(user.getPassword())) {
				return user;
			}
		}
		return null;
	}
}
