package com.oauth.fakebookApplication.model.implementation;

import java.util.HashMap;
import java.util.Map;

import com.oauth.fakebookApplication.model.FakebookDB;

public class Database implements FakebookDB {
	private Map<Integer, FakebookUser> usersByID = new HashMap<>();
	private int users = 0;
	
	public Database() {
		FakebookUser defaultUser = new FakebookUser("Default@fakebook.com", "Default User");
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
		return id;
	}

	@Override
	public void updateUser(int userId, FakebookUser user) {
		FakebookUser existingUser = usersByID.get(userId);
		existingUser.setEmail(user.getEmail());
		existingUser.setName(user.getName());
	}
	
}
