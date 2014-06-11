package com.google.devrel.training.conference.domain;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class AppEngineUser {
    @Id
    private String email;
    private User user;
    private AppEngineUser() {}
    public AppEngineUser(User user) {
        this.user = user;
        this.email = user.getEmail();
    }
    public User getUser() {
        return user;
    }
    public Key<AppEngineUser> getKey() {
        return Key.create(AppEngineUser.class, email);
    }
}
