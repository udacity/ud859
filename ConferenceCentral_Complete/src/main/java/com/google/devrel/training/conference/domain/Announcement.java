package com.google.devrel.training.conference.domain;

/**
 * A simple wrapper for announcement message.
 */
public class Announcement {

    private String message;

    public Announcement() {}

    public Announcement(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
