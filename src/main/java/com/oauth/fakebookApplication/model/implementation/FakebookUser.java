package com.oauth.fakebookApplication.model.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FakebookUser {
	private int userId;
	private String email;
	private String name;
	private String password;
	private Set<FakebookUser> friends = new HashSet<>();
	private List<Post> wall = new ArrayList<>();
	
	public FakebookUser(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	public FakebookUser() {}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Iterator<FakebookUser> getFriends() {
		return friends.iterator();
	}
	
	public void addFriend(FakebookUser friend) {
		this.friends.add(friend);
	}
	
	public void removeFriend(FakebookUser friend) {
		this.friends.remove(friend);
	}
	
	public Iterator<Post> getWall() {
		return wall.iterator();
	}
	
	public void addPost(Post post) {
		this.wall.add(post);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() { return this.password; }

	public void setPassword(String password) { this.password = password; }
	
}
