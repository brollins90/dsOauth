package com.oauth.fakebookApplication.model;

import com.oauth.fakebookApplication.model.implementation.FakebookUser;

public interface FakebookDB {
	
	public FakebookUser getUser(int userId);
	public int addUser(FakebookUser user);
	public void updateUser(int userId, FakebookUser user);
}
