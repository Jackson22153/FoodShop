package com.phucx.model;

import java.io.Serializable;

import com.phucx.constant.EventType;

public class EventMessage<T> implements Serializable{
    private String eventId;
    private EventType eventType;
    private T payload;
    private String errorMessage;
    public EventMessage(String eventId, EventType eventType, T payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
    }
    public EventMessage(String eventId, EventType eventType, T payload, String errorMessage) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.errorMessage = errorMessage;
    }
    public EventMessage() {
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public EventType getEventType() {
        return eventType;
    }
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public T getPayload() {
        return payload;
    }
    public void setPayload(T payload) {
        this.payload = payload;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
