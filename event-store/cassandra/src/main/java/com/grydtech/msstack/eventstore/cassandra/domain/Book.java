package com.grydtech.msstack.eventstore.cassandra.domain;

import com.grydtech.msstack.core.types.Entity;

import java.util.UUID;

public class Book implements Entity<UUID> {

    private UUID id;

    private String title;

    private String author;

    private String subject;

    private String publisher;

    Book() {
    }

    public Book(UUID id, String title, String author, String subject) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.subject = subject;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Book setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
