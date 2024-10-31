package com.example.book_your_seat.queue.service.facade;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.service.command.QueueCommandService;
import com.example.book_your_seat.queue.service.query.QueueQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueFacade {
    private final QueueCommandService queueCommandService;
    private final QueueQueryService queueQueryService;


    public TokenResponse issueTokenAndEnqueue(Long userId) {
        return new TokenResponse(queueCommandService.issueTokenAndEnqueue(userId));
    }


    public void dequeueWaitingQueue(Long userId, String token) {
        queueCommandService.removeTokenInWaitingQueue(userId, token);
    }


    public void dequeueProcessingQueue(Long userId, String token) {
        queueCommandService.removeTokenInProcessingQueue(userId, token);
    }



    public QueueResponse findQueueStatus(Long userId, String token) {
        return queueQueryService.findQueueStatus(userId, token);
    }

}
