package com.example.book_your_seat.queue.manager;

import com.example.book_your_seat.common.util.JwtUtil;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.book_your_seat.queue.QueueConst.*;


@Component
@RequiredArgsConstructor
public class QueueManager {
    public final QueueRedisRepository queueRedisRepository;
    public final JwtUtil jwtUtil;


    /*
    토큰을 발급하고, Waiting Queue에 등록
     */
    public String issueTokenAndEnqueue(Long userId) {
        String token = jwtUtil.createJwt(userId);

        checkAlreadyIssuedUser(token);

        //waiting queue가 비어있고, processing queue가 다 차지 않았으면 바로 processing queue에 넣어주기
        if (queueRedisRepository.isProcessableNow()) {
            queueRedisRepository.enqueueProcessingQueue(token);
        } else {
            queueRedisRepository.enqueueWaitingQueue(token);
        }

        return token;
    }

    /*
    유저의 현재 큐 상태 확인
     */
    public QueueResponse findQueueStatusByToken(String token) {
        if (queueRedisRepository.isInProcessingQueue(token)) {
            return new QueueResponse(PROCESSING, 0);
        }

        Integer position = queueRedisRepository.getWaitingQueuePosition(token);
        if (position != null) {
            return new QueueResponse(WAITING, position);
        } else {
            return new QueueResponse(NOT_IN_QUEUE, 0);
        }
    }

    /*
    update 가능한 queue Size 계산
     */
    public int calculateAvailableProcessingCount() {
        int size = queueRedisRepository.getProcessingQueueCount();
        return PROCESSING_QUEUE_SIZE - size;
    }

    /*
    Waiting Queue -> Processing Queue 로 이동시키기
     */
    public void updateWaitingToProcessing() {
        int count = calculateAvailableProcessingCount();
        if (count == 0) return;

        List<String> tokens = queueRedisRepository.getFrontTokensFromWaitingQueue(count);
        tokens.forEach(queueRedisRepository::updateWaitingToProcessing);
    }

    /*
    스케줄러 실행 시 만료토큰 제거
     */
    public void removeExpiredToken() {
        queueRedisRepository.removeExpiredToken(System.currentTimeMillis());
    }

    /*
    웨이팅 큐에서 삭제
     */
    public void removeTokenInWaitingQueue(String token) {
        queueRedisRepository.removeTokenInWaitingQueue(token);
    }

    /*
    진행 완료된 토큰 제거
     */
    public void completeProcessingToken(String token) {
        queueRedisRepository.removeProcessingToken(token);
    }

    /*
    이미 토큰이 발급된 유저인지 확인
     */
    private void checkAlreadyIssuedUser(String token) {
        if (queueRedisRepository.isInWaitingQueue(token)
                || queueRedisRepository.isInProcessingQueue(token)) {
            throw new IllegalArgumentException(ALREADY_ISSUED_USER);
        }
    }
}
