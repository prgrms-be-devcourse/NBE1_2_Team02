package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.controller.dto.QueueToken;
import com.example.book_your_seat.queue.manager.QueueManager;
import com.example.book_your_seat.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueManager queueManager;
    private final UserManager userManager;

    public QueueToken issueQueueToken(Long userId) {

        userManager.checkUser(userId);

        return new QueueToken(queueManager.enqueueToken(userId));
    }
}
