package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.repository.RedisQueueRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final RedisQueueRepository redisQueueRepository;

    @Scheduled(fixedRate = 30000)
    public void updateQueueStatus() {
        Long availableProcessingRoom = calculateAvailableProcessingRoom();
        if (availableProcessingRoom <= 0) return;

        Set<String> tokensNeedToUpdateToProcessing =
                redisQueueRepository.getWaitingQueueNeedToUpdateToProcessing(availableProcessingRoom.intValue());

        for (String token : tokensNeedToUpdateToProcessing) {
            try {
                redisQueueRepository.updateToProcessingQueue(
                        token,
                        System.currentTimeMillis() + 900000
                );
            } catch (Exception e) {
                System.err.println("Error updating token to processing: " + e.getMessage());
            }
        }
    }

    private Long calculateAvailableProcessingRoom() {
        Long currentProcessingCount = redisQueueRepository.getProcessingQueueCount();
        return 1000 - currentProcessingCount;
    }

}
