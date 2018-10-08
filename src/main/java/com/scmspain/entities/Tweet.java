package com.scmspain.entities;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scmspain.utils.TextElementInserter;

@Entity
public class Tweet {
	
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    @NotNull
    @Size(min=1)
    private String publisher;
    
    @Column(nullable = false, length = 140)
    @NotNull
    @Size(min=1, max = 140)
    private String tweet;
    
    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;
    
    @Column (nullable=false)
    private Instant publicationDateTime;
    
    @Column (nullable=true)
    private Instant discardedDateTime;
    
    @Column (nullable=true)
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Link> links;

    public Tweet() {
    }

    public Tweet(String tweet, String publisher, List<Link> links) {
		super();
		this.tweet = tweet;
		this.publisher = publisher;
		this.links = links;
		this.publicationDateTime = Instant.now();
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

    @JsonProperty("tweet")
    public String getTweet() {
        return insertLinksOnTweet(tweet, links);
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

	private String insertLinksOnTweet(String text, List<Link> links) {
		return new TextElementInserter().apply(text, links);
	}

}
