package com.google.devrel.training.conference.servlet;

import static com.google.devrel.training.conference.service.OfyService.ofy;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.base.Joiner;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.domain.Conference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet for putting announcements in memcache.
 */
public class SetAnnouncementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
      	// Query for conferences with less than 5 seats lef
    	Iterable<Conference> iterable = ofy().load().type(Conference.class)
    	                .filter("seatsAvailable <", 5)
    	                .filter("seatsAvailable >", 0);
        List<String> conferenceNames = new ArrayList<>(0);
        for (Conference conference : iterable) {
            conferenceNames.add(conference.getName());
        }
        if (conferenceNames.size() > 0) {
            StringBuilder announcementStringBuilder = new StringBuilder(
                    "Last chance to attend! The following conferences are nearly sold out: ");
            Joiner joiner = Joiner.on(", ").skipNulls();
            announcementStringBuilder.append(joiner.join(conferenceNames));
            MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
            memcacheService.put(Constants.MEMCACHE_ANNOUNCEMENTS_KEY,
                    announcementStringBuilder.toString());
        }
        response.setStatus(204);
    }
}
