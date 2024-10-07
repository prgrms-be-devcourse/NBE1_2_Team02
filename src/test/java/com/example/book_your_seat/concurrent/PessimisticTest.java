package com.example.book_your_seat.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.book_your_seat.IntegralTestSupport;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.facade.CouponCommandFacade;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PessimisticTest extends IntegralTestSupport {
    @Autowired
    private CouponCommandFacade couponCommandServiceImpl;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    private List<User> testUsers;
    private Coupon testCoupon;
    private final int THREAD_COUNT = 32;

    @BeforeEach
    public void setUp() {
        testUsers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            testUsers.add(new User("nickname", "username", "test" + i + "@test.com", "passwordpassword"));
        }

        userRepository.saveAll(testUsers);
        testCoupon = couponRepository.saveAndFlush(new Coupon(100, DiscountRate.FIVE, LocalDate.of(2024,11,1)));
    }

    @AfterEach
    public void tearDown() {
        userCouponRepository.deleteAll();
        couponRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("비관적 락을 이용하여 동시에 100명이 쿠폰 발급을 요청한다.")
    public void issueCouponWithPessimisticTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(100);

        long startTime = System.currentTimeMillis();
        for (User testUser : testUsers) {
            executorService.submit(() -> {
                try {
                    couponCommandServiceImpl.issueCouponWithPessimistic(testUser.getId(), testCoupon.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long stopTime = System.currentTimeMillis();
        System.out.println(stopTime - startTime + "ms");

        Coupon updateCoupon = couponRepository.findById(testCoupon.getId()).orElseThrow();
        assertEquals(0, updateCoupon.getAmount());
    }

    @Test
    @DisplayName("비관적 락을 이용하여 동시에 101명이 쿠폰 발급을 요청하면 1명은 쿠폰을 받지 못한다.")
    public void issueCouponWithPessimisticFailTest() throws InterruptedException {
        User addUser = new User("nickname", "username", "test100"+ "@test.com", "passwordpassword");
        testUsers.add(addUser);
        userRepository.saveAndFlush(addUser);

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(101);

        for (User testUser : testUsers) {
            executorService.submit(() -> {
                try {
                    assertThrows(IllegalArgumentException.class, () ->  couponCommandServiceImpl.issueCouponWithPessimistic(testUser.getId(), testCoupon.getId()));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
    }


}
