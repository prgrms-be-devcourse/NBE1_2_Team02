package com.example.book_your_seat.queue.repository;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.book_your_seat.queue.QueueConst.*;

@Repository
@RequiredArgsConstructor
public class QueueRedisRepository implements Serializable {

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSet;

    // userId와 token을 함께 저장
    public void enqueueProcessingQueue(Long userId, String token) {
        String value = generateValue(userId, token);
        zSet.add(PROCESSING_QUEUE_KEY, value, System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME);
    }

    public void enqueueWaitingQueue(Long userId, String token) {
        String value = generateValue(userId, token);
        zSet.add(WAITING_QUEUE_KEY, value, System.currentTimeMillis() + WAITING_TOKEN_EXPIRATION_TIME);
    }

    public boolean isProcessableNow() {
        Long pqSize = zSet.zCard(PROCESSING_QUEUE_KEY);
        Long wqSize = zSet.zCard(WAITING_QUEUE_KEY);
        if (pqSize == null) pqSize = 0L;
        if (wqSize == null) wqSize = 0L;

        return pqSize < PROCESSING_QUEUE_SIZE && wqSize == 0;
    }

    public boolean isInProcessingQueue(Long userId) {
        Set<String> values = zSet.range(PROCESSING_QUEUE_KEY, 0, -1);
        return values.stream().anyMatch(
                value -> value.matches(userId + ":.*")
        );
    }

    public boolean isInWaitingQueue(Long userId) {
        Set<String> values = zSet.range(WAITING_QUEUE_KEY, 0, -1);
        return values.stream().anyMatch(
                value -> value.matches(userId + ":.*")
        );
    }

    public Integer getWaitingQueuePosition(Long userId, String token) {
        String value = generateValue(userId, token);
        Long rank = zSet.rank(WAITING_QUEUE_KEY, value);
        return (rank == null) ? null : rank.intValue() + 1;
    }

    public Integer getProcessingQueueCount() {
        Long count = zSet.zCard(PROCESSING_QUEUE_KEY);
        return (count == null) ? 0 : count.intValue();
    }

    public List<String> getFrontTokensFromWaitingQueue(int count) {
        Set<String> tokens = zSet.range(WAITING_QUEUE_KEY, 0, count - 1);

        if (tokens == null || tokens.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(tokens);
        }
    }

    // userId와 token을 함께 처리하여 대기열에서 처리열로 이동
    public void updateWaitingToProcessing(String value) {
        zSet.remove(WAITING_QUEUE_KEY, value);
        zSet.add(PROCESSING_QUEUE_KEY, value, System.currentTimeMillis() + PROCESSING_TOKEN_EXPIRATION_TIME);
    }

    public void removeTokenInWaitingQueue(Long userId, String token) {
        String value = generateValue(userId, token);
        zSet.remove(WAITING_QUEUE_KEY, value);
    }

    public void removeExpiredToken(Long currentTime) {
        zSet.removeRangeByScore(PROCESSING_QUEUE_KEY, Double.NEGATIVE_INFINITY, (double) currentTime);
        zSet.removeRangeByScore(WAITING_QUEUE_KEY, Double.NEGATIVE_INFINITY, (double) currentTime);
    }

    public void removeProcessingToken(Long userId) {
        Set<String> values = zSet.range(PROCESSING_QUEUE_KEY, 0, -1);

        values.stream()
                .filter(value -> value.matches(userId + ":.*"))
                .findFirst()
                .ifPresent(value -> zSet.remove(PROCESSING_QUEUE_KEY, value));
    }

    // userId와 token을 조합한 키 생성 메소드
    private String generateValue(Long userId, String token) {
        return userId.toString() + ":" + token;
    }
}
