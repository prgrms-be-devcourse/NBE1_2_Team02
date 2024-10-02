package com.example.book_your_seat.queue.service.facade;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.service.QueueCommandService;
import com.example.book_your_seat.queue.service.QueueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final QueueCommandService queueCommandService;
    private final QueueQueryService queueQueryService;

    @Override
    public TokenResponse issueTokenAndEnqueue(Long userId) {
        return new TokenResponse(queueCommandService.issueTokenAndEnqueue(userId));
    }

    @Override
    public void dequeueWaitingQueue(Long userId, String token) {
        queueCommandService.removeTokenInWaitingQueue(userId, token);
    }

    @Override
    @Transactional(readOnly = true)
    public QueueResponse findQueueStatus(Long userId, String token) {
        return queueQueryService.findQueueStatus(userId, token);
    }

    @Override
    public void updateWaitingToProcessing() {
        queueCommandService.updateWaitingToProcessing();
    }
}
