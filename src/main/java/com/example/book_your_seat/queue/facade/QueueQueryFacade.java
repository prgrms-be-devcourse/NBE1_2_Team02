package com.example.book_your_seat.queue.facade;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.manager.QueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueueQueryFacade implements QueueQueryService {
    private final QueueManager queueManager;

    @Override
    public QueueResponse findQueueStatus(Long userId, String token) {
        return queueManager.findQueueStatus(userId, token);
    }
}
