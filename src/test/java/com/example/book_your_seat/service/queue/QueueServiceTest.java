package com.example.book_your_seat.service.queue;

import static org.assertj.core.api.Assertions.*;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.queue.controller.dto.QueueToken;
import com.example.book_your_seat.queue.manager.QueueManager;
import com.example.book_your_seat.queue.repository.RedisQueueRepository;
import com.example.book_your_seat.queue.service.QueueService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.manager.UserManager;
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
    private QueueManager queueManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    RedisQueueRepository redisQueueRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private User savedUser;

    @BeforeEach
    void beforeEach() {
        savedUser = userManager.createUser(new User("nickname", "username", "email@email.com", "passwordpassowrd"));
    }

    @AfterEach
    void afterEach() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("")
    void issueTokenTest() {
        // given
        Long userId = savedUser.getId();
        System.out.println(userId);

        // when
        QueueToken queueToken = queueService.issueQueueToken(userId);

        // then
        assertThat(queueToken).isNotNull();
    }

}
