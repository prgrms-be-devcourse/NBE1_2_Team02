package com.example.book_your_seat.payment.service.facade;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import com.example.book_your_seat.coupon.facade.CouponCommandService;
import com.example.book_your_seat.coupon.facade.CouponQueryService;
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse;
import com.example.book_your_seat.payment.controller.dto.response.ReserveResponse;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.service.PaymentCommandService;
import com.example.book_your_seat.payment.service.PaymentQueryService;
import com.example.book_your_seat.payment.service.dto.ReserveCommand;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.ReservationQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_PAYMENT;


@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    private final ReservationQueryService reservationQueryService;

    private final CouponQueryService couponQueryService;
    private final CouponCommandService couponCommandService;



    @Override
    public Payment findByPaymentId(String paymentId) {
        return paymentQueryService.getPayment(UUID.fromString(paymentId));
    }

    @Override
    public ReserveResponse reserve(ReserveCommand command) {

        Long concertId = command.getConcertId();
        List<Long> seatsId = command.getSeatsId();
        Long userCouponId = command.getUserCouponId();

        Long concludedPrice = getConcludedPrice(concertId, userCouponId, seatsId);
        UUID paymentId = saveDraft(concludedPrice, command);
        return new ReserveResponse(paymentId, concludedPrice);
    }


    private Long getConcludedPrice(Long concertId, Long userCouponId, List<Long> seatsId) {
        BigDecimal originalPrice = paymentQueryService.getOriginalPrice(concertId, seatsId.size());
        BigDecimal concludedPrice = paymentQueryService.applyCoupon(originalPrice, userCouponId);
        return concludedPrice.longValue();
    }


    private UUID saveDraft(Long concludedPrice, ReserveCommand command) {
        Payment payment = createPaymentDraft(concludedPrice, command);
        Payment savedPayment = paymentCommandService.savePayment(payment);

        Reservation reservation = reservationQueryService.findReservationBySeatsId(command.getSeatsId());
        reservation.addPayment(savedPayment);
        return savedPayment.getId();
    }


    private Payment createPaymentDraft(Long concludedPrice, ReserveCommand command) {
        CouponDetailResponse couponResponse
                = couponQueryService.getCouponDetailById(command.getUserCouponId());

        return Payment.builder()
                .totalPrice(concludedPrice)
                .userCouponId(command.getUserCouponId())
                .discountRate(couponResponse.discountRate())
                .build();
    }

    @Override
    public void processPayment(String paymentKey, String orderId, Long amount) {

        Payment payment = paymentQueryService.getPayment(UUID.fromString(orderId));
        validateAmount(amount, payment);

        couponCommandService.useUserCoupon(payment.getUserCouponId());
        payment.setAdditionalInfo();

    }

    private static void validateAmount(Long amount, Payment payment) {
        Long paymentPrice = payment.getTotalPrice();
        if (!paymentPrice.equals(amount)) {
            throw new IllegalArgumentException(INVALID_PAYMENT);
        }
    }
}
