package com.fitness.activityservice.model;

public enum ActivityType {
    RUNNING,
    CYCLING,
    SWIMMING,
    WALKING,
    HIKING,
    YOGA,
    GYM,
    DANCE,
    OTHER;

    public static ActivityType fromString(String type) {
        try {
            return ActivityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
