package com.example.book_your_seat.queue.manager;

import com.example.book_your_seat.common.JwtUtil;
import com.example.book_your_seat.queue.repository.RedisQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueManager {

    private final JwtUtil jwtUtil;
    private final RedisQueueRepository redisQueueRepository;

    public String enqueueToken(Long userId) {
        String token = jwtUtil.generateToken(userId);
        Long score = System.currentTimeMillis();

        redisQueueRepository.addToWaitingQueue(token, score);
        return token;
    }

}
