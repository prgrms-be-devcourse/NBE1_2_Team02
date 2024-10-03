package com.example.book_your_seat.service.payment;

import com.example.book_your_seat.IntegerTestSupport;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.manager.UserCouponManager;
import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import com.example.book_your_seat.seat.service.facade.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class PaymentServiceTest extends IntegerTestSupport {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private UserCouponManager userCouponManager;

    @MockBean
    private SeatService seatService;

    @Test
    @DisplayName("3333원 짜리 공연을 10%할인 쿠폰을 적용하여 최종금액 계산시 2999원")
    void getFinalPriceTest() {
        //given
        Long userCouponId = 1L;
        Long seatId = 1L;
        Integer seatPrice = 3333;
        DiscountRate discountRate = DiscountRate.TEN; // 10% discount

        UserCoupon mockUserCoupon = mock(UserCoupon.class);
        Coupon mockCoupon = mock(Coupon.class);

        when(userCouponManager.getUserCoupon(userCouponId)).thenReturn(mockUserCoupon);
        when(mockUserCoupon.isUsed()).thenReturn(false);
        when(mockUserCoupon.getCoupon()).thenReturn(mockCoupon);
        when(mockCoupon.getDiscountRate()).thenReturn(discountRate);
        when(seatService.getSeatPrice(seatId)).thenReturn(seatPrice);

        FinalPriceRequest request = new FinalPriceRequest(Collections.singletonList(seatId),userCouponId);

        // when
        FinalPriceResponse response = paymentService.getFinalPrice(request);

        // then
        assertThat(response.finalPrice(), is(BigDecimal.valueOf(2999)));
        verify(userCouponManager).getUserCoupon(userCouponId);
        verify(seatService).getSeatPrice(seatId);
    }

}