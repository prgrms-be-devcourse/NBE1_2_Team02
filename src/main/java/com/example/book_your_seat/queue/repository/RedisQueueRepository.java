package com.example.book_your_seat.queue.repository;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToWaitingQueue(String token, Long userId, Long score) {

        System.out.println("token + \":\" + userId = " + token + ":" + userId);

        if (haveSpaceInProcessingQueue()) {
            redisTemplate.opsForZSet().add("PROCESSING_QUEUE", token + ":" + userId, score.doubleValue());
            return;
        }
        redisTemplate.opsForZSet().add("WAITING_QUEUE", token + ":" + userId, score.doubleValue());
    }

    private boolean haveSpaceInProcessingQueue() {
        Long size = redisTemplate.opsForZSet().size("PROCESSING_QUEUE");
        if (size == null) {
            return true;
        }
        return size < 1000L;
    }

    public boolean isProcessingQueue(String token, Long userId) {
        Double score = redisTemplate.opsForZSet().score("PROCESSING_QUEUE", token + ":" + userId);
        return score != null;
    }

    public long getWaitingQueuePosition(String token, String userId) {
        Long rank = redisTemplate.opsForZSet().rank("WAITING_QUEUE", token + ":" + userId);
        return rank != null ? rank : -1;
    }

    public boolean alreadyEnQueued(Long userId) {
        String pattern = ":" + userId;
        return isUserInQueue("WAITING_QUEUE", pattern) || isUserInQueue("PROCESSING_QUEUE", pattern);
    }

    private boolean isUserInQueue(String queueName, String pattern) {
        Set<String> queueEntries = redisTemplate.opsForZSet().range(queueName, 0, -1);
        return queueEntries != null && queueEntries.stream().anyMatch(entry -> entry.endsWith(pattern));
    }

    public Set<String> getWaitingQueueNeedToUpdateToProcessing(int needToUpdateCount) {
        Set<String> range = redisTemplate
                .opsForZSet()
                .range("WAITING_QUEUE", 0, needToUpdateCount - 1);

        return range != null ? range : Collections.emptySet();
    }

    public void updateToProcessingQueue(String token, long expirationTime) {
        redisTemplate.opsForZSet().remove("WAITING_QUEUE", token);
        redisTemplate.opsForZSet().add("PROCESSING_QUEUE", token, (double) expirationTime);
    }

    public Long getProcessingQueueCount() {
        Long size = redisTemplate.opsForZSet().size("PROCESSING_QUEUE");
        return size != null ? size : 0L;
    }
}
