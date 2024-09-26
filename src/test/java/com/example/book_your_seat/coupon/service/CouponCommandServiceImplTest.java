package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.service.facade.LockCouponLettuceFacade;
import com.example.book_your_seat.coupon.service.facade.LockCouponRedissonFacade;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class CouponCommandServiceImplTest {

    @Autowired
    private CouponCommandService couponCommandService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LockCouponLettuceFacade lockCouponFacade;

    @Autowired
    private LockCouponRedissonFacade lockCouponRedissonFacade;

    private User user;

    private Coupon coupon;


    @BeforeEach
    void setUp() {
        // given
        coupon = new Coupon(100, DiscountRate.FIFTEEN);

        user = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.save(user);

        couponRepository.save(coupon);
    }




    @Test
    @DisplayName("쿠폰 발급 테스트")
    @Transactional
    void coupon() throws Exception {
       //given
        couponCommandService.useCoupon(user.getId(), coupon.getId());

       //when
        entityManager.flush();
        entityManager.clear();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();

        //then
        Assertions.assertThat(findCoupon.getAmount()).isEqualTo(99);
    }

    @Test
    @DisplayName("쿠폰 발급 두번할때 예외처리")
    @Transactional
    public void couponException() throws Exception {
       //given
        couponCommandService.useCoupon(user.getId(), coupon.getId());


        //when
        entityManager.flush();
        entityManager.clear();

       //then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            couponCommandService.useCoupon(user.getId(), coupon.getId());
        });
    }


    @Test
    @DisplayName("동시성 테스트")
    public void synchronicityTest() throws InterruptedException {
       //given
        ExecutorService executorService = Executors.newFixedThreadPool(100); //스레드 풀 생성
        CountDownLatch countDownLatch = new CountDownLatch(100); // 다른 스레드에서 작업이 완료될떄 까지 대기할 수 있음

        //when

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try{
                    couponCommandService.useCoupon(user.getId(), coupon.getId());
                }finally {
                    countDownLatch.countDown();
                }
            });

        }

        countDownLatch.await();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        //then
        Assertions.assertThat(findCoupon.getAmount()).isZero();
    }



    @Test
    @DisplayName("Redis(Lettuce 동시성 테스트")
    public void LettuceTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(100); //스레드 풀 생성
        CountDownLatch countDownLatch = new CountDownLatch(100); // 다른 스레드에서 작업이 완료될떄 까지 대기할 수 있음

        //when

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try{
                    lockCouponFacade.useCoupon(user.getId(), coupon.getId());
                } catch (InterruptedException e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            });

        }

        countDownLatch.await();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        //then
        Assertions.assertThat(findCoupon.getAmount()).isZero();
    }

    @Test
    @DisplayName("Redis(Lettuce 동시성 테스트 초과 버전")
    public void LettuceLimitTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(101); //스레드 풀 생성
        CountDownLatch countDownLatch = new CountDownLatch(101); // 다른 스레드에서 작업이 완료될떄 까지 대기할 수 있음
        AtomicInteger exceptionCount = new AtomicInteger(0); // 예외 발생 카운트

        //when

        for (int i = 0; i < 101; i++) {
            executorService.submit(() -> {
                try{
                    lockCouponFacade.useCoupon(user.getId(), coupon.getId());
                }
                catch (IllegalArgumentException e) {
                    exceptionCount.incrementAndGet(); // 예외 발생 시 카운트
                }catch (InterruptedException e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            });

        }

        countDownLatch.await();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        //then
        Assertions.assertThat(findCoupon.getAmount()).isZero();
        Assertions.assertThat(exceptionCount.get()).isEqualTo(1); // 예외는 1번 발생해야 함 (101번째 요청
    }



    @Test
    @DisplayName("Redis(Redisson 동시성 테스트")
    public void RedissonTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(100); //스레드 풀 생성
        CountDownLatch countDownLatch = new CountDownLatch(100); // 다른 스레드에서 작업이 완료될떄 까지 대기할 수 있음

        //when

        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try{
                    lockCouponRedissonFacade.useCoupon(user.getId(), coupon.getId());
                } finally {
                    countDownLatch.countDown();
                }
            });

        }

        countDownLatch.await();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        //then
        Assertions.assertThat(findCoupon.getAmount()).isZero();
    }

    @Test
    @DisplayName("Redis(Redisson 동시성 초과 테스트")
    public void RedissonLimitTest() throws Exception {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(101); //스레드 풀 생성
        CountDownLatch countDownLatch = new CountDownLatch(101); // 다른 스레드에서 작업이 완료될떄 까지 대기할 수 있음
        AtomicInteger exceptionCount = new AtomicInteger(0); // 예외 발생 카운트

        //when

        for (int i = 0; i < 101; i++) {
            executorService.submit(() -> {
                try{
                    lockCouponRedissonFacade.useCoupon(user.getId(), coupon.getId());
                }
                catch (IllegalArgumentException e) {
                    exceptionCount.incrementAndGet(); // 예외 발생 시 카운트
                }finally {
                    countDownLatch.countDown();
                }
            });

        }

        countDownLatch.await();

        Coupon findCoupon = couponRepository.findById(coupon.getId()).get();
        //then
        Assertions.assertThat(findCoupon.getAmount()).isZero();
        Assertions.assertThat(exceptionCount.get()).isEqualTo(1); // 예외는 1번 발생해야 함 (101번째 요청
    }

}