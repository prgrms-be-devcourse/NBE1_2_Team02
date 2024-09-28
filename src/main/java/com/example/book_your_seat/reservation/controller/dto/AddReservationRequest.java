package com.example.book_your_seat.reservation.controller.dto;

import java.util.List;

public record AddReservationRequest(

        Long userId,
        Long concertId,
        Long userCouponId,
        Long addressId,
        List<Long> seats

) {

  /*  public static Reservation to(AddReservationRequest request) {
        return new Reservation(
                ReservationStatus.ORDERED,
                request.userCouponId(),
                request.concertId);
    }*/

}