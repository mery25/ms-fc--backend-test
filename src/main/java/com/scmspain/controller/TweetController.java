package com.scmspain.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scmspain.controller.command.DiscardTweetCommand;
import com.scmspain.controller.command.PublishTweetCommand;
import com.scmspain.entities.Tweet;
import com.scmspain.exceptions.TweetNotFoundException;
import com.scmspain.services.TweetService;

@RestController
public class TweetController {
	
    private final TweetService tweetService;

    public TweetController(final TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/tweet")
    public List<Tweet> listAllTweets() {
        return this.tweetService.listAllTweets();
    }

    @PostMapping("/tweet")
    @ResponseStatus(CREATED)
    public void publishTweet(@RequestBody PublishTweetCommand publishTweetCommand) {
        this.tweetService.publishTweet(publishTweetCommand.getPublisher(), 
        		                       publishTweetCommand.getTweet());
    }
    
    @GetMapping("/discarded")
    public List<Tweet> listAllDiscardedTweets() {
        return this.tweetService.listAllDiscardedTweets();
    }
    
    @PostMapping("/discarded")
    @ResponseStatus(NO_CONTENT)
    public void discardTweet(@RequestBody DiscardTweetCommand discardTweetCommand) {
        this.tweetService.discardTweet(discardTweetCommand.getTweetId());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Object invalidValidationException(ValidationException ex) {
        return new Object() {
            public String message = ex.getMessage();
            public String exceptionClass = ex.getClass().getSimpleName();
        };
    }
    
    @ExceptionHandler(TweetNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Object invalidTweetNotFoundException(TweetNotFoundException ex) {
        return new Object() {
            public String message = ex.getMessage();
        };
    }
}
