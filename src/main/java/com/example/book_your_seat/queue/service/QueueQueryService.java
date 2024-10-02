package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;

public interface QueueQueryService {
    QueueResponse findQueueStatus(Long userId, String token);

}
