package com.grydtech.msstack.eventstore.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.grydtech.msstack.config.ConfigKey;
import com.grydtech.msstack.config.ConfigurationProperties;
import com.grydtech.msstack.core.connectors.eventstore.EventStoreConnector;
import com.grydtech.msstack.core.types.messaging.Event;
import com.grydtech.msstack.eventstore.cassandra.repository.EventRepository;
import com.grydtech.msstack.eventstore.cassandra.repository.KeyspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

;

/**
 * Created by dileka on 9/21/18.
 */
public final class CassandraDBConnector extends EventStoreConnector<UUID, Event> {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraDBConnector.class);

    private Cluster cluster;
    private Session session;
    private EventRepository eventRepository;

    @Override
    public void push(Event event) {
        eventRepository.insertEvent(event);
    }

    @Override
    public Event get(UUID uuid) {
        return eventRepository.selectEventByUUID(uuid);
    }

    @Override
    public List<Event> get(String topic) {
        List<? extends Event> events = eventRepository.selectAllEventByTopic(topic);
        return new ArrayList<>(events);
    }

    @Override
    public void connect() {
        // Configuration
        String dbName = ConfigurationProperties.get(ConfigKey.EVENTSTORE_NAME);
        String dbHost = ConfigurationProperties.get(ConfigKey.EVENTSTORE_HOST);
        int dbPort = Integer.parseInt(ConfigurationProperties.get(ConfigKey.EVENTSTORE_PORT));
        // Cluster
        this.cluster = Cluster.builder().withClusterName(dbName).addContactPoint(dbHost).withPort(dbPort).build();
        Metadata metadata = cluster.getMetadata();
        LOG.info("Cluster Name: " + metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            LOG.info("Data Center: " + host.getDatacenter() + "| Host: " + host.getAddress() + "| Rack: " + host.getRack());
        }
        // Connect
        this.session = cluster.connect();
        this.configureAfterConnect();
    }

    private void configureAfterConnect() {
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("eventStoreConfiguration", "SimpleStrategy", 2);
        sr.useKeyspace("library");

        eventRepository = new EventRepository(session);
        eventRepository.createTable();
        eventRepository.alterTableEvents("publisher", "text");
        eventRepository.createTableEventsByTopic();
    }

    @Override
    public void disconnect() {
        session.close();
        cluster.close();
    }
}
