package com.grydtech.msstack.eventstore.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.grydtech.msstack.eventstore.cassandra.domain.Event;
import com.grydtech.msstack.eventstore.cassandra.repository.BookRepository;
import com.grydtech.msstack.eventstore.cassandra.repository.KeyspaceRepository;
import org.apache.thrift.transport.TTransportException;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventStoreTest {

    private KeyspaceRepository schemaRepository;

    private BookRepository bookRepository;

    private Session session;
    
    private  CassandraDBConnector cassandraDBConnector;

    final String KEYSPACE_NAME = "testLibrary";
    final String BOOKS = "books";
    final String BOOKS_BY_TITLE = "booksByTitle";

    @Test
    public void whenCreatingATable_thenCreatedCorrectly() throws InterruptedException, IOException, TTransportException {
   
        CassandraDBConnector cassandraDBConnector = new CassandraDBConnector();
        cassandraDBConnector.putEvent(new Event(new UUID(5,2),"topic1", "message"));
    }


    @Test(expected = InvalidQueryException.class)
    public void whenDeletingATable_thenUnconfiguredTable() {
        bookRepository.createTable();
        bookRepository.deleteTable(BOOKS);

        session.execute("SELECT * FROM " + KEYSPACE_NAME + "." + BOOKS + ";");
    }

    @AfterClass
    public static void cleanup() {
       // EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
