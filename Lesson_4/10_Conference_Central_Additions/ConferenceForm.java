package com.google.devrel.training.conference.form;

import com.google.common.collect.ImmutableList;

import java.util.Date;
import java.util.List;

/**
 * A simple Java object (POJO) representing a Conference form sent from the client.
 */
public class ConferenceForm {
    /**
     * The name of the conference.
     */
    private String name;

    /**
     * The description of the conference.
     */
    private String description;

    /**
     * Topics that are discussed in this conference.
     */
    private List<String> topics;

    /**
     * The city where the conference will take place.
     */
    private String city;

    /**
     * The start date of the conference.
     */
    private Date startDate;

    /**
     * The end date of the conference.
     */
    private Date endDate;

    /**
     * The capacity of the conference.
     */
    private int maxAttendees;

    private ConferenceForm() {}

    /**
     * Public constructor is solely for Unit Test.
     * @param name
     * @param description
     * @param topics
     * @param city
     * @param startDate
     * @param endDate
     * @param maxAttendees
     */
    public ConferenceForm(String name, String description, List<String> topics, String city,
                          Date startDate, Date endDate, int maxAttendees) {
        this.name = name;
        this.description = description;
        this.topics = topics == null ? null : ImmutableList.copyOf(topics);
        this.city = city;
        this.startDate = startDate == null ? null : new Date(startDate.getTime());
        this.endDate = endDate == null ? null : new Date(endDate.getTime());
        this.maxAttendees = maxAttendees;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getCity() {
        return city;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }
}
