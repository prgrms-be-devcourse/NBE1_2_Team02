package com.example.book_your_seat.service.coupon;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.coupon.service.CouponCommandServiceImpl;
import com.example.book_your_seat.coupon.facade.OptimisticLockCouponFacade;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.book_your_seat.coupon.domain.DiscountRate.FIVE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CouponCommandServiceImplTest extends IntegerTestSupport {
    @Autowired
    private CouponCommandServiceImpl couponCommandServiceImpl;

    @Autowired
    private OptimisticLockCouponFacade optimisticLockCouponFacade;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    private List<User> testUsers;
    private Coupon testCoupon;
    private final int THREAD_COUNT = 16;

    @BeforeEach
    public void setUpCoupon() {
        testUsers = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            testUsers.add(new User("nickname", "username", "test" + i + "@test.com", "passwordpassword"));
        }

        userRepository.saveAll(testUsers);
        testCoupon = couponRepository.saveAndFlush(new Coupon(100, FIVE));
    }

    @AfterEach
    public void tearDownCoupon() {
        userRepository.deleteAll();
        couponRepository.deleteAll();
        userCouponRepository.deleteAll();
    }

    @Test
    @DisplayName("비관적 락을 이용하여 동시에 100명이 쿠폰 발급을 요청한다.")
    public void issueCouponWithPessimisticTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(testUsers.size());

        long startTime = System.currentTimeMillis();
        for (User testUser : testUsers) {
            executorService.submit(() -> {
                try {
                    couponCommandServiceImpl.issueCouponWithPessimistic(testUser, testCoupon.getId());
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
    @DisplayName("낙관적 락을 이용하여 동시에 100명이 쿠폰 발급을 요청한다.")
    public void issueCouponWithOptimisticTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(testUsers.size());

        long startTime = System.currentTimeMillis();
        for (User testUser : testUsers) {
            executorService.submit(() -> {
                try {
                    optimisticLockCouponFacade.issueCouponWithOptimistic(testUser, testCoupon.getId());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
    @DisplayName("쿠폰을 한개 생성한다.")
    public void createCoupon() {
        //given
        CouponCreateRequest request = new CouponCreateRequest(100, FIVE);

        //when
        Long couponId = couponCommandServiceImpl.createCoupon(request).couponId();
        Coupon coupon = couponRepository.findById(couponId).get();

        //then
        assertEquals(100, coupon.getAmount());
        assertEquals(FIVE, coupon.getDiscountRate());
    }
}
