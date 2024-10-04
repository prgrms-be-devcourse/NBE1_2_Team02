package com.example.book_your_seat.payment.service;

import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.repository.UserCouponRepository;
import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.example.book_your_seat.common.constants.Constants.INVALID_ADDRESS;
import static com.example.book_your_seat.reservation.ReservationConst.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final UserCouponRepository userCouponRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public BigDecimal getOriginalPrice(Long concertId, int quantity) {
        return concertRepository.findById(concertId)
                .map(concert -> concert.getPrice() * quantity)
                .map(BigDecimal::new)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT));
    }

    @Override
    public BigDecimal applyCoupon(BigDecimal originalPrice, Long userCouponId) {
        if (userCouponId == null) {
            return originalPrice;
        }

        return userCouponRepository.findByIdAndIsUsed(userCouponId, false)
                .map(UserCoupon::getCoupon)
                .filter(coupon -> LocalDate.now().isBefore(coupon.getExpirationDate()))
                .map(Coupon::getDiscountRate)
                .map(discountRate -> discountRate.calculateDiscountedPrice(originalPrice))
                .orElse(originalPrice);
    }


    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_USER));
    }

    @Override
    public Address getAddress(Long addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_ADDRESS));
    }

    @Override
    public List<Seat> getSeats(List<Long> seatsId) {
        return seatRepository.findValidSeats(seatsId);
    }

    @Override
    public Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow();
    }
}
