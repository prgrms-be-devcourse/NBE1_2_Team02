package com.example.book_your_seat.queue.service;

import static com.example.book_your_seat.queue.QueueConst.ALLOWED_PROCESSING_SIZE;
import static com.example.book_your_seat.queue.QueueConst.FIFTEEN_MINUTE;
import static com.example.book_your_seat.queue.QueueConst.ZERO;

import com.example.book_your_seat.queue.repository.RedisQueueRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class QueueScheduler {

    private final RedisQueueRepository redisQueueRepository;

    @Scheduled(fixedRate = 30000)
    public void updateQueueStatus() {
        Long availableProcessingRoom = calculateAvailableProcessingRoom();
        if (availableProcessingRoom <= ZERO) return;

        Set<String> tokensNeedToUpdateToProcessing =
                redisQueueRepository.getWaitingQueueNeedToUpdateToProcessing(availableProcessingRoom.intValue());

        for (String token : tokensNeedToUpdateToProcessing) {
            try {
                redisQueueRepository.updateToProcessingQueue(
                        token,
                        System.currentTimeMillis() + FIFTEEN_MINUTE
                );
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private Long calculateAvailableProcessingRoom() {
        Long currentProcessingCount = redisQueueRepository.getProcessingQueueCount();
        return ALLOWED_PROCESSING_SIZE - currentProcessingCount;
    }

}
