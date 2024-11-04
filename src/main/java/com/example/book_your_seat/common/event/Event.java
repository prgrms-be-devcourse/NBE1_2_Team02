package com.example.book_your_seat.common.event;

import lombok.Getter;

@Getter
public abstract class Event {

    private long timestamp;

    public Event(long timestamp) {
        this.timestamp = System.currentTimeMillis();
    }
}
