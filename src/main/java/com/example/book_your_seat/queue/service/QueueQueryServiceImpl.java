package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.book_your_seat.queue.QueueConst.*;

@Service
@RequiredArgsConstructor
public class QueueQueryServiceImpl implements QueueQueryService {
    public final QueueRedisRepository queueRedisRepository;

    /*
    유저의 현재 큐 상태 확인
     */
    public QueueResponse findQueueStatus(Long userId, String token) {
        if (queueRedisRepository.isInProcessingQueue(userId)) {
            return QueueResponse.processing();
        }

        Integer position = queueRedisRepository.getWaitingQueuePosition(userId, token);
        if (position != null) {
            return QueueResponse.waiting(position, calculateEstimatedWaitSeconds(position));
        } else {
            return QueueResponse.notInQueue();
        }
    }

    private Integer calculateEstimatedWaitSeconds(int position) {
        int batchSize = PROCESSING_QUEUE_SIZE;
        int batchInterval = FIVE * MINUTE;
        int batches = position / batchSize;
        return batches * batchInterval;
    }
}
