package model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventRepository {
    private DatabaseSingleton database;

    public EventRepository(DatabaseSingleton database) {
        this.database = database;
    }

    public void createEvent(Event event) {
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

    public Event getEventById(UUID eventId) {
        String sql = "SELECT * FROM events WHERE eventid = ?";
        Event event = null;

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    event = new Event(UUID.fromString(resultSet.getString("eventId")),
                            UUID.fromString(resultSet.getString("ownerId")),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getTimestamp("startTime").toLocalDateTime(),
                            resultSet.getTimestamp("endTime").toLocalDateTime(),
                            resultSet.getString("location"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    public List<Event> getEventsByOwner(UUID ownerId) {
        String sql = "SELECT * FROM events WHERE ownerId = ?";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setObject(1, ownerId);

            createEventsFromSet(events, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<Event> getEventsInTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        String sql = "SELECT * FROM events where starTime >=? AND endTime <= ?";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));
            statement.setTimestamp(2, Timestamp.valueOf(endTime));

            createEventsFromSet(events, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    public List<Event> getEventsBefore(LocalDateTime endTime) {
        String sql = "SELECT * FROM events WHERE endTime <= ?";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(endTime));

            createEventsFromSet(events, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<Event> getEventsAfter(LocalDateTime startTime) {
        String sql = "SELECT * FROM events WHERE startTime >= ?";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(startTime));

            createEventsFromSet(events, statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }


    public void createEventsFromSet(List<Event> events,
                                    PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Event event = new Event(UUID.fromString(resultSet.getString("eventId")),
                        UUID.fromString(resultSet.getString("ownerId")),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("startTime").toLocalDateTime(),
                        resultSet.getTimestamp("endTime").toLocalDateTime(),
                        resultSet.getString("location"));

                events.add(event);
            }
        }
    }

    public void deleteEvent(UUID eventId) {
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
}
