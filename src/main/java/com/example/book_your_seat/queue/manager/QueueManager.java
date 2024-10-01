package com.example.book_your_seat.queue.manager;

import com.example.book_your_seat.common.JwtUtil;
import com.example.book_your_seat.queue.domain.QueueStatus;
import com.example.book_your_seat.queue.repository.RedisQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueManager {

    private final JwtUtil jwtUtil;
    private final RedisQueueRepository redisQueueRepository;

    public String enqueueToken(Long userId) {
        if (redisQueueRepository.alreadyEnQueued(userId)) {
            throw new IllegalArgumentException("이미 대기열에 있습니다.");
        }

        String token = jwtUtil.generateToken(userId);
        Long score = System.currentTimeMillis();

        redisQueueRepository.addToWaitingQueue(token, userId, score);
        return token;
    }

    public QueueStatus getQueueStatus(String token) {
        Long userIdFromToken = jwtUtil.getUserIdFromToken(token);

        if (redisQueueRepository.isProcessingQueue(token, userIdFromToken)) {
            return QueueStatus.PROCESSING;
        }
        if (redisQueueRepository.getWaitingQueuePosition(token, userIdFromToken.toString()) > 0L) {
            return QueueStatus.WAITING;
        }
        return QueueStatus.CANCELED;
    }

    public Long getPositionInWaitingStatus(String token) {
        Long userIdFromToken = jwtUtil.getUserIdFromToken(token);
        return redisQueueRepository.getWaitingQueuePosition(token, userIdFromToken.toString());
    }

    public long calculateEstimatedWaitSeconds(long position) {
        long batchSize = 1000L;
        long batchInterval = 60L * 5;
        long batches = position / batchSize;
        return batches * batchInterval;
    }

    public void removeToken(String token) {
        redisQueueRepository.removeWaitingToken(token);
        redisQueueRepository.removeProcessingToken(token);
    }
}
