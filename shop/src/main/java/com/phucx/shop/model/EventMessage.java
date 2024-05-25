package com.phucx.shop.model;

import com.phucx.shop.constant.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage<T> {
    private String eventId;
    private EventType eventType;
    private T payload;
}
