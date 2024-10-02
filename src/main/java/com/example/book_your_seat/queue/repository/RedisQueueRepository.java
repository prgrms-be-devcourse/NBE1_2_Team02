package com.example.book_your_seat.queue.repository;

import static com.example.book_your_seat.queue.QueueConst.ALLOWED_PROCESSING_SIZE;
import static com.example.book_your_seat.queue.QueueConst.DELIMITER;
import static com.example.book_your_seat.queue.QueueConst.PROCESSING_QUEUE_KEY;
import static com.example.book_your_seat.queue.QueueConst.WAITING_QUEUE_KEY;
import static com.example.book_your_seat.queue.QueueConst.ZERO;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToWaitingQueue(String token, Long score) {
        if (haveSpaceInProcessingQueue()) {
            redisTemplate.opsForZSet().add(PROCESSING_QUEUE_KEY, token, score.doubleValue());
            return;
        }
        redisTemplate.opsForZSet().add(WAITING_QUEUE_KEY, token, score.doubleValue());
    }

    private boolean haveSpaceInProcessingQueue() {
        Long size = redisTemplate.opsForZSet().size(PROCESSING_QUEUE_KEY);
        if (size == null) {
            return true;
        }
        return size < ALLOWED_PROCESSING_SIZE;
    }

    public boolean isProcessingQueue(String token) {
        Double score = redisTemplate.opsForZSet().score(PROCESSING_QUEUE_KEY, token);
        return score != null;
    }

    public long getWaitingQueuePosition(String token) {
        Long rank = redisTemplate.opsForZSet().rank(WAITING_QUEUE_KEY, token);
        return rank != null ? rank : -1;
    }

    public boolean alreadyEnQueued(Long userId) {
        String pattern = DELIMITER + userId;
        return isUserInQueue(WAITING_QUEUE_KEY, pattern) || isUserInQueue(PROCESSING_QUEUE_KEY, pattern);
    }

    private boolean isUserInQueue(String queueName, String pattern) {
        Set<String> queueEntries = redisTemplate.opsForZSet().range(queueName, ZERO, -1);
        return queueEntries != null && queueEntries.stream().anyMatch(entry -> entry.endsWith(pattern));
    }

    public Set<String> getWaitingQueueNeedToUpdateToProcessing(int needToUpdateCount) {
        Set<String> range = redisTemplate
                .opsForZSet()
                .range(WAITING_QUEUE_KEY, ZERO, needToUpdateCount - 1);

        return range != null ? range : Collections.emptySet();
    }

    public void updateToProcessingQueue(String token, long expirationTime) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, token);
        redisTemplate.opsForZSet().add(PROCESSING_QUEUE_KEY, token, (double) expirationTime);
    }

    public Long getProcessingQueueCount() {
        Long size = redisTemplate.opsForZSet().size(PROCESSING_QUEUE_KEY);
        return size != null ? size : ZERO;
    }

    public void removeProcessingToken(String token) {
        redisTemplate.opsForZSet().remove(PROCESSING_QUEUE_KEY, token);
    }

    public void removeWaitingToken(String token) {
        redisTemplate.opsForZSet().remove(WAITING_QUEUE_KEY, token);
    }
}
