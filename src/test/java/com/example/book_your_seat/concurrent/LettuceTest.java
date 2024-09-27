package com.example.book_your_seat.concurrent;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.redis.service.LockCouponLettuceFacade;
import com.example.book_your_seat.coupon.redis.service.LockCouponRedissonFacade;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import com.example.book_your_seat.coupon.service.CouponCommandService;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LettuceTest {


    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private Coupon coupon;

    @Autowired
    private LockCouponLettuceFacade lockCouponFacade;




    @BeforeEach
    void setUp() {

        coupon  =  new Coupon(100, DiscountRate.FIVE, LocalDate.of(2024, 11, 1).plusMonths(12));
        // given
        couponRepository.save(coupon);

        user = new User("nickname", "username", "test@test.com", "passwordpassword");
        userRepository.save(user);

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



}
