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

    public EventRepository(DatabaseSingleton database) {
        this.database = database;
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
            e.printStackTrace();
        }
    }

    public synchronized Event getEventById(UUID eventId) {
        String sql = "SELECT * FROM events WHERE eventid = ?";
        Event event = null;

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    event = new Event(eventId,
                            UUID.fromString(resultSet.getString("ownerId")),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("startTime").toLocalDateTime(),
                            resultSet.getTimestamp("endTime").toLocalDateTime(),
                            resultSet.getString("location"),getAttendeeIDs(eventId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            e.printStackTrace();
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
            throw new RuntimeException(e);
        }
        return events;
    }

    public List<Event> getEventsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM events where startTime >=? AND endTime <= ?";
        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            statement.setTimestamp(2, Timestamp.valueOf(endTime));
            synchronized (events){
                createEventsFromSet(events, statement);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    public List<Event> getEventsBefore(LocalDateTime endTime) {
        String sql = "SELECT * FROM events WHERE endTime <= ?";
        List<Event> events = Collections.synchronizedList(new ArrayList<>());

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(endTime));
            synchronized (events){
                createEventsFromSet(events, statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<Event> getEventsAfter(LocalDateTime startTime) {
        String sql = "SELECT * FROM events WHERE startTime >= ?";
        List<Event> events = Collections.synchronizedList(new ArrayList<>());
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            synchronized (events){
                createEventsFromSet(events, statement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }


    public synchronized void createEventsFromSet(List<Event> events,
                                    PreparedStatement statement) throws SQLException {
        List<Event> threadSafeEvents = Collections.synchronizedList(events);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID eventId = UUID.fromString(resultSet.getString("eventId"));
                Event event = new Event(eventId,
                        UUID.fromString(resultSet.getString("ownerId")),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("startTime").toLocalDateTime(),
                        resultSet.getTimestamp("endTime").toLocalDateTime(),
                        resultSet.getString("location"),getAttendeeIDs(eventId));

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
            e.printStackTrace();
        }
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
            e.printStackTrace();
        }


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
            e.printStackTrace();
        }
        return attendeeIDs;
    }

    public synchronized void removeEvent(UUID eventId){
        String deleteEventSQL = "DELETE FROM events WHERE eventId = ?";
        String deleteUserEventSQL = "DELETE FROM userEvents WHERE eventId = ?";
        Connection conn = null;
        try(Connection connection = database.getConnection();
        PreparedStatement deleteEventStmt = connection.prepareStatement(deleteEventSQL);
        PreparedStatement deleteUserEventStmt = connection.prepareStatement(deleteUserEventSQL))
        {
            conn = connection;
            conn.setAutoCommit(false);
            deleteUserEventStmt.setObject(1,eventId);
            deleteUserEventStmt.executeUpdate();

            deleteEventStmt.setObject(1,eventId);
            deleteEventStmt.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
        }catch (SQLException e){
            e.printStackTrace();
            try
            {
                if(conn!= null){
                    conn.rollback();
                }
            }catch (SQLException f){
                f.printStackTrace();
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
            e.printStackTrace();
        }
        return events;
    }
}
