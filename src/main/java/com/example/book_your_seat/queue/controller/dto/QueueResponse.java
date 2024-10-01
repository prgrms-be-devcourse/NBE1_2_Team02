package com.example.book_your_seat.queue.controller.dto;

public record QueueResponse(
        String status,
        Integer waitingQueueCount
) {
}
