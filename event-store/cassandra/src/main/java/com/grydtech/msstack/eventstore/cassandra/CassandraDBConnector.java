package com.grydtech.msstack.eventstore.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.grydtech.msstack.core.BasicEvent;
import com.grydtech.msstack.core.eventstore.EventStoreConnector;
import com.grydtech.msstack.eventstore.cassandra.domain.Event;
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
public class CassandraDBConnector extends EventStoreConnector {
    
    private static final Logger LOG = LoggerFactory.getLogger(CassandraDBConnector.class);
    
    private Cluster cluster;
    
    private Session session;
    
    private EventRepository eventRepository;
    
    public CassandraDBConnector() {
        
        Builder b;
        if (applicationConfiguration.getEventStore().getEventStoreName() != null) {
            b = Cluster.builder().addContactPoint(applicationConfiguration.getEventStore().getNode());
        } else {
            b = Cluster.builder().addContactPoint("localhost");
        }
        
        if (applicationConfiguration.getDatabase().getPort() != null) {
            b.withPort(Integer.parseInt(applicationConfiguration.getEventStore().getPort()));
        }
        
        b.withPort(9142);
        cluster = b.build();
        
        Metadata metadata = cluster.getMetadata();
        LOG.info("Cluster name: " + metadata.getClusterName());
        
        for (Host host : metadata.getAllHosts()) {
            LOG.info("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " + host.getRack());
        }
        
        session = cluster.connect();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("eventStore", "SimpleStrategy", 1);
        sr.useKeyspace("library");
        
        eventRepository = new EventRepository(session);
        eventRepository.createTable();
        eventRepository.alterTableEvents("publisher", "text");
        
        eventRepository.createTableEventsByTopic();
    }
    
    @Override
    public void putEvent(BasicEvent basicEvent) {
        eventRepository.insertEventBatch((Event) basicEvent);
    }
    
    @Override
    public Event getEvent(UUID uuid) {
        return eventRepository.selectEventByUUID(uuid);
    }
    
    @Override
    public List<BasicEvent> getEventsByTopic(String topic) {
        List<Event> events = eventRepository.selectAllEventByTopic(topic);
        return new ArrayList<BasicEvent>(events);
    }
    
}
