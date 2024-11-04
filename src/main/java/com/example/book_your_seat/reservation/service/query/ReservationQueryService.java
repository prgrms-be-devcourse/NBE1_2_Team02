package com.example.book_your_seat.reservation.service.query;

import com.example.book_your_seat.payment.domain.Payment;
import com.example.book_your_seat.payment.repository.PaymentRepository;
import com.example.book_your_seat.reservation.contorller.dto.request.CancelReservationRequest;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.payment.PaymentConst.INVALID_PAYMENT;
import static com.example.book_your_seat.reservation.ReservationConst.RESERVATION_CAN_NOT_CANCEL;
import static com.example.book_your_seat.reservation.domain.ReservationStatus.ORDERED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationQueryService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public String validateReservation(final CancelReservationRequest request) {
        Reservation reservation = getValidatedReservation(request);

        return paymentRepository.findById(reservation.getPaymentId())
                .map(Payment::getPaymentKey)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_PAYMENT));
    }

    private Reservation getValidatedReservation(CancelReservationRequest request) {
        return reservationRepository.findById(request.ReservationId())
                .filter(r -> r.getStatus().equals(ORDERED))
                .orElseThrow(() -> new IllegalArgumentException(RESERVATION_CAN_NOT_CANCEL));
    }
}
