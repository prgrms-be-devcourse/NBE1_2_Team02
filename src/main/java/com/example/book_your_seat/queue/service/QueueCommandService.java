package com.example.book_your_seat.queue.service;

public interface QueueCommandService {
    String issueTokenAndEnqueue(Long userId);

    void updateWaitingToProcessing();

    void removeExpiredToken();

    void removeTokenInWaitingQueue(Long userId, String token);

    void removeTokenInProcessingQueue(Long userId, String token);
}
