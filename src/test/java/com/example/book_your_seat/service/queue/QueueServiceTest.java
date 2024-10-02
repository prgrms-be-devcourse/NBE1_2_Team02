package com.example.book_your_seat.service.queue;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.facade.QueueCommandService;
import com.example.book_your_seat.queue.facade.QueueQueryService;
import com.example.book_your_seat.queue.manager.QueueManager;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import static com.example.book_your_seat.queue.QueueConst.*;
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
    private QueueManager queueManager;

    private User testUser;
    private ZSetOperations<String, String> zSet;

    @BeforeEach
    void beforeEach() {
        User user = new User("nickname", "username", "email@email.com", "password123456789");
        testUser = userRepository.save(user);
        zSet = redisTemplate.opsForZSet();
    }

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("user는 대기열에 중복 등록할 수 없다.")
    void issueTokenAndDuplicateEnqueueTest() {
        //given 한 번 등록
        queueCommandService.issueTokenAndEnqueue(testUser.getId());

        //when 한번 더 등록
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queueCommandService.issueTokenAndEnqueue(testUser.getId());
        });

        //then
        assertEquals(ALREADY_ISSUED_USER, exception.getMessage());
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 차지 않은 경우 진행열에 넣는다.")
    void issueTokenAndEnqueueProcessingQueueTest() {
        //when
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId()).token();

        //then
        String value = testUser.getId() + ":" + token;

        assertNotNull(zSet.score(PROCESSING_QUEUE_KEY, value));
        assertEquals(1L, zSet.zCard(PROCESSING_QUEUE_KEY));
    }

    @Test
    @DisplayName("토큰을 발급하고 진행열이 다 찬 경우 대기열에 넣는다.")
    void issueTokenAndEnqueueWaitingQueueTest() {
        //given
        for (int i = 0; i < 1000; i++) {
            queueCommandService.issueTokenAndEnqueue((long) i);
        }

        //when
        String token = queueCommandService.issueTokenAndEnqueue(1001L).token();

        //then
        String value = "1001:" + token;
        assertNotNull(zSet.score(WAITING_QUEUE_KEY, value));
        assertEquals(1, zSet.zCard(WAITING_QUEUE_KEY));
    }

    @Test
    @DisplayName("대기열을 탈출한다.")
    void dequeueWaitingQueueTest() {
        //given
        for (int i = 1000; i < 2100; i++) {
            queueCommandService.issueTokenAndEnqueue((long) i);
        }

        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId()).token();

        //when
        queueCommandService.dequeueWaitingQueue(testUser.getId(), token);

        //then
        String value = testUser.getId().toString() + ":" + token;
        assertNull(zSet.score(WAITING_QUEUE_KEY, value));
        assertEquals(100, zSet.zCard(WAITING_QUEUE_KEY));

    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(진행열에 들어온 상황)")
    void findQueueStatusTest1() {
        //given
        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId()).token();

        //when
        QueueResponse queueResponse = queueQueryService.findQueueStatus(testUser.getId(), token);

        //then
        assertEquals(PROCESSING, queueResponse.status());
        assertEquals(0, queueResponse.waitingQueueCount());
    }

    @Test
    @DisplayName("현재 나의 대기열 상태를 조회한다.(대기열에 있는 상황)")
    void findQueueStatusTest2() {
        //given
        for (int i = 1000; i < 2100; i++) {
            queueCommandService.issueTokenAndEnqueue((long) i);
        }

        String token = queueCommandService.issueTokenAndEnqueue(testUser.getId()).token();

        //when
        QueueResponse queueResponse = queueQueryService.findQueueStatus(testUser.getId(), token);

        //then
        assertEquals(WAITING, queueResponse.status());
        assertEquals(101, queueResponse.waitingQueueCount());
    }

    @Test
    @DisplayName("API 수행 완료 시 진행열에서 탈출한다.")
    void completeProcessingTokenTest() {
        //given
        for (int i = 1000; i < 1500; i++) {
            queueCommandService.issueTokenAndEnqueue((long) i);
        }
        queueCommandService.issueTokenAndEnqueue(testUser.getId());

        //when
        queueManager.completeProcessingToken(testUser.getId());

        //then
        assertEquals(400, zSet.zCard(PROCESSING_QUEUE_KEY));
    }

    @Test
    @DisplayName("스케줄러 실행 시 대기열 -> 진행열로 변환된다.")
    void updateWaitingToProcessingTest() {
        //given
        for (int i = 1000; i <= 2500; i++) {
            queueCommandService.issueTokenAndEnqueue((long) i);
        }

        queueCommandService.issueTokenAndEnqueue(testUser.getId());

        //when 500개 완료했을 때 스케줄러를 실행하면
        for (int i = 1000; i <= 1500; i++) {
            queueManager.completeProcessingToken((long) i);
        }

        queueManager.updateWaitingToProcessing();

        //then 대기열에서 진행열으로 500개 당겨져와야한다.
        assertEquals(zSet.zCard(PROCESSING_QUEUE_KEY),1000);
        assertEquals(zSet.zCard(WAITING_QUEUE_KEY),1);
    }
}
