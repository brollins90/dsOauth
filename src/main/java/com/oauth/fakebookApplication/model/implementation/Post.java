package com.oauth.fakebookApplication.model.implementation;

import java.time.LocalDateTime;

public class Post {
	private LocalDateTime dateTime;
	private String title;
	private String body;
	
	public Post(LocalDateTime dateTime, String title, String body) {
		this.dateTime = dateTime;
		this.title = title;
		this.body = body;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
