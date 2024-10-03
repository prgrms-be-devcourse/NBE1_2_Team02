package com.example.book_your_seat.payment.service;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public interface PaymentQueryService {

    BigDecimal getOriginalPrice(Long concertId, int quantity);
    BigDecimal applyCoupon(BigDecimal originalPrice, Long userCouponId);

    User getUser(Long userId);

    Address getAddress(Long addressId);

    List<Seat> getSeats(Long concertId, List<Long> seatsId);

    Payment getPayment(UUID paymentId);
}
