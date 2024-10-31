package com.example.book_your_seat.queue.service.command;

import com.example.book_your_seat.queue.util.QueueJwtUtil;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book_your_seat.queue.QueueConst.*;

@Service
@RequiredArgsConstructor
public class QueueCommandService {

    private final QueueRedisRepository queueRedisRepository;
    public final QueueJwtUtil queueJwtUtil;

    /*
    토큰을 발급하고, Waiting Queue에 등록
     */

    public String issueTokenAndEnqueue(Long userId) {
        String token = queueJwtUtil.createJwt(userId);

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
    public void updateWaitingToProcessing() {
        int count = calculateAvailableProcessingCount();
        if (count == 0) return;

        List<String> values = queueRedisRepository.getFrontTokensFromWaitingQueue(count);
        values.forEach(queueRedisRepository::updateWaitingToProcessing);
    }

    /*
    스케줄러 실행 시 만료토큰 제거
     */

    public void removeExpiredToken() {
        queueRedisRepository.removeExpiredToken(System.currentTimeMillis());
    }

    /*
    대기열에서 토큰 삭제
     */

    public void removeTokenInWaitingQueue(Long userId, String token) {
        Long tokenUserId = queueJwtUtil.getUserIdByToken(token);
        if(userId.equals(tokenUserId))
            queueRedisRepository.removeTokenInWaitingQueue(userId, token);
        else throw new IllegalArgumentException(REMOVE_BAD_REQUEST);
    }

    /*
    처리열에서 토큰 삭제 (API 완료 시, 예외 발생 시)
   */

    public void removeTokenInProcessingQueue(Long userId, String token) {
        Long tokenUserId = queueJwtUtil.getUserIdByToken(token);
        if(userId.equals(tokenUserId))
            queueRedisRepository.removeTokenInProcessingQueue(userId, token);
        else throw new IllegalArgumentException(REMOVE_BAD_REQUEST);
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
