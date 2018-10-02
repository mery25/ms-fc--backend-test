package com.scmspain.exceptions;

public class TweetNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6283877308769877003L;

	private final Long tweet;
	
	public TweetNotFoundException(Long tweet) {
		super("No Tweet found with id " + tweet);
		this.tweet = tweet;
	}

	public Long getTweet() {
		return tweet;
	}
	
}
