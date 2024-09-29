package com.example.book_your_seat.reservation.service.command.impl;

import com.example.book_your_seat.reservation.controller.dto.BookReservationDto;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.domain.ReservationStatus;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.reservation.service.command.ReservationCommandService;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.user.domain.Address;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.repository.AddressRepository;
import com.example.book_your_seat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.SEAT_NOT_FOUND;
import static com.example.book_your_seat.user.AddressConst.ADDRESS_NOT_FOUND;
import static com.example.book_your_seat.user.UserConst.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationCommandServicePessimisticLock implements ReservationCommandService {

    private final ReservationRepository reservationRepository;
    private final AddressRepository addressRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    @Override
    public Reservation bookReservation(final BookReservationDto dto) {
        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new IllegalArgumentException(ADDRESS_NOT_FOUND));

        List<Seat> seats = dto.seatIds().stream()
                .map(seatId -> seatRepository.findById(seatId)
                        .orElseThrow(() -> new IllegalArgumentException(SEAT_NOT_FOUND)))
                .toList();

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND));

        Reservation reservation = Reservation.builder()
                .address(address)
                .status(ReservationStatus.ORDERED)
                .userCouponId(dto.couponId())
                .finalPrice(dto.payment().getTotalPrice())
                .user(user)
                .payment(dto.payment())
                .build();

        seats.forEach(seat -> seat.assignReservation(reservation));

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservation;
    }
}
