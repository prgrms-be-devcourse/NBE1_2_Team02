package com.example.book_your_seat.coupon.facade;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.user.domain.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserCouponServiceImplTest{

    @Autowired
    private EntityManager em;

    @Autowired
    private UserCouponService userCouponService;

    @BeforeEach
    void beforeEach() {
        // 유저 저장
        User user = new User("nickname", "username", "email@email.com", "password123456789");
        em.persist(user);

        // 쿠폰 저장
        for(int i =0; i < 30; i++){
            Coupon coupon = new Coupon(100, DiscountRate.FIFTEEN, LocalDate.now());
            em.persist(coupon);

            UserCoupon userCoupon = new UserCoupon(user, coupon);

            if(i % 2 == 0){
                userCoupon.setUsed();
            }
            em.persist(userCoupon);

        }


        em.flush();
        em.clear();
    }


    @Test
    @DisplayName("동적쿼리를 활용한 페이징 처리(쿠폰 미사용)테스트")
    public void couponNotUsed() throws Exception {
       //given
        PageRequest pageRequest = PageRequest.of(0, 5);

        UserCouponRequest userCouponRequest = new UserCouponRequest(false);
        //when
        Slice<UserCouponResponse> userCoupons = userCouponService.getUserCoupons(userCouponRequest, 1L, pageRequest);

        System.out.println("userCoupons.getSize() = " + userCoupons.getSize());
        System.out.println("userCoupons.getContent() = " + userCoupons.getContent());
        //then
        for(UserCouponResponse userCouponResponse : userCoupons){
            System.out.println("userCouponResponse = " + userCouponResponse);
        }


        assertThat(userCoupons.getSize()).isEqualTo(5);
        assertThat(userCoupons.getContent().get(0).isUsed()).isFalse();
    }


    @Test
    @DisplayName("동적쿼리를 활용한 페이징 처리(쿠폰 사용)테스트")
    public void couponUsed() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 5);

        UserCouponRequest userCouponRequest = new UserCouponRequest(true);
        //when
        Slice<UserCouponResponse> userCoupons = userCouponService.getUserCoupons(userCouponRequest, 1L, pageRequest);

        //then
        for(UserCouponResponse userCouponResponse : userCoupons){
            System.out.println("userCouponResponse = " + userCouponResponse);
        }


        assertThat(userCoupons.getSize()).isEqualTo(5);
        assertThat(userCoupons.getContent().get(0).isUsed()).isTrue();
    }

    @Test
    @DisplayName("무한 스크롤로 다음 페이지 확인테스트")
    public void infiniteScrolling() throws Exception {
       //given
        UserCouponRequest userCouponRequest = new UserCouponRequest(true);

        PageRequest pageRequest1 = PageRequest.of(0, 5);  // 총 쿠폰 30장이고 사용한 한 쿠폰 15 이므로

        Slice<UserCouponResponse> userCoupons = userCouponService.getUserCoupons(userCouponRequest, 1L, pageRequest1);

        assertThat(userCoupons.getSize()).isEqualTo(5);
        assertThat(userCoupons.hasNext()).isTrue();
       //then

        PageRequest pageRequest2 = PageRequest.of(1, 20); //10장 남음
        Slice<UserCouponResponse> userCoupons2 = userCouponService.getUserCoupons(userCouponRequest, 1L, pageRequest2);

        assertThat(userCoupons2.hasNext()).isFalse();
    }



}