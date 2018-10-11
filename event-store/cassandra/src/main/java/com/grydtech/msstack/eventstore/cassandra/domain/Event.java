package com.grydtech.msstack.eventstore.cassandra.domain;

import com.grydtech.msstack.core.BasicEvent;

import java.util.UUID;

public class Event extends BasicEvent {

    private UUID uuid;
    
    private String topic;
    
    private String message;

    public Event(UUID uuid, String topic, String message) {
        this.uuid = uuid;
        this.topic = topic; 
        this.message = message;
    }
    
    public UUID getUuid() {
        return uuid;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
