package com.example.book_your_seat.seat.service.dto;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public final class SelectSeatsCommand {


    private final Long userId;
    private final Long addressId;
    private final Long concertId;
    private final List<Long> seatsId;

    @Builder
    private SelectSeatsCommand(Long userId, Long addressId, Long concertId, List<Long> seatsId) {
        this.userId = userId;
        this.addressId = addressId;
        this.concertId = concertId;
        this.seatsId = seatsId;
    }

    public static SelectSeatsCommand from(Long concertId, Long userId, SelectSeatRequest request) {
        return SelectSeatsCommand.builder()
                .concertId(concertId)
                .userId(userId)
//                .addressId(request.addressId())
                .seatsId(request.seatIds())
                .build();
    }
}
