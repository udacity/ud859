package com.google.devrel.training.conference.spi;

import static com.google.devrel.training.conference.service.OfyService.ofy;
import static org.junit.Assert.*;

import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.devrel.training.conference.domain.Conference;
import com.google.devrel.training.conference.domain.Profile;
import com.google.devrel.training.conference.form.ConferenceForm;
import com.google.devrel.training.conference.form.ProfileForm;
import com.google.devrel.training.conference.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Tests for ConferenceApi API methods.
 */
public class ConferenceApiTest {

    private static final String EMAIL = "testuser@example.com";

    private static final String USER_ID = "123456789";

    private static final TeeShirtSize TEE_SHIRT_SIZE = TeeShirtSize.NOT_SPECIFIED;

    private static final String DISPLAY_NAME = "Test User";

    private static final String NAME = "GCP Live";

    private static final String DESCRIPTION = "New announcements for Google Cloud Platform";

    private static final String CITY = "Mountain View";

    private static final int MONTH = 3;

    private static final int CAP = 500;

    private User user;

    private ConferenceApi conferenceApi;

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        user = new User(EMAIL, "gmail.com", USER_ID);
        conferenceApi = new ConferenceApi();
    }

    @After
    public void tearDown() throws Exception {
        ofy().clear();
        helper.tearDown();
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetProfileWithoutUser() throws Exception {
        conferenceApi.getProfile(null);
    }

    @Test
    public void testGetProfileFirstTime() throws Exception {
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertNull(profile);
        profile = conferenceApi.getProfile(user);
        assertNull(profile);
    }

    @Test
    public void testSaveProfile() throws Exception {
        // Save the profile for the first time.
        Profile profile = conferenceApi.saveProfile(
                user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testSaveProfileWithNull() throws Exception {
        // Save the profile for the first time with null values.
        Profile profile = conferenceApi.saveProfile(user, new ProfileForm(null, null));
        String displayName = EMAIL.substring(0, EMAIL.indexOf("@"));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(displayName, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(displayName, profile.getDisplayName());
    }

    @Test
    public void testGetProfile() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Fetch the Profile via the API.
        Profile profile = conferenceApi.getProfile(user);
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        // Save for the first time.
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Then try to update it.
        String newDisplayName = "Kay's Daddy";
        TeeShirtSize newTeeShirtSize = TeeShirtSize.L;
        conferenceApi.saveProfile(user, new ProfileForm(newDisplayName, newTeeShirtSize));
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(newTeeShirtSize, profile.getTeeShirtSize());
        assertEquals(newDisplayName, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfileWithNulls() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Update the Profile with null values.
        Profile profile = conferenceApi.saveProfile(user, new ProfileForm(null, null));
        // Expected behavior is that the existing properties do not get overwritten

        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testCreateConference() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        // Check the return value.
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerUserId());
        assertEquals(CITY, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        assertEquals(MONTH, conference.getMonth());
        // Check if a new Profile is created
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        String displayName = EMAIL.substring(0, EMAIL.indexOf("@"));
        assertEquals(displayName, profile.getDisplayName());
    }

    @Test
    public void testGetConferencesCreated() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);

        List<Conference> conferencesCreated = conferenceApi.getConferencesCreated(user);
        assertEquals(1, conferencesCreated.size());
        assertTrue("The result should contain a conference",
                conferencesCreated.contains(conference));
    }

    @Test
    public void testUpdateConference() throws Exception {
        // First create a conference.
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        // Check the return value.
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerUserId());
        assertEquals(CITY, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        assertEquals(MONTH, conference.getMonth());

        // Update it with new values.
        String newName = "Google I/O";
        String newDescription = "Google's annual developer event.";
        String newCity = "San Francisco";
        startDate = dateFormat.parse("06/25/2014");
        endDate = dateFormat.parse("06/26/2014");
        topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Developers");
        int newCap = 5000;
        conferenceForm = new ConferenceForm(newName, newDescription, topics, newCity, startDate,
                endDate, newCap);
        conference = conferenceApi.updateConference(
                user, conferenceForm, conference.getWebsafeKey());
        assertEquals(newName, conference.getName());
        assertEquals(newDescription, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerUserId());
        assertEquals(newCity, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(newCap, conference.getMaxAttendees());
        assertEquals(newCap, conference.getSeatsAvailable());
        assertEquals(6, conference.getMonth());
    }

    @Test
    public void testGetConference() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        // Check the return value.
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerUserId());
        assertEquals(CITY, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        assertEquals(MONTH, conference.getMonth());
    }

    @Test
    public void testRegistrations() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        // Registration
        Boolean result = conferenceApi.registerForConference(
                user, conference.getWebsafeKey()).getResult();
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertTrue("registerForConference should succeed.", result);
        assertEquals(CAP - 1, conference.getSeatsAvailable());
        assertTrue("Profile should have the conferenceId in conferenceIdsToAttend.",
                profile.getConferenceKeysToAttend().contains(conference.getWebsafeKey()));

        // Unregister
        result = conferenceApi.unregisterFromConference(
                user, conference.getWebsafeKey()).getResult();
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertTrue("unregisterFromConference should succeed.", result);
        assertEquals(CAP, conference.getSeatsAvailable());
        assertFalse("Profile shouldn't have the conferenceId in conferenceIdsToAttend.",
                profile.getConferenceKeysToAttend().contains(conference.getWebsafeKey()));
    }

    @Test(expected = ConflictException.class)
    public void testRegistrationFailure_NoSeatsAvailable() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        // Create a conference as the remaining seats is zero.
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, 0);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        conferenceApi.registerForConference(
                user, conference.getWebsafeKey()).getResult();
    }

    @Test(expected = ConflictException.class)
    public void testRegistrationFailure_AlreadyRegisteredForTheSameConference() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        // Registration
        Boolean result = conferenceApi.registerForConference(
                user, conference.getWebsafeKey()).getResult();
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertTrue("The first registration should succeed.", result);
        assertEquals(CAP - 1, conference.getSeatsAvailable());
        assertTrue("Profile should have the conferenceId in conferenceIdsToAttend.",
                profile.getConferenceKeysToAttend().contains(conference.getWebsafeKey()));

        // The user has already registered for the conference. This should throw an ForbiddenException.
        conferenceApi.registerForConference(
                user, conference.getWebsafeKey()).getResult();
    }

    @Test(expected = NotFoundException.class)
    public void testGetConferenceToAttendWithoutProfile() throws Exception {
        conferenceApi.getConferencesToAttend(
                new User("anotheruser@example.com", "gmail.com", "anotheruserid"));
    }

    @Test
    public void testGetConferenceToAttend() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);

        // Should be 0 result.
        Collection<Conference> conferenceToAttend = conferenceApi.getConferencesToAttend(user);
        assertEquals(0, conferenceToAttend.size());

        // Registration
        conferenceApi.registerForConference(user, conference.getWebsafeKey());
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        conferenceToAttend = conferenceApi.getConferencesToAttend(user);
        assertEquals(1, conferenceToAttend.size());
        assertTrue("The result should contain the conference.",
                conferenceToAttend.contains(conference));
    }
}
