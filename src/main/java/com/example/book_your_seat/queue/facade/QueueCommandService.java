package com.example.book_your_seat.queue.facade;

import com.example.book_your_seat.queue.controller.dto.TokenResponse;

public interface QueueCommandService {
    TokenResponse issueTokenAndEnqueue(Long userId);
    void dequeueWaitingQueue(Long userId, String token);
}
