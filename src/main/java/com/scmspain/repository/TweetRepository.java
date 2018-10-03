package com.scmspain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scmspain.entities.Tweet;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, Long> {

	@Query("FROM Tweet WHERE pre2015MigrationStatus<>99 AND discardedDateTime=NULL ORDER BY publicationDateTime DESC")
	List<Tweet> findAllSortedByPublicationDate();
	
	@Query("FROM Tweet WHERE pre2015MigrationStatus<>99 AND discardedDateTime<>NULL ORDER BY discardedDateTime DESC")
	List<Tweet> findAllDiscardedSortedByDiscardedDate();
	
}