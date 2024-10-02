package com.example.book_your_seat.queue.service.facade;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;


public interface QueueService {
    TokenResponse issueTokenAndEnqueue(Long userId);
    void dequeueWaitingQueue(Long userId, String token);
    QueueResponse findQueueStatus(Long userId, String token);
    void updateWaitingToProcessing();
}
