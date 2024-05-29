package model;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class EventRepository {
    private DatabaseSingleton database;

    private final Log log;

    public static final String CLASS = "(server/model/EventRepository)";

    public EventRepository(DatabaseSingleton database) {
        this.database = database;
        this.log = Log.getInstance();
    }

    public synchronized void createEvent(Event event) {
        String sql = "INSERT INTO events (eventId, title, description, startTime, endTime, ownerId, location) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, event.getEventId());
            statement.setString(2, event.getTitle());
            statement.setString(3, event.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(event.getStartTime()));
            statement.setTimestamp(5, Timestamp.valueOf(event.getEndTime()));
            statement.setObject(6, event.getCreatorId());
            statement.setObject(7, event.getLocation());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.addLog("Failed to create the event in the database " + CLASS);
            log.addLog(e.getStackTrace().toString());        }
    }

    public synchronized Event getEventById(UUID eventId) {
        String sql = "SELECT * FROM events WHERE eventid = ?";
        Event event = null;

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    UUID ownerId = UUID.fromString(resultSet.getString("ownerId"));
                    List<UUID> attendeeIDs = getAttendeeIDs(eventId);
                    attendeeIDs.add(ownerId);
                    event = new Event(eventId,
                            ownerId,
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("startTime").toLocalDateTime(),
                            resultSet.getTimestamp("endTime").toLocalDateTime(),
                            resultSet.getString("location"),attendeeIDs);
                }

            }
        } catch (SQLException e) {
            log.addLog("Failed to get event by id from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());               }
        return event;
    }

    public List<Event> getEventsByOwner(UUID ownerId) {
        String sql = "SELECT * FROM events WHERE ownerId = ?";
        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, ownerId);
            synchronized (events){
                createEventsFromSet(events, statement);

            }
        } catch (SQLException e) {
            log.addLog("Failed to get list of events by owner from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());
        }
        return events;
    }

    public List<Event> getEventsByOwner(UUID ownerId, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT * FROM events WHERE startTime >= ? AND endTime <= ? AND ownerId = ?";
        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {

            statement.setTimestamp(1, Timestamp.valueOf(startDate));
            statement.setTimestamp(2, Timestamp.valueOf(endDate));
            statement.setObject(3, ownerId);
            synchronized (events){
                createEventsFromSet(events, statement);

            }

        } catch (SQLException e) {
            log.addLog("Failed to get events by owner from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());                  }
        return events;
    }




    public synchronized void createEventsFromSet(List<Event> events,
                                    PreparedStatement statement) throws SQLException {
        List<Event> threadSafeEvents = Collections.synchronizedList(events);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID eventId = UUID.fromString(resultSet.getString("eventId"));
                UUID ownerId = UUID.fromString(resultSet.getString("ownerId"));
                List<UUID> attendeeIDs = getAttendeeIDs(eventId);
                attendeeIDs.add(ownerId);
                Event event = new Event(eventId,
                        ownerId,
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("startTime").toLocalDateTime(),
                        resultSet.getTimestamp("endTime").toLocalDateTime(),
                        resultSet.getString("location"),attendeeIDs);

                threadSafeEvents.add(event);
            }
        }
    }

    public synchronized void deleteEvent(UUID eventId) {
        String sql = "DELETE FROM events WHERE eventId = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, eventId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.addLog("Failed to delete event from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());                  }
    }

    public void updateEvent(Event event) {
        String sql = "UPDATE events SET title = ?, description = ?, startTime = ?, endTime = ?, ownerId = ? WHERE eventId = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(event.getStartTime()));
            statement.setTimestamp(4, Timestamp.valueOf(event.getEndTime()));
            statement.setObject(5, event.getCreatorId());
            statement.setObject(6, event.getEventId());

            statement.executeUpdate();
        } catch (SQLException e) {
            log.addLog("Failed to update event in the database " + CLASS);
            log.addLog(e.getStackTrace().toString());                  }


    }

    public List<UUID> getAttendeeIDs(UUID eventId){
        List<UUID> attendeeIDs = new ArrayList<>();
        String sql = "SELECT userId FROM userevents WHERE eventId = ?";
        try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
        {
            statement.setObject(1,eventId);
            try(ResultSet resultSet = statement.executeQuery())
            {
                while(resultSet.next()){
                    UUID attendeeId = UUID.fromString(resultSet.getString("userId"));
                    attendeeIDs.add(attendeeId);
                }

            }
        }catch (SQLException e){
            log.addLog("Failed to get ids of attendees from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());                  }
        return attendeeIDs;
    }

    public void removeEvent(UUID eventId) {
        String deleteEventSQL = "DELETE FROM events WHERE eventId = ?";
        String deleteUserEventSQL = "DELETE FROM userEvents WHERE eventId = ?";

        try (
            PreparedStatement deleteEventStmt = database.getConnection().prepareStatement(deleteEventSQL);
            PreparedStatement deleteUserEventStmt = database.getConnection().prepareStatement(deleteUserEventSQL)
        ) {
            database.getConnection().setAutoCommit(false); // Start transaction

            // Delete from userEvents table
            deleteUserEventStmt.setObject(1, eventId);
            deleteUserEventStmt.executeUpdate();

            // Delete from events table
            deleteEventStmt.setObject(1, eventId);
            deleteEventStmt.executeUpdate();

            database.getConnection().commit(); // Commit transaction
        } catch (SQLException e) {
            log.addLog("Failed to remove event from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());
            try {
                database.getConnection().rollback(); // Rollback transaction on error
            } catch (SQLException rollbackException) {
                log.addLog("Failed to rollback the transaction in the database " + CLASS);
                log.addLog(rollbackException.getStackTrace().toString());
            }
        } finally {
            try {
                database.getConnection().setAutoCommit(true); // Reset to default state
            } catch (SQLException e) {
                log.addLog("Failed to enable auto commit in the database " + CLASS);
                log.addLog(e.getStackTrace().toString());
            }
        }
    }

    public synchronized List<Event> getUsersEvents(UUID userId){
        List<Event> events = Collections.synchronizedList(new ArrayList<>());
        String sql = "SELECT * FROM events WHERE ownerId = ? " +
        "OR eventId IN (SELECT eventId FROM userevents WHERE userId = ?)";

        try(PreparedStatement statement = database.getConnection().prepareStatement(sql))
        {
            statement.setObject(1,userId);
            statement.setObject(2,userId);

            createEventsFromSet(events,statement);

            }catch (SQLException e){
            log.addLog("Failed to get events by user from the database " + CLASS);
            log.addLog(e.getStackTrace().toString());
        }
        return events;
    }
}
