package com.example.book_your_seat.queue.facade;

import com.example.book_your_seat.queue.controller.dto.TokenResponse;
import com.example.book_your_seat.queue.manager.QueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QueueCommandFacade implements QueueCommandService {
    private final QueueManager queueManager;

    //토큰 발급
    @Override
    public TokenResponse issueTokenAndEnqueue(Long userId) {
        return new TokenResponse(queueManager.issueTokenAndEnqueue(userId));
    }

    @Override
    public void dequeueWaitingQueue(String token) {
        queueManager.removeTokenInWaitingQueue(token);
    }
}
