package com.example.book_your_seat.queue.manager;

import static com.example.book_your_seat.queue.QueueConst.ALLOWED_PROCESSING_SIZE;
import static com.example.book_your_seat.queue.QueueConst.ALREADY_IN_QUEUE_KEY;
import static com.example.book_your_seat.queue.QueueConst.FIVE;
import static com.example.book_your_seat.queue.QueueConst.MINUTE;
import static com.example.book_your_seat.queue.QueueConst.ZERO;

import com.example.book_your_seat.common.JwtUtil;
import com.example.book_your_seat.queue.util.QueueStatus;
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
            throw new IllegalArgumentException(ALREADY_IN_QUEUE_KEY);
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
        if (redisQueueRepository.getWaitingQueuePosition(token, userIdFromToken) > ZERO) {
            return QueueStatus.WAITING;
        }
        return QueueStatus.CANCELED;
    }

    public Long getPositionInWaitingStatus(String token) {
        Long userIdFromToken = jwtUtil.getUserIdFromToken(token);
        return redisQueueRepository.getWaitingQueuePosition(token, userIdFromToken);
    }

    public long calculateEstimatedWaitSeconds(long position) {
        long batchSize = ALLOWED_PROCESSING_SIZE;
        long batchInterval = FIVE * MINUTE;
        long batches = position / batchSize;
        return batches * batchInterval;
    }

    public void removeToken(String token) {
        redisQueueRepository.removeWaitingToken(token);
        redisQueueRepository.removeProcessingToken(token);
    }
}
