package com.example.book_your_seat.queue.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.book_your_seat.queue.QueueConst.*;

@Repository
@RequiredArgsConstructor
public class QueueRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();

    public void enqueueProcessingQueue(String token) {
        zSet.add(PROCESSING_QUEUE_KEY, token, System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME);
    }

    public void enqueueWaitingQueue(String token) {
        zSet.add(WAITING_QUEUE_KEY, token, System.currentTimeMillis());
    }

    public boolean isProcessableNow() {
        Long pqSize = zSet.zCard(PROCESSING_QUEUE_KEY);
        Long wqSize = zSet.zCard(WAITING_QUEUE_KEY);
        if (pqSize == null) pqSize = 0L;
        if (wqSize == null) wqSize = 0L;

        return pqSize < PROCESSING_QUEUE_SIZE && wqSize == 0;
    }

    public boolean isInProcessingQueue(String token) {
        return zSet.score(PROCESSING_QUEUE_KEY, token) != null;
    }

    public boolean isInWaitingQueue(String token) {
        return zSet.score(WAITING_QUEUE_KEY, token) != null;
    }

    public Integer getWaitingQueuePosition(String token) {
        Long rank = zSet.rank(WAITING_QUEUE_KEY, token);
        return (rank == null) ? null : rank.intValue() + 1;
    }

    public Integer getProcessingQueueCount() {
        Long count = zSet.zCard(PROCESSING_QUEUE_KEY);
        return (count == null) ? 0 : count.intValue();
    }

    public List<String> getFrontTokensFromWaitingQueue(int count) {
        Set<String> tokens = redisTemplate.opsForZSet().range(WAITING_QUEUE_KEY, 0, count - 1);

        if (tokens == null || tokens.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(tokens);
        }
    }

    public void updateWaitingToProcessing(String token) {
        zSet.remove(WAITING_QUEUE_KEY, token);
        zSet.add(PROCESSING_QUEUE_KEY, token, System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME);
    }

    public void removeTokenInWaitingQueue(String token) {
        zSet.remove(WAITING_QUEUE_KEY, token);
    }

    public void removeExpiredToken(long currentTime) {
        redisTemplate.opsForZSet().removeRangeByScore(PROCESSING_QUEUE_KEY, Double.NEGATIVE_INFINITY, (double) currentTime);
        redisTemplate.opsForZSet().removeRangeByScore(WAITING_QUEUE_KEY, Double.NEGATIVE_INFINITY, (double) currentTime);
    }

    public void removeProcessingToken(String token) {
        redisTemplate.opsForSet().remove(PROCESSING_QUEUE_KEY, token);
    }
}
