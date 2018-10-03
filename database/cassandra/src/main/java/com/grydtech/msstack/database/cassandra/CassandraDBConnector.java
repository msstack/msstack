package com.grydtech.msstack.database.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.grydtech.msstack.core.database.DatabaseConnector;
import com.grydtech.msstack.database.cassandra.domain.Book;
import com.grydtech.msstack.database.cassandra.repository.BookRepository;
import com.grydtech.msstack.database.cassandra.repository.KeyspaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by dileka on 9/21/18.
 */
public class CassandraDBConnector extends DatabaseConnector<Book> {
    
    private static final Logger LOG = LoggerFactory.getLogger(CassandraDBConnector.class);
    
    private Cluster cluster;
    
    private Session session;
    
    private BookRepository bookRepository;
    
    public CassandraDBConnector() {
        Builder b = Cluster.builder().addContactPoint(applicationConfiguration.getDatabase().getNode());
        
        if (applicationConfiguration.getDatabase().getPort() != null) {
            b.withPort(Integer.parseInt(applicationConfiguration.getDatabase().getPort()));
        }
        cluster = b.build();
        
        Metadata metadata = cluster.getMetadata();
        LOG.info("Cluster name: " + metadata.getClusterName());
        
        for (Host host : metadata.getAllHosts()) {
            LOG.info("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " + host.getRack());
        }
        
        session = cluster.connect();
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("library");
    
        BookRepository br = new BookRepository(session);
        br.createTable();
        br.alterTablebooks("publisher", "text");
    
        br.createTableBooksByTitle();
    }
    
    @Override
    public void putKey(Book book) {
        Book bookNew = new Book(UUIDs.timeBased(), "Effective Java", "Joshua Bloch", "Programming");
        bookRepository.insertBookBatch(book);
    }
    
    @Override
    public Book getValue(String key) {
        return bookRepository.selectAllBookByTitle().get(0);
    }
    
    public List<Book> getValues(String key) {
        return bookRepository.selectAllBookByTitle();
        
    }
}
