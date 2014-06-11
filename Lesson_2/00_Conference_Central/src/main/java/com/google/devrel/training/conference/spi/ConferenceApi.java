package com.google.devrel.training.conference.spi;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.domain.Profile;
import com.google.devrel.training.conference.form.ProfileForm;
import com.google.devrel.training.conference.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;

/**
 * Defines conference APIs.
 */
@Api(name = "conference", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
        Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "API for the Conference Central Backend application.")
public class ConferenceApi {

    /*
     * Get the display name from the user's email. For example, if the email is
     * lemoncake@example.com, then the display name becomes "lemoncake."
     */
    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }

    /**
     * Creates or updates a Profile object associated with the given user
     * object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @param profileForm
     *            A ProfileForm object sent from the client form.
     * @return Profile object just created.
     * @throws UnauthorizedException
     *             when the User object is null.
     */

    // Declare this method as a method available externally through Endpoints
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    // The request that invokes this method should provide data that
    // conforms to the fields defined in ProfileForm

    // TODO 1 Pass the ProfileForm parameter
    // TODO 2 Pass the User parameter
    public Profile saveProfile() throws UnauthorizedException {

        String userId = null;
        String mainEmail = null;
        String displayName = "Your name will go here";
        TeeShirtSize teeShirtSize = TeeShirtSize.NOT_SPECIFIED;

        // TODO 2
        // If the user is not logged in, throw an UnauthorizedException

        // TODO 1
        // Set the teeShirtSize to the value sent by the ProfileForm, if sent
        // otherwise leave it as the default value

        // TODO 1
        // Set the displayName to the value sent by the ProfileForm, if sent
        // otherwise set it to null

        // TODO 2
        // Get the userId and mainEmail

        // TODO 2
        // If the displayName is null, set it to default value based on the user's email
        // by calling extractDefaultDisplayNameFromEmail(...)

        // Create a new Profile entity from the
        // userId, displayName, mainEmail and teeShirtSize
        Profile profile = new Profile(userId, displayName, mainEmail, teeShirtSize);

        // TODO 3 (In Lesson 3)
        // Save the Profile entity in the datastore

        // Return the profile
        return profile;
    }

    /**
     * Returns a Profile object associated with the given user object. The cloud
     * endpoints system automatically inject the User object.
     *
     * @param user
     *            A User object injected by the cloud endpoints.
     * @return Profile object.
     * @throws UnauthorizedException
     *             when the User object is null.
     */
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // TODO
        // load the Profile Entity
        String userId = ""; // TODO
        Key key = null; // TODO
        Profile profile = null; // TODO load the Profile entity
        return profile;
    }
}
