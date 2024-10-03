package com.example.book_your_seat.queue.service;

import com.example.book_your_seat.common.util.JwtUtil;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book_your_seat.queue.QueueConst.ALREADY_ISSUED_USER;
import static com.example.book_your_seat.queue.QueueConst.PROCESSING_QUEUE_SIZE;

@Service
@RequiredArgsConstructor
public class QueueCommandServiceImpl implements QueueCommandService {
    private final QueueRedisRepository queueRedisRepository;
    public final JwtUtil jwtUtil;

    /*
    토큰을 발급하고, Waiting Queue에 등록
     */
    @Override
    public String issueTokenAndEnqueue(Long userId) {
        String token = jwtUtil.createJwt(userId);

        checkAlreadyIssuedUser(userId);

        //waiting queue가 비어있고, processing queue가 다 차지 않았으면 바로 processing queue에 넣어주기
        if (queueRedisRepository.isProcessableNow()) {
            queueRedisRepository.enqueueProcessingQueue(userId, token);
        } else {
            queueRedisRepository.enqueueWaitingQueue(userId, token);
        }

        return token;
    }

    /*
    Waiting Queue -> Processing Queue 로 이동시키기
     */
    @Override
    public void updateWaitingToProcessing() {
        int count = calculateAvailableProcessingCount();
        if (count == 0) return;

        List<String> values = queueRedisRepository.getFrontTokensFromWaitingQueue(count);
        values.forEach(queueRedisRepository::updateWaitingToProcessing);
    }

    /*
    스케줄러 실행 시 만료토큰 제거
     */
    @Override
    public void removeExpiredToken() {
        queueRedisRepository.removeExpiredToken(System.currentTimeMillis());
    }

    /*
    대기열에서 토큰 삭제
     */
    @Override
    public void removeTokenInWaitingQueue(Long userId, String token) {
        queueRedisRepository.removeTokenInWaitingQueue(userId, token);
    }

    /*
    처리열에서 토큰 삭제 (API 완료 시, 예외 발생 시)
   */
    @Override
    public void removeTokenInProcessingQueue(Long userId) {
        queueRedisRepository.removeProcessingToken(userId);
    }

    /*
    이미 토큰이 발급된 유저인지 확인
     */
    private void checkAlreadyIssuedUser(Long userId) {
        if (queueRedisRepository.isInWaitingQueue(userId)
                || queueRedisRepository.isInProcessingQueue(userId)) {
            throw new IllegalArgumentException(ALREADY_ISSUED_USER);
        }
    }

    /*
    update 가능한 queue Size 계산
     */
    private int calculateAvailableProcessingCount() {
        int size = queueRedisRepository.getProcessingQueueCount();
        return PROCESSING_QUEUE_SIZE - size;
    }
}
