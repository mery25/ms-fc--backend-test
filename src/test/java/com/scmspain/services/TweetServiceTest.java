package com.scmspain.services;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import com.scmspain.entities.Link;
import com.scmspain.entities.Tweet;
import com.scmspain.exceptions.TweetNotFoundException;

public class TweetServiceTest {
    private EntityManager entityManager;
    private MetricWriter metricWriter;
    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.metricWriter = mock(MetricWriter.class);

        this.tweetService = new TweetService(entityManager, metricWriter);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(entityManager).persist(any(Tweet.class));
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
    
    @Test
    public void shouldInsertANewTweetWithLinks() throws Exception {
    	List<Link> links = new ArrayList<>();
    	links.add(new Link("http://foogle.co", 4));
    	links.add(new Link("http://google.com", 10));
        tweetService.publishTweet("Guybrush Threepwood", "I am Guybrush Threepwood, mighty pirate.");

        verify(entityManager).persist(any(Tweet.class));
    }
    
    @Test(expected = TweetNotFoundException.class)
    public void shouldThrowAnExceptionWhenDiscardTweetNotFound() throws Exception {
    	tweetService.discardTweet(10L);
    }
    
    @Test
    public void shouldDiscardATweet() throws Exception {
        when(entityManager.find(Tweet.class, 1L)).thenReturn(new Tweet());
        tweetService.discardTweet(1L);
        verify(entityManager).merge(any(Tweet.class));
    }
    
}
