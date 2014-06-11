package com.google.devrel.training.conference.domain;

import com.google.common.collect.ImmutableList;
import com.google.devrel.training.conference.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Profile class stores user's profile data.
 */
@Entity
public class Profile {
    /**
     *  Use userId as the datastore key.
     */
    @Id
    private String userId;

    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;

    /**
     * User's main e-mail address.
     */
    private String mainEmail;

    /**
     * The user's tee shirt size.
     * Options are defined as an Enum in ProfileForm
     */
    private TeeShirtSize teeShirtSize;

    /**
     * Keys of the conferences that this user registers to attend.
     */
    private List<String> conferenceKeysToAttend = new ArrayList<>(0);

    /**
     * Just making the default constructor private.
     */
    private Profile() {}

    /**
     * Public constructor for Profile.
     * @param userId The datastore key.
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * @param teeShirtSize User's teeShirtSize (Enum is in ProfileForm)
     */
    public Profile(String userId, String displayName, String mainEmail, TeeShirtSize teeShirtSize) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
        this.teeShirtSize = teeShirtSize;
    }

    /**
     * Getter for userId.
     * @return userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Getter for displayName.
     * @return displayName.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Getter for mainEmail.
     * @return mainEmail.
     */
    public String getMainEmail() {
        return mainEmail;
    }

    /**
     * Getter for teeShirtSize.
     * @return teeShirtSize.
     */
    public TeeShirtSize getTeeShirtSize() {
        return teeShirtSize;
    }

    /**
     * Getter for conferenceIdsToAttend.
     * @return an immutable copy of conferenceIdsToAttend.
     */
    public List<String> getConferenceKeysToAttend() {
        return ImmutableList.copyOf(conferenceKeysToAttend);
    }

    /**
     * Update the Profile with the given displayName and teeShirtSize
     * @param displayName
     * @param teeShirtSize
     */
    public void update(String displayName, TeeShirtSize teeShirtSize) {
        if (displayName != null) {
            this.displayName = displayName;
        }
        if (teeShirtSize != null) {
            this.teeShirtSize = teeShirtSize;
        }
    }

    /**
     * Adds a ConferenceId to conferenceIdsToAttend.
     *
     * The method initConferenceIdsToAttend is not thread-safe, but we need a transaction for
     * calling this method after all, so it is not a practical issue.
     *
     * @param conferenceKey a websafe String representation of the Conference Key.
     */
    public void addToConferenceKeysToAttend(String conferenceKey) {
        conferenceKeysToAttend.add(conferenceKey);
    }

    /**
     * Remove the conferenceId from conferenceIdsToAttend.
     *
     * @param conferenceKey a websafe String representation of the Conference Key.
     */
    public void unregisterFromConference(String conferenceKey) {
        if (conferenceKeysToAttend.contains(conferenceKey)) {
            conferenceKeysToAttend.remove(conferenceKey);
        } else {
            throw new IllegalArgumentException("Invalid conferenceKey: " + conferenceKey);
        }
    }
}
