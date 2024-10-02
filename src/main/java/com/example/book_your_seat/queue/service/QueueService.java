package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.QueueToken;
import com.example.book_your_seat.queue.util.QueueStatus;
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

    public QueueResponse findQueueByToken(String token) {
        QueueStatus status = queueManager.getQueueStatus(token);

        if (status == QueueStatus.PROCESSING) {
            return QueueResponse.processing();
        }

        if (status == QueueStatus.CANCELED) {
            return QueueResponse.cancel();
        }

        Long position = queueManager.getPositionInWaitingStatus(token);
        Long estimatedWaitTime = queueManager.calculateEstimatedWaitSeconds(position);

        return QueueResponse.waiting(position, estimatedWaitTime);
    }

    public void deleteQueueToken(String token) {
        QueueStatus status = queueManager.getQueueStatus(token);
        queueManager.removeToken(status, token);
    }
}
