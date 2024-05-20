package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event implements Serializable {
    private UUID creatorId;
    private UUID eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<UUID> attendeeIDs;
    private String location;

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



    public List<UUID> getAttendeeIDs()
    {
        return attendeeIDs;
    }

    public Event( UUID creatorId, String title, String description, LocalDateTime startTime, LocalDateTime endTime, String location, List<UUID> attendeeIDs){
        this.creatorId = creatorId;
        this.eventId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.attendeeIDs = attendeeIDs;
    }

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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String toString() {
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

    public UUID getEventId() {
        return eventId;
    }
}
