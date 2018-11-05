package com.grydtech.msstack.eventstore.cassandra.domain;

import com.grydtech.msstack.core.types.Entity;
import lombok.Data;

import java.util.UUID;

@Data
public class Book implements Entity<UUID> {

    private UUID id;
    private String title;
    private String author;
    private String subject;
    private String publisher;

    public Book(UUID id, String title, String author, String subject) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
    }
}
