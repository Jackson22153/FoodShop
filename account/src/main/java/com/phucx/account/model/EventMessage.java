package com.phucx.account.model;

import com.phucx.account.constant.EventType;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class EventMessage<T> {
    private String eventId;
    private EventType eventType;
    private T payload;
}
