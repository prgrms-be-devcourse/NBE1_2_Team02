package com.example.book_your_seat.reservation.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public final class ReservationsCommand {

    private final Long userId;
    private final Long reservationId;
    private final int pageSize;

    @Builder
    private ReservationsCommand(Long userId, Long reservationId, int pageSize) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.pageSize = pageSize;
    }

    public static ReservationsCommand from(Long userId, int pageSize, Long reservationId) {
        return ReservationsCommand.builder()
                .userId(userId)
                .reservationId(reservationId)
                .pageSize(pageSize)
                .build();
    }
}
