package com.example.book_your_seat.queue.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void addToWaitingQueue(String token, Long score) {
        System.out.println("score = " + score);
        redisTemplate.opsForZSet().add("WAITING_QUEUE_KEY", token, score.doubleValue());
    }
}
