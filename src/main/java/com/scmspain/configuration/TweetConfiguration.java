package com.scmspain.configuration;

import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.repository.TweetRepository;
import com.scmspain.services.TweetService;

@Configuration
public class TweetConfiguration {
    @Bean
    public TweetService getTweetService(MetricWriter metricWriter, TweetRepository repository) {
        return new TweetService(metricWriter, repository);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }
}
