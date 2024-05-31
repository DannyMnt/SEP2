package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an event in the system, which can be created and managed by users.
 * Each event has a unique identifier, a creator, a title, a description, start and end times,
 * a location, and a list of attendee IDs.
 */
public class Event implements Serializable {
    private UUID creatorId;
    private UUID eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<UUID> attendeeIDs;
    private String location;

    /**
     * Constructs an Event object with the provided details.
     *
     * @param creatorId   The unique identifier of the user who created the event.
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The start time of the event.
     * @param endTime     The end time of the event.
     * @param location    The location of the event.
     */
    public Event(UUID creatorId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location) {
        this.creatorId = creatorId;
        this.eventId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.attendeeIDs = new ArrayList<>();
    }





    /**
     * Constructs an Event object with the provided details and attendee IDs.
     *
     * @param creatorId   The unique identifier of the user who created the event.
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The start time of the event.
     * @param endTime     The end time of the event.
     * @param location    The location of the event.
     * @param attendeeIDs The list of attendee IDs for the event.
     */
    public Event(UUID creatorId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, List<UUID> attendeeIDs){
        this.creatorId = creatorId;
        this.eventId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.attendeeIDs = attendeeIDs;
    }

    /**
     * Constructs an Event object with the provided details, including the event ID.
     *
     * @param eventId     The unique identifier of the event.
     * @param creatorId   The unique identifier of the user who created the event.
     * @param title       The title of the event.
     * @param description The description of the event.
     * @param startTime   The start time of the event.
     * @param endTime     The end time of the event.
     * @param location    The location of the event.
     * @param attendeeIDs The list of attendee IDs for the event.
     */
    public Event(UUID eventId, UUID creatorId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, List<UUID> attendeeIDs){
        this.creatorId = creatorId;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.attendeeIDs = attendeeIDs;
    }

    /**
     * Retrieves the list of attendee IDs for the event.
     *
     * @return The list of attendee IDs for the event.
     */
    public synchronized List<UUID> getAttendeeIDs()
    {
        return attendeeIDs;
    }

    /**
     * Sets the location of the event.
     *
     * @param location The location to be set for the event.
     */
    public synchronized void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the location of the event.
     *
     * @return The location of the event.
     */
    public synchronized String getLocation() {
        return location;
    }

    /**
     * Retrieves the title of the event.
     *
     * @return The title of the event.
     */
    public synchronized String getTitle() {
        return title;
    }

    /**
     * Sets the title of the event.
     *
     * @param title The title to be set for the event.
     */
    public synchronized void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the description of the event.
     *
     * @return The description of the event.
     */
    public synchronized String getDescription() {
        return description;
    }

    /**
     * Sets the description of the event.
     *
     * @param description The description to be set for the event.
     */
    public synchronized void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the start time of the event.
     *
     * @return The start time of the event.
     */
    public synchronized LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the event.
     *
     * @param startTime The start time to be set for the event.
     */
    public synchronized void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Retrieves the end time of the event.
     *
     * @return The end time of the event.
     */
    public synchronized LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the event.
     *
     * @param endTime The end time to be set for the event.
     */
    public synchronized void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Retrieves the unique identifier of the user who created the event.
     *
     * @return The unique identifier of the event creator.
     */
    public synchronized UUID getCreatorId() {
        return creatorId;
    }


    /**
     * Returns a string representation of the event, including its creator ID, event ID, title,
     * description, start and end times, and location.
     *
     * @return A string representation of the event.
     */
    @Override
    public synchronized String toString() {
        return "Event{" +
            "creatorId=" + creatorId +
            ", eventId=" + eventId +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", location='" + location + '\'' +
            '}';
    }

    /**
     * Retrieves the unique identifier of the event.
     *
     * @return The unique identifier of the event.
     */
    public synchronized UUID getEventId() {
        return eventId;
    }
}
