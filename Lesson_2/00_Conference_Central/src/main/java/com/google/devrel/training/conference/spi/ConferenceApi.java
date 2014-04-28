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
        
        String userId = "";
        String mainEmail = "";
        String displayName = "Your name will go here";
        String teeShirtSize = "I will know your tee shirt size as soon as you tell me";

        // TODO 2
        // If the user is not logged in, throw an UnauthorizedException
        
        // TODO 2
        // Get the userId and mainEmail
        
        // TODO 1
        // Get the displayName and teeShirtSize sent by the request.
        // Un-comment this code
        /*
         displayName = profileForm.getDisplayName(); 
         if (profileForm.getTeeShirtSize() != null) {
             teeShirtSize = profileForm.getTeeShirtSize();
         }
        */
        

        // TODO 2
        // Populate the displayName with the default value if null.
        // Uncomment this code
        /*
         if (displayName == null) { 
           displayName = extractDefaultDisplayNameFromEmail(user.getEmail()); 
           }
         */

        // Create a new Profile entity from the
        // userId, displayName, mainEmail and teeShirtSize
        Profile profile = new Profile(userId, displayName, mainEmail, 
                teeShirtSize);

        // TODO 3
        // Save the entity in the datastore

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
