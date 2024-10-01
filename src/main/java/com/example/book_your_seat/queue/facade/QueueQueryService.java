package com.example.book_your_seat.queue.facade;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;

public interface QueueQueryService {
    QueueResponse findQueueStatusByToken(String token);
}
