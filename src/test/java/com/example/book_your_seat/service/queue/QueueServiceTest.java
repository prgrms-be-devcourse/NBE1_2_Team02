package com.example.book_your_seat.service.queue;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.repository.QueueRedisRepository;
import com.example.book_your_seat.queue.service.command.QueueCommandService;
import com.example.book_your_seat.queue.service.query.QueueQueryService;
import com.example.book_your_seat.queue.util.QueueJwtUtil;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;

import static com.example.book_your_seat.queue.QueueConst.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class QueueServiceTest extends IntegralTestSupport {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueueCommandService queueCommandService;

    @Autowired
    private QueueQueryService queueQueryService;

    @Autowired
    private QueueJwtUtil queueJwtUtil;

    private ZSetOperations<String, String> zSet;

    private User testUser;

    private static final Long CONCERT_ID = 1L;

    @BeforeEach
    void beforeEach() {
        zSet = redisTemplate.opsForZSet();
        User user = new User("nickname", "username", "email@email.com", "password123456789");
        testUser = userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 차지 않은 경우 진행열에 넣는다.")
    void issueTokenAndEnqueueProcessingQueueTest() {
        //when
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();

        //then
        String key = PROCESSING_QUEUE_KEY + ":" + CONCERT_ID;
        assertNotNull(zSet.score(key, token));
        assertEquals(1, zSet.zCard(key));
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 찬 경우 대기열에 넣는다.")
    void issueTokenAndEnqueueWaitingQueueTest() {
        //given
        for (int i = 0; i < 1000; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }

        //when
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();

        //then
        String key = WAITING_QUEUE_KEY + ":" + CONCERT_ID;
        assertNotNull(zSet.score(key, token));
        assertEquals(1, zSet.zCard(key));
    }

    @Test
    @DisplayName("대기열을 탈출한다.")
    void dequeueWaitingQueueTest() {
        //given
        for (int i = 0; i < 1100; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }

        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();

        //when
        queueCommandService.removeTokenInWaitingQueue(testUser.getId(), CONCERT_ID, token);

        //then
        String key = WAITING_QUEUE_KEY + ":" + CONCERT_ID;
        assertNull(zSet.score(key, token));
        assertEquals(100, zSet.zCard(key));

    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(진행열에 들어온 상황)")
    void findQueueStatusTest1() {
        //given
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();

        //when
        QueueResponse queueResponse = queueQueryService.findQueueStatus(CONCERT_ID, token);

        //then
        assertEquals(PROCESSING, queueResponse.status());
        assertEquals(0, queueResponse.waitingQueueCount());
    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(대기열에 있는 상황)")
    void findQueueStatusTest2() throws InterruptedException {
        //given
        for (int i = 0; i < 1010; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }

        Thread.sleep(10);
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();

        //when
        QueueResponse queueResponse = queueQueryService.findQueueStatus(CONCERT_ID, token);

        //then
        assertEquals(WAITING, queueResponse.status());
        assertEquals(11, queueResponse.waitingQueueCount());
    }

    @Test
    @DisplayName("API 수행 완료 시 진행열에서 탈출한다.")
    void completeProcessingTokenTest() {
        //given
        for (int i = 0; i < 500; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }

        //when
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID).token();


        String key = PROCESSING_QUEUE_KEY + ":" + CONCERT_ID;
        //then
        assertEquals(501, zSet.zCard(key));

        //when
        queueCommandService.removeTokenInProcessingQueue(testUser.getId(), CONCERT_ID, token);

        //then
        assertEquals(500, zSet.zCard(key));
    }

    @Test
    @DisplayName("스케줄러 실행 시 대기열 -> 진행열로 변환된다.")
    void updateWaitingToProcessingTest() {
        //given
        //처리큐에 들어갈 500명 기록
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            tokens.add(queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID).token());
        }

        for (int i = 500; i < 1500; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }
        queueCommandService.issueTokenAndEnqueue(testUser.getId(), CONCERT_ID);


        //when 500개 완료했을 때 스케줄러를 실행하면
        for (String token : tokens) {
            Long tokenUserId = queueJwtUtil.getUserIdByToken(token);
            queueCommandService.removeTokenInProcessingQueue(tokenUserId, CONCERT_ID, token);
        }

        queueCommandService.updateWaitingToProcessing();

        //then 대기열에서 진행열으로 500개 당겨져와야한다.
        assertEquals(zSet.zCard(PROCESSING_QUEUE_KEY + ":" + CONCERT_ID), 1000);
        assertEquals(zSet.zCard(WAITING_QUEUE_KEY + ":" + CONCERT_ID), 1);
    }

    @Test
    @DisplayName("processing queue에 빈자리 만큼 waiting queue에서 채워준다.")
    void updateQueueTest() {
        //given
        List<String> tokens = new ArrayList<>();

        // 작업을 마치고 나갈 10명 기록
        for (int i = 0; i < 10; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            tokens.add(queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID).token());
        }

        for (int i = 10; i < 1100 - 1; i++) {
            User savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
            queueCommandService.issueTokenAndEnqueue(savedUser.getId(), CONCERT_ID);
        }

        // 마지막에 접근한 1명을 기록
        Long lastUserId = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd")).getId();
        String lastUserToken = queueCommandService.issueTokenAndEnqueue(lastUserId, CONCERT_ID).token();

        //when
        QueueResponse queueResponse1 = queueQueryService.findQueueStatus(CONCERT_ID, lastUserToken);

        // 초반 10명을 processing queue에서 삭제
        for (String token : tokens) {
            Long tokenUserId = queueJwtUtil.getUserIdByToken(token);
            queueCommandService.removeTokenInProcessingQueue(tokenUserId, CONCERT_ID, token);
        }

        //update
        queueCommandService.updateWaitingToProcessing();
        QueueResponse queueResponse2 = queueQueryService.findQueueStatus(CONCERT_ID, lastUserToken);

        //then
        assertThat(queueResponse1).isNotNull();
        assertThat(queueResponse1.status()).isEqualTo(WAITING);
        assertThat(queueResponse1.waitingQueueCount()).isEqualTo(100L);
        assertThat(queueResponse1.estimatedWaitTime()).isEqualTo(0L);

        assertThat(queueResponse2).isNotNull();
        assertThat(queueResponse2.status()).isEqualTo(WAITING);
        assertThat(queueResponse2.waitingQueueCount()).isEqualTo(90L);
        assertThat(queueResponse2.estimatedWaitTime()).isEqualTo(0L);
    }

}
