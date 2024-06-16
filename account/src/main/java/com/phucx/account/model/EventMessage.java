package com.phucx.account.model;

import com.phucx.account.constant.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage<T> {
    private String eventId;
    private EventType eventType;
    private T payload;
}
