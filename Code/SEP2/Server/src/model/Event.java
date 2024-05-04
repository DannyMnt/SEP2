package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event implements Serializable {
    private UUID creatorId;
    private UUID eventId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Event(UUID creatorId, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.creatorId = creatorId;
        this.eventId = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
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
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public UUID getEventId() {
        return eventId;
    }
}
