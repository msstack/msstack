package com.grydtech.msstack.eventstore.cassandra.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.eventstore.cassandra.domain.BookEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventRepository {

    private static final String TABLE_NAME = "events";
    private static final String TABLE_NAME_BY_TOPIC = TABLE_NAME + "by_topic";

    private Session session;

    public EventRepository(Session session) {
        this.session = session;
    }

    /**
     * Creates the events table.
     */
    public void createTable() {
        final String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + "uuid uuid PRIMARY KEY, " + "title text," + "topic text," + "message text);";
        session.execute(query);
    }

    /**
     * Creates the events tableByTopic.
     */
    public void createTableEventsByTopic() {
        final String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BY_TOPIC + "(" + "uuid uuid, " + "message text," + "topic text" + "PRIMARY KEY (uuid));";
        session.execute(query);
    }

    /**
     * Alters the events events and adds an extra column.
     */
    public void alterTableEvents(String columnName, String columnType) {
        final String query = "ALTER TABLE " + TABLE_NAME + " ADD " + columnName + " " + columnType + ";";
        session.execute(query);
    }

    /**
     * Insert a row in the table Events.
     *
     * @param event Event
     */
    public void insertEvent(Event event) {
        final String query = "INSERT INTO " + TABLE_NAME + "(uuid, topic, message) " + "VALUES (" + event.getId() + ", '" + event.getTopic() + "', '" + event.getPayload() +
                "');";
        session.execute(query);
    }

    /**
     * Insert a row in the table eventsByTopic.
     *
     * @param event Event
     */
    public void insertEventByTopic(Event event) {
        final String query = "INSERT INTO " + TABLE_NAME_BY_TOPIC + "(uuid, topic) " + "VALUES (" + event.getId() + ", '" + event.getTopic() + "');";
        session.execute(query);
    }

    /**
     * Insert event into two identical tables using a batch query.
     *
     * @param event Event
     */
    public void insertEventBatch(Event event) {
        final String query = "BEGIN BATCH " + "INSERT INTO " + TABLE_NAME + "(uuid, topic, message) " + "VALUES (" + event.getId() + ", '" + event.getTopic() + "', '" + event.getPayload() +
                "');" + "INSERT INTO " + TABLE_NAME_BY_TOPIC + "(uuid, topic) " + "VALUES (" + event.getId() + ", '" + event.getTopic() + "');" +
                "APPLY BATCH;";
        session.execute(query);
    }

    /**
     * Select event by uuid.
     *
     * @return Event
     */
    public Event selectEventByUUID(UUID uuid) {
        final String query = "SELECT * FROM " + TABLE_NAME_BY_TOPIC + " WHERE uuid = '" + uuid + "';";
        ResultSet rs = session.execute(query);
        List<Event> events = new ArrayList<>();

        for (Row r : rs) {
            BookEvent bookEvent = new BookEvent();
            bookEvent.setId(r.getUUID("id"));
            bookEvent.setPayload(r.getString("message"));
            events.add(bookEvent);
        }

        return events.get(0);
    }

    /**
     * Select all events from events
     *
     * @return List of Events
     */
    public List<Event> selectAll() {
        final String query = "SELECT * FROM " + TABLE_NAME;
        ResultSet rs = session.execute(query);
        List<Event> events = new ArrayList<>();

        for (Row r : rs) {
            BookEvent bookEvent = new BookEvent();
            bookEvent.setId(r.getUUID("uuid"));
            bookEvent.setPayload(r.getString("message"));
            events.add(bookEvent);
        }
        return events;
    }

    /**
     * Select all events from eventsByTopic
     *
     * @return List of Events
     */
    public List<Event> selectAllEventByTopic(String topic) {
        final String query = "SELECT * FROM " + TABLE_NAME_BY_TOPIC + " WHERE topic = '" + topic + "';";
        ResultSet rs = session.execute(query);
        List<Event> events = new ArrayList<>();

        for (Row r : rs) {
            BookEvent bookEvent = new BookEvent();
            bookEvent.setId(r.getUUID("uuid"));
            bookEvent.setPayload(r.getString(null));
            events.add(bookEvent);
        }
        return events;
    }

    /**
     * Delete a event by topic.
     */
    public void deleteEventByTopic(String topic) {
        final String query = "DELETE FROM " + TABLE_NAME_BY_TOPIC + " WHERE topic = '" + topic + "';";
        session.execute(query);
    }

    /**
     * Delete table.
     *
     * @param tableName the databaseName of the table to delete.
     */
    public void deleteTable(String tableName) {
        final String query = "DROP TABLE IF EXISTS " + tableName;
        session.execute(query);
    }
}
