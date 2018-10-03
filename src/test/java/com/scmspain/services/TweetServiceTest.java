package com.scmspain.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import com.scmspain.entities.Link;
import com.scmspain.entities.Tweet;
import com.scmspain.exceptions.TweetNotFoundException;
import com.scmspain.repository.TweetRepository;

public class TweetServiceTest {
    private MetricWriter metricWriter;
    private TweetService tweetService;
    private TweetRepository repository;

    @Before
    public void setUp() throws Exception {
        this.repository = mock(TweetRepository.class);
        this.metricWriter = mock(MetricWriter.class);

        this.tweetService = new TweetService(metricWriter, repository);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(repository).save(any(Tweet.class));
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowAnExceptionWhenTweetLengthIsInvalid() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }
    
    @Test(expected = ValidationException.class)
    public void shouldThrowAnExceptionWhenTweetTextIsEmpty() throws Exception {
        tweetService.publishTweet("Pirate", "");
    }
    
    @Test(expected = ValidationException.class)
    public void shouldThrowAnExceptionWhenTweetPubliserIsEmpty() throws Exception {
        tweetService.publishTweet("", "I am Guybrush Threepwood, mighty pirate.");
    }
    
    @Test(expected = ValidationException.class)
    public void shouldThrowAnExceptionWhenNullPubliser() throws Exception {
        tweetService.publishTweet(null, "I am Guybrush Threepwood, mighty pirate.");
    }
    
    @Test(expected = ValidationException.class)
    public void shouldThrowAnExceptionWhenNullTweet() throws Exception {
        tweetService.publishTweet("Maria", null);
    }
    
    @Test
    public void shouldInsertANewTweetWithLinks() throws Exception {
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 4));
    	links.add(new Link("http://google.com", 10));
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(repository).save(any(Tweet.class));
    }
    
    @Test(expected = TweetNotFoundException.class)
    public void shouldThrowAnExceptionWhenDiscardTweetNotFound() throws Exception {
    	tweetService.discardTweet(10L);
    }
    
    @Test
    public void shouldDiscardATweet() throws Exception {
        when(repository.findOne(1L)).thenReturn(new Tweet());
        tweetService.discardTweet(1L);
        verify(repository).save(any(Tweet.class));
    }
    
}
