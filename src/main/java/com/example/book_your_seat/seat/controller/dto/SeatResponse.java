package com.example.book_your_seat.seat.controller.dto;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
public final class SeatResponse {

    private final Long seatId;
    private final Long concertId;
    private final boolean isSold;

    @Builder
    private SeatResponse(Long seatId, Long concertId, boolean isSold) {
        this.seatId = seatId;
        this.concertId = concertId;
        this.isSold = isSold;
    }

    public static SeatResponse from(Seat seat) {
        return SeatResponse.builder()
                .seatId(seat.getId())
                .concertId(seat.getConcert().getId())
                .isSold(seat.isSold())
                .build();
    }
}
