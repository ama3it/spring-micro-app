package com.fitness.activityservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fitness.activityservice.model.Activity;

// @Repository 
public interface ActivityRepository extends MongoRepository<Activity, String> {
    // Custom query methods can be defined here if needed
    // For example, find activities by userId or type
    // List<Activity> findByUserId(String userId);
    // List<Activity> findByType(ActivityType type);

} 