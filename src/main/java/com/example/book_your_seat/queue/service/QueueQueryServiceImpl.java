package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.queue.QueueConst.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QueueQueryServiceImpl implements QueueQueryService {
    public final QueueRedisRepository queueRedisRepository;

    /*
    유저의 현재 큐 상태 확인
     */
    public QueueResponse findQueueStatus(Long userId, String token) {
        if (queueRedisRepository.isInProcessingQueue(userId)) {
            return new QueueResponse(PROCESSING, 0);
        }

        Integer position = queueRedisRepository.getWaitingQueuePosition(userId, token);
        if (position != null) {
            return new QueueResponse(WAITING, position);
        } else {
            return new QueueResponse(NOT_IN_QUEUE, 0);
        }
    }
}
