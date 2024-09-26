package com.example.book_your_seat.concurrent;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class NoLockTest extends IntegerTestSupport {

    @Autowired
    private CouponCommandService couponCommandService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void beforeEach() {
        User user = new User("nickname", "username", "email@email.com", "password123456789");
        userRepository.save(user);
        couponRepository.save(new Coupon(100, DiscountRate.FIVE));
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    void 동시에_100개_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponCommandService.issueCoupon(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime);
        Coupon stock = couponRepository.findById(1L).orElseThrow();

        Assertions.assertThat(stock.getAmount()).isEqualTo(0L);
    }


}
