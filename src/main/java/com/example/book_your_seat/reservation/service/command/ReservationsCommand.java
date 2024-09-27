package com.example.book_your_seat.reservation.service.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public final class ReservationsCommand {

    private final Long userId;
    private final Long reservationId;
    private final int pageSize;

    private ReservationsCommand(Long userId, Long reservationId, int pageSize) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.pageSize = pageSize;
    }

    public static ReservationsCommand from(Long userId, int pageSize, Long reservationId) {
        return new ReservationsCommand(userId, reservationId, pageSize);
    }
}
