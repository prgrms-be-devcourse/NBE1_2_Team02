package com.example.book_your_seat.payment.service.facade.impl;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.manager.UserCouponManager;
import com.example.book_your_seat.payment.controller.dto.FinalPriceRequest;
import com.example.book_your_seat.payment.controller.dto.FinalPriceResponse;
import com.example.book_your_seat.payment.controller.dto.PaymentDto;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmRequest;
import com.example.book_your_seat.payment.controller.dto.tossPayment.TossPaymentConfirmSuccessResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.service.command.PaymentCommandService;
import com.example.book_your_seat.payment.service.facade.PaymentService;
import com.example.book_your_seat.payment.service.tossPayment.TossPaymentService;
import com.example.book_your_seat.seat.service.facade.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CompletableFuture;

import static com.example.book_your_seat.coupon.CouponConst.COUPON_ALREADY_USED;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final UserCouponManager userCouponManager;
    private final SeatService seatService;
    private final TossPaymentService tossPaymentService;
    private final PaymentCommandService paymentCommandService;

    @Override
    @Transactional(readOnly = true)
    public FinalPriceResponse getFinalPrice(final FinalPriceRequest request) {
        UserCoupon userCoupon = userCouponManager.getUserCoupon(request.userCouponId());
        validateUserCoupon(userCoupon);

        Integer seatPrice = seatService.getSeatPrice(request.seatIds().get(0));

        DiscountRate discountRate = userCoupon.getCoupon().getDiscountRate();

        return new FinalPriceResponse(calculateDiscountPrice(seatPrice, discountRate));
    }

    @Override
    public CompletableFuture<TossPaymentConfirmSuccessResponse> requestConfirm(TossPaymentConfirmRequest request) {
        return tossPaymentService.requestConfirm(request);
    }

    @Override
    public Payment createPayment(PaymentDto paymentDto) {
        return paymentCommandService.createPayment(paymentDto);
    }

    private void validateUserCoupon(UserCoupon userCoupon) {
        if (userCoupon.isUsed()) {
            throw new IllegalArgumentException(COUPON_ALREADY_USED);
        }
    }

    private BigDecimal calculateDiscountPrice(Integer seatPrice, DiscountRate discountRate) {
        return BigDecimal.valueOf(seatPrice)
                .multiply(BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(discountRate.getValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                ))
                .setScale(0, RoundingMode.DOWN);
    }
}
