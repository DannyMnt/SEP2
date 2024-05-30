package model;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a UserEvent with a user ID and an event ID.
 */
public class UserEvent implements Serializable {
    private UUID userId;
    private UUID eventId;

    /**
     * Constructs a UserEvent with the specified user ID and event ID.
     *
     * @param userId The UUID of the user associated with the event.
     * @param eventId The UUID of the event associated with the user.
     */
    public UserEvent(UUID userId, UUID eventId){
        this.userId = userId;
        this.eventId = eventId;
    }

    /**
     * Retrieves the event ID associated with this UserEvent.
     *
     * @return The UUID of the event associated with this UserEvent.
     */
    public synchronized UUID getEventId() {
        return eventId;
    }


    /**
     * Retrieves the user ID associated with this UserEvent.
     *
     * @return The UUID of the user associated with this UserEvent.
     */
    public synchronized UUID getUserId() {
        return userId;
    }

    /**
     * Returns a string representation of this UserEvent.
     *
     * @return A string representation of this UserEvent.
     */
    @Override
    public synchronized String toString() {
        return "UserEvents{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                '}';
    }
}
