package com.example.book_your_seat.queue.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToWaitingQueue(String token, Long score) {
        redisTemplate.opsForZSet().add("WAITING_QUEUE", token, score.doubleValue());
    }

    public boolean isProcessingQueue(String token) {
        Double score = redisTemplate.opsForZSet().score("PROCESSING_QUEUE", token);
        return score != null;
    }

    public long getWaitingQueuePosition(String token, String userId) {
        Long rank = redisTemplate.opsForZSet().rank("WAITING_QUEUE", token);
        return rank != null ? rank : -1;
    }
}
