package com.example.book_your_seat.service.queue;

import static org.assertj.core.api.Assertions.*;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.queue.controller.dto.QueueResponse;
import com.example.book_your_seat.queue.controller.dto.QueueToken;
import com.example.book_your_seat.queue.service.QueueScheduler;
import com.example.book_your_seat.queue.service.QueueService;
import com.example.book_your_seat.queue.util.QueueStatus;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class QueueServiceTest extends IntegerTestSupport {

    @Autowired
    private QueueService queueService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private QueueScheduler queueScheduler;

    private User savedUser;

    @BeforeEach
    void beforeEach() {
        savedUser = userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
        for (int i = 0; i < 1100; i++) {
            userRepository.save(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
        }
    }

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("대기열 진입을 시도하면 대기열에 저장되고, 토큰을 반환한다.")
    void issueTokenTest() {
        // given
        Long userId = savedUser.getId();

        // when
        QueueToken queueToken = queueService.issueQueueToken(userId);

        // then
        assertThat(queueToken).isNotNull();
    }

    @Test
    @DisplayName("processing queue가 가득 차기 전까지 processing queue에 저장된다.")
    void issueTokenInProcessingQueueTest() {
        //given
        Long userId = savedUser.getId();
        QueueToken queueToken = queueService.issueQueueToken(userId);

        //when
        QueueResponse queueResponse = queueService.findQueueByToken(queueToken.token());

        //then
        assertThat(queueResponse).isNotNull();
        assertThat(queueResponse.status()).isEqualTo(QueueStatus.PROCESSING);
        assertThat(queueResponse.remainingWaitingCount()).isEqualTo(0L);
        assertThat(queueResponse.estimatedWaitTime()).isEqualTo(0L);
    }

    @Test
    @DisplayName("processing queue가 가득 차면 waiting queue에 저장된다.")
    void issueTokenWaitingQueueTest() {
        //given
        List<User> all = userRepository.findAll();
        for (int i = 0; i < all.size() - 1; i++) {
            Long userId = all.get(i).getId();
            queueService.issueQueueToken(userId);
        }

        Long id = all.get(all.size() - 1).getId();
        QueueToken queueToken = queueService.issueQueueToken(id);

        //when
        QueueResponse queueResponse = queueService.findQueueByToken(queueToken.token());

        //then
        assertThat(queueResponse).isNotNull();
        assertThat(queueResponse.status()).isEqualTo(QueueStatus.WAITING);
        assertThat(queueResponse.remainingWaitingCount()).isEqualTo(100L);
        assertThat(queueResponse.estimatedWaitTime()).isEqualTo(0L);
    }

    @Test
    @DisplayName("토큰을 통해서 대기열에서 삭제할 수 있다")
    void deleteQueueByTokenTest() {
        //given
        Long userId = savedUser.getId();
        QueueToken queueToken = queueService.issueQueueToken(userId);

        //when
        queueService.deleteQueueToken(queueToken.token());

        QueueResponse queueResponse = queueService.findQueueByToken(queueToken.token());

        //then
        assertThat(queueResponse).isNotNull();
        assertThat(queueResponse.status()).isEqualTo(QueueStatus.CANCELED);
        assertThat(queueResponse.remainingWaitingCount()).isEqualTo(0L);
        assertThat(queueResponse.estimatedWaitTime()).isEqualTo(0L);
    }

    @Test
    @DisplayName("processing queue에 빈자리 만큼 waiting queue에서 채워준다.")
    void updateQueueTest() {
        //given
        List<User> all = userRepository.findAll();

        // 작업을 마치고 나갈 10명 기록
        List<QueueToken> tokens = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long userId = all.get(i).getId();
            tokens.add(queueService.issueQueueToken(userId));
        }

        for (int i = 10; i < all.size() - 1; i++) {
            Long userId = all.get(i).getId();
            queueService.issueQueueToken(userId);
        }

        // 마지막에 접근한 1명을 기록
        Long id = all.get(all.size() - 1).getId();
        QueueToken queueToken = queueService.issueQueueToken(id);

        //when
        QueueResponse queueResponse1 = queueService.findQueueByToken(queueToken.token());

        // 초반 10명을 processing queue에서 삭제
        for (QueueToken token : tokens) {
            queueService.deleteQueueToken(token.token());
        }
        queueScheduler.updateQueueStatus();

        QueueResponse queueResponse2 = queueService.findQueueByToken(queueToken.token());

        //then
        assertThat(queueResponse1).isNotNull();
        assertThat(queueResponse1.status()).isEqualTo(QueueStatus.WAITING);
        assertThat(queueResponse1.remainingWaitingCount()).isEqualTo(100L);
        assertThat(queueResponse1.estimatedWaitTime()).isEqualTo(0L);

        assertThat(queueResponse2).isNotNull();
        assertThat(queueResponse2.status()).isEqualTo(QueueStatus.WAITING);
        assertThat(queueResponse2.remainingWaitingCount()).isEqualTo(90L);
        assertThat(queueResponse2.estimatedWaitTime()).isEqualTo(0L);
    }

    @Test
    @DisplayName("processing queue 충분한 자라가 있으면 waiting queue에서 processing queue로 옮겨진다.")
    void updateQueueTest2() {
        //given
        List<User> all = userRepository.findAll();

        // 작업을 마치고 나갈 10명 기록
        List<QueueToken> tokens = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Long userId = all.get(i).getId();
            tokens.add(queueService.issueQueueToken(userId));
        }

        for (int i = 200; i < all.size() - 1; i++) {
            Long userId = all.get(i).getId();
            queueService.issueQueueToken(userId);
        }

        // 마지막에 접근한 1명을 기록
        Long id = all.get(all.size() - 1).getId();
        QueueToken queueToken = queueService.issueQueueToken(id);

        //when
        QueueResponse queueResponse1 = queueService.findQueueByToken(queueToken.token());

        // 초반 10명을 processing queue에서 삭제
        for (QueueToken token : tokens) {
            queueService.deleteQueueToken(token.token());
        }
        queueScheduler.updateQueueStatus();

        QueueResponse queueResponse2 = queueService.findQueueByToken(queueToken.token());

        //then
        assertThat(queueResponse1).isNotNull();
        assertThat(queueResponse1.status()).isEqualTo(QueueStatus.WAITING);
        assertThat(queueResponse1.remainingWaitingCount()).isEqualTo(100L);
        assertThat(queueResponse1.estimatedWaitTime()).isEqualTo(0L);

        assertThat(queueResponse2).isNotNull();
        assertThat(queueResponse2.status()).isEqualTo(QueueStatus.PROCESSING);
        assertThat(queueResponse2.remainingWaitingCount()).isEqualTo(0L);
        assertThat(queueResponse2.estimatedWaitTime()).isEqualTo(0L);
    }

}
