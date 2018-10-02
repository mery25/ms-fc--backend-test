package com.scmspain.services;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;

import com.scmspain.entities.Tweet;
import com.scmspain.exceptions.TweetNotFoundException;

@Service
@Transactional
public class TweetService {
    private final EntityManager entityManager;
    private final MetricWriter metricWriter;

    public TweetService(final EntityManager entityManager, 
    		            final MetricWriter metricWriter) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void publishTweet(String publisher, String text) {
        if (publisher != null && publisher.length() > 0 && text != null && text.length() > 0 && text.length() < 140) {
            Tweet tweet = new Tweet();
            tweet.setTweet(text);
            tweet.setPublisher(publisher);
            tweet.setPublicationDateTime(Instant.now());

            this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
            this.entityManager.persist(tweet);
        } else {
            throw new IllegalArgumentException("Tweet must not be greater than 140 characters");
        }
    }

    /**
      Recover tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public Tweet getTweet(Long id) {
      return this.entityManager.find(Tweet.class, id);
    }

    /**
      Recover tweets from repository
      Result - retrieved lost of Tweet
    */
    public List<Tweet> listAllTweets() {
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        TypedQuery<Tweet> query = this.entityManager.createQuery("FROM Tweet WHERE pre2015MigrationStatus<>99 AND discardedDateTime=NULL "
        		+ "ORDER BY publicationDateTime DESC", Tweet.class);
        return query.getResultList();
    }

    /**
    Discard tweet from repository
    Parameter - id - id of the Tweet to discard
    Result - discarded Tweet
  */
	public void discardTweet(Long tweetId) {
		Tweet tweet = getTweet(tweetId);
		if (tweet == null)
			throw new TweetNotFoundException(tweetId);
		
		tweet.setDiscardedDateTime(Instant.now());
		this.metricWriter.increment(new Delta<Number>("discarded-tweets", 1));
		this.entityManager.merge(tweet);
	}

    /**
    Recover discarded tweets from repository
    Result - retrieved list of discard Tweets
  */
	public List<Tweet> listAllDiscardedTweets() {
        this.metricWriter.increment(new Delta<Number>("times-queried-discarded-tweets", 1));

        TypedQuery<Tweet> query = this.entityManager.createQuery("FROM Tweet WHERE pre2015MigrationStatus<>99 AND discardedDateTime<>NULL"
        		+ " ORDER BY discardedDateTime DESC", Tweet.class);
        return query.getResultList();
	}
	
}