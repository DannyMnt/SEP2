package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Repository class for managing events in the database.
 */
public class EventRepository {
    private DatabaseSingleton database;

    private final Log log;

    public static final String CLASS = "(server/model/EventRepository)";

    /**
     * Constructs an EventRepository object with a database connection.
     *
     * @param database The database connection.
     */
    public EventRepository(DatabaseSingleton database) {
        this.database = database;
        this.log = Log.getInstance();
    }

    /**
     * Creates a new event in the database.
     *
     * @param event The event to be created.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));        }
    }

    /**
     * Retrieves an event by its unique identifier from the database.
     *
     * @param eventId The unique identifier of the event.
     * @return The event with the specified ID, or null if not found.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));               }
        return event;
    }

    /**
     * Retrieves a list of events owned by a specific user from the database.
     *
     * @param ownerId The unique identifier of the owner user.
     * @return A list of events owned by the specified user.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
        return events;
    }

    /**
     * Retrieves a list of events owned by a specific user within a specified time range from the database.
     *
     * @param ownerId   The unique identifier of the owner user.
     * @param startDate The start date of the time range.
     * @param endDate   The end date of the time range.
     * @return A list of events owned by the specified user within the given time range.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));                  }
        return events;
    }



    /**
     * Creates a list of events from the result set obtained from the database.
     *
     * @param events    The list to which events will be added.
     * @param statement The prepared statement to execute.
     * @throws SQLException If an SQL exception occurs while accessing the database.
     */
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

    /**
     * Deletes an event from the database based on its unique identifier.
     *
     * @param eventId The unique identifier of the event to delete.
     */
    public synchronized void deleteEvent(UUID eventId) {
        String sql = "DELETE FROM events WHERE eventId = ?";

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, eventId);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.addLog("Failed to delete event from the database " + CLASS);
            log.addLog(Arrays.toString(e.getStackTrace()));                  }
    }

    /**
     * Updates an existing event in the database.
     *
     * @param event The event object containing updated information.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));                  }


    }

    /**
     * Retrieves a list of attendee IDs for a given event from the database.
     *
     * @param eventId The unique identifier of the event.
     * @return A list of UUIDs representing attendee IDs.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
        return attendeeIDs;
    }

    /**
     * Removes an event and its associated user events from the database.
     * This method performs a transaction by deleting the event and related user events in a single atomic operation.
     *
     * @param eventId The unique identifier of the event to remove.
     */
    public void removeEvent(UUID eventId) {
        String deleteEventSQL = "DELETE FROM events WHERE eventId = ?";
        String deleteUserEventSQL = "DELETE FROM userEvents WHERE eventId = ?";

        try (
            PreparedStatement deleteEventStmt = database.getConnection().prepareStatement(deleteEventSQL);
            PreparedStatement deleteUserEventStmt = database.getConnection().prepareStatement(deleteUserEventSQL)
        ) {
            database.getConnection().setAutoCommit(false);

            deleteUserEventStmt.setObject(1, eventId);
            deleteUserEventStmt.executeUpdate();

            deleteEventStmt.setObject(1, eventId);
            deleteEventStmt.executeUpdate();

            database.getConnection().commit();
        } catch (SQLException e) {
            log.addLog("Failed to remove event from the database " + CLASS);
            log.addLog(Arrays.toString(e.getStackTrace()));
            try {
                database.getConnection().rollback();
            } catch (SQLException rollbackException) {
                log.addLog("Failed to rollback the transaction in the database " + CLASS);
                log.addLog(Arrays.toString(rollbackException.getStackTrace()));
            }
        } finally {
            try {
                database.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                log.addLog("Failed to enable auto commit in the database " + CLASS);
                log.addLog(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    /**
     * Retrieves a list of events associated with a specific user from the database.
     * This method fetches events owned by the user as well as events the user is attending.
     *
     * @param userId The unique identifier of the user.
     * @return A list of events associated with the specified user.
     */
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
            log.addLog(Arrays.toString(e.getStackTrace()));
        }
        return events;
    }
}
