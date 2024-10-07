package com.example.book_your_seat.payment.service.dto;

import com.example.book_your_seat.payment.controller.dto.request.ReserveRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public final class ReserveCommand {


    private final Long concertId;
    private final Long userId;
    private final List<Long> seatsId;
    private final Long userCouponId;
    private final Long addressId;

    @Builder
    private ReserveCommand(Long concertId, Long userId, List<Long> seatsId, Long userCouponId, Long addressId) {
        this.concertId = concertId;
        this.userId = userId;
        this.seatsId = seatsId;
        this.userCouponId = userCouponId;
        this.addressId = addressId;
    }

    public static ReserveCommand from(Long concertId, Long userId, ReserveRequest request) {
        return ReserveCommand.builder()
                .concertId(concertId)
                .userId(userId)
                .seatsId(request.seatsId())
                .userCouponId(request.userCouponId())
                .addressId(request.addressId())
                .build();
    }
}
