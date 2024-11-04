package com.example.book_your_seat.payment.controller.dto.response;

import com.example.book_your_seat.reservation.domain.ReservationStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public final class ConfirmResponse {

    private final Long userId;
    private final Long reservationId;

    private final Long concludePrice;
    private final ReservationStatus status;

    private final String concertTitle;
    private final int concertStartHour;
    private final List<Long> seatNumbers;


    @Builder
    private ConfirmResponse(Long userId,
                            Long reservationId,
                            Long concludePrice,
                            ReservationStatus status,
                            String concertTitle,
                            int concertStartHour,
                            List<Long> seatNumbers
    ) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.concludePrice = concludePrice;
        this.status = status;
        this.concertTitle = concertTitle;
        this.concertStartHour = concertStartHour;
        this.seatNumbers = seatNumbers;
    }
}
