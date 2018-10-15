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
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(").append("uuid uuid PRIMARY KEY, ").append("title text,").append("topic text,").append("message text);");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Creates the events tableByTopic.
     */
    public void createTableEventsByTopic() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME_BY_TOPIC).append("(").append("uuid uuid, ").append("message text,").append("topic text").append("PRIMARY KEY (uuid));");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Alters the events events and adds an extra column.
     */
    public void alterTableEvents(String columnName, String columnType) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ").append(TABLE_NAME).append(" ADD ").append(columnName).append(" ").append(columnType).append(";");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Insert a row in the table Events.
     *
     * @param event
     */
    public void insertEvent(Event event) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(TABLE_NAME).append("(uuid, topic, message) ").append("VALUES (").append(event.getId()).append(", '").append(event.getTopic()).append("', '").append(event.getPayload())
                .append("');");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Insert a row in the table eventsByTopic.
     *
     * @param event
     */
    public void insertEventByTopic(Event event) {
        StringBuilder sb = new StringBuilder("INSERT INTO ").append(TABLE_NAME_BY_TOPIC).append("(uuid, topic) ").append("VALUES (").append(event.getId()).append(", '").append(event.getTopic()).append("');");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Insert event into two identical tables using a batch query.
     *
     * @param event
     */
    public void insertEventBatch(Event event) {
        StringBuilder sb = new StringBuilder("BEGIN BATCH ").append("INSERT INTO ").append(TABLE_NAME).append("(uuid, topic, message) ").append("VALUES (").append(event.getId()).append(", '").append(event.getTopic()).append("', '").append(event.getPayload())
                .append("');").append("INSERT INTO ").append(TABLE_NAME_BY_TOPIC).append("(uuid, topic) ").append("VALUES (").append(event.getId()).append(", '").append(event.getTopic()).append("');")
                .append("APPLY BATCH;");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Select event by uuid.
     *
     * @return
     */
    public Event selectEventByUUID(UUID uuid) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_BY_TOPIC).append(" WHERE uuid = '").append(uuid).append("';");

        final String query = sb.toString();

        ResultSet rs = session.execute(query);

        List<Event> events = new ArrayList<>();

        for (Row r : rs) {
            Event<String> s = new BookEvent()
                    .setId(r.getUUID("id"))
                    .setPayload(r.getString("message"));
            events.add(s);
        }

        return events.get(0);
    }

    /**
     * Select all events from events
     *
     * @return
     */
    public List<Event> selectAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME);

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Event> events = new ArrayList<Event>();

        for (Row r : rs) {
            Event<String> event = new BookEvent()
                    .setId(r.getUUID("uuid"))
                    .setPayload(r.getString("message"));
            events.add(event);
        }
        return events;
    }

    /**
     * Select all events from eventsByTopic
     *
     * @return
     */
    public List<Event> selectAllEventByTopic(String topic) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ").append(TABLE_NAME_BY_TOPIC).append(" WHERE topic = '").append(topic).append("';");
        ;

        final String query = sb.toString();
        ResultSet rs = session.execute(query);

        List<Event> events = new ArrayList<Event>();

        for (Row r : rs) {
            Event<String> event = new BookEvent()
                    .setId(r.getUUID("uuid"))
                    .setPayload(r.getString(null));
            events.add(event);
        }
        return events;
    }

    /**
     * Delete a event by topic.
     */
    public void deleteEventByTopic(String topic) {
        StringBuilder sb = new StringBuilder("DELETE FROM ").append(TABLE_NAME_BY_TOPIC).append(" WHERE topic = '").append(topic).append("';");

        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Delete table.
     *
     * @param tableName the name of the table to delete.
     */
    public void deleteTable(String tableName) {
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ").append(tableName);

        final String query = sb.toString();
        session.execute(query);
    }
}
