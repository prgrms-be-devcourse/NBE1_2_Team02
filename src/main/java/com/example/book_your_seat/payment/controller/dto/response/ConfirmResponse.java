package com.example.book_your_seat.payment.controller.dto.response;

import com.example.book_your_seat.reservation.domain.ReservationStatus;
import lombok.Builder;

import java.util.List;

public final class ConfirmResponse {

    private final Long userId;
    private final Long reservationId;

    private final Long concludePrice;
    private final ReservationStatus status;

    private final String concertTitle;
    private final int concertStartHour;
    private final List<Long> seatsId;


    @Builder
    private ConfirmResponse(Long userId,
                                Long reservationId,
                                Long concludePrice,
                                ReservationStatus status,
                                List<Long> seatsId,
                                String concertTitle,
                                int concertStartHour
    ) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.concludePrice = concludePrice;
        this.status = status;
        this.seatsId = seatsId;
        this.concertTitle = concertTitle;
        this.concertStartHour = concertStartHour;
    }
}