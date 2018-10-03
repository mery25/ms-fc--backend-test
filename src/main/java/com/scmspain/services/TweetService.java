package com.scmspain.services;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;

import com.scmspain.entities.Link;
import com.scmspain.entities.Tweet;
import com.scmspain.exceptions.TweetNotFoundException;
import com.scmspain.utils.PatternExtractor;

@Service
@Transactional
public class TweetService {
	
    private final EntityManager entityManager;
    private final MetricWriter metricWriter;
    private final Validator validator;

    public TweetService(final EntityManager entityManager, 
    		            final MetricWriter metricWriter) {
        this.entityManager = entityManager;
        this.metricWriter = metricWriter;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void publishTweet(String publisher, String text) {
    	PatternExtractor patternExtractor = new PatternExtractor(Link.REGEX_PATTERN, text);
    	patternExtractor.extract();
    	List<Link> links = patternExtractor.getExtractedPatternMap().entrySet()
    			                                                    .stream()
    			                                                    .map((e) -> new Link(e.getValue(), e.getKey()))
    			                                                    .collect(Collectors.toList());
    	String convertedText = patternExtractor.getText();
		Tweet tweet = new Tweet(convertedText, publisher, links);
		Set<ConstraintViolation<Tweet>> constraints = validator.validate(tweet);
		if (constraints.size() != 0)
			throw new ConstraintViolationException(constraints);

		this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
		this.entityManager.persist(tweet);
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