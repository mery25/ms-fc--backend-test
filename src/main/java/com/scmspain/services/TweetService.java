package com.scmspain.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.scmspain.repository.TweetRepository;
import com.scmspain.utils.PatternExtractor;
import com.scmspain.utils.PatternExtractor.PatternExtractorResult;

@Service
@Transactional
public class TweetService {

    private final MetricWriter metricWriter;
    private final Validator validator;
    private final TweetRepository tweetRepository;
    
    public TweetService(final MetricWriter metricWriter,
    		            final TweetRepository repository) {
        this.metricWriter = metricWriter;
        this.tweetRepository = repository;
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
    	List<Link> links = new ArrayList<>();
    	if (text != null) {
    		PatternExtractorResult patternExtractorResult = new PatternExtractor().apply(Link.REGEX_PATTERN, text);
	    	links = patternExtractorResult.getExtractedPatternMap().entrySet().stream()
	    			                                               .map((e) -> new Link(e.getValue(), e.getKey()))
	    			                                               .collect(Collectors.toList());
	    	text = patternExtractorResult.getText();
    	}
		Tweet tweet = new Tweet(text, publisher, links);
		Set<ConstraintViolation<Tweet>> constraints = validator.validate(tweet);
		if (constraints.size() != 0)
			throw new ConstraintViolationException(constraints);

		this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
		tweetRepository.save(tweet);
    }

    /**
      Recover tweet from repository
      Parameter - id - id of the Tweet to retrieve
      Result - retrieved Tweet
    */
    public Tweet getTweet(Long id) {
    	return tweetRepository.findOne(id);
    }

    /**
      Recover tweets from repository
      Result - retrieved lost of Tweet
    */
    public List<Tweet> listAllTweets() {
        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        return tweetRepository.findAllSortedByPublicationDate();
    }

    /**
    Discard tweet from repository
    Parameter - id - id of the Tweet to discard
    Result - discarded Tweet
  */
	public void discardTweet(Long tweetId) {
		Tweet tweet = getTweet(tweetId);
		if (tweet == null || tweet.getDiscardedDateTime() != null)
			throw new TweetNotFoundException(tweetId);
		
		tweet.setDiscardedDateTime(Instant.now());
		this.metricWriter.increment(new Delta<Number>("discarded-tweets", 1));
		tweetRepository.save(tweet);
	}

    /**
    Recover discarded tweets from repository
    Result - retrieved list of discard Tweets
  */
	public List<Tweet> listAllDiscardedTweets() {
        this.metricWriter.increment(new Delta<Number>("times-queried-discarded-tweets", 1));
        return tweetRepository.findAllDiscardedSortedByDiscardedDate();
	}
	
}