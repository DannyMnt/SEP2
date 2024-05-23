package model;

import java.io.Serializable;
import java.util.UUID;

public class UserEvent implements Serializable {
    private UUID userId;
    private UUID eventId;
    public UserEvent(UUID userId, UUID eventId){
        this.userId = userId;
        this.eventId = eventId;
    }

    public synchronized UUID getEventId() {
        return eventId;
    }

    public synchronized UUID getUserId() {
        return userId;
    }

    @Override
    public synchronized String toString() {
        return "UserEvents{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                '}';
    }
}
