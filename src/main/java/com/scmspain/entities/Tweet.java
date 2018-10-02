package com.scmspain.entities;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private String publisher;
    
    @Column(nullable = false, length = 140)
    private String tweet;
    
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    
    @Column (nullable=false)
    private Instant publicationDateTime;
    
    @Column (nullable=true)
    private Instant discardedDateTime;

    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    @JsonIgnore
	public Instant getDiscardedDateTime() {
		return discardedDateTime;
	}

	public void setDiscardedDateTime(Instant discardedDateTime) {
		this.discardedDateTime = discardedDateTime;
	}

	@JsonIgnore
	public Instant getPublicationDateTime() {
		return publicationDateTime;
	}

	public void setPublicationDateTime(Instant publicationDateTime) {
		this.publicationDateTime = publicationDateTime;
	}

}
