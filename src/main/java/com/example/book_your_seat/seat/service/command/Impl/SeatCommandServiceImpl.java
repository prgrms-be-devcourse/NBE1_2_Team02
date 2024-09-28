package com.example.book_your_seat.seat.service.command.Impl;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;
import com.example.book_your_seat.seat.domain.Seat;
import com.example.book_your_seat.seat.repository.SeatRepository;
import com.example.book_your_seat.seat.service.command.SeatCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.seat.SeatConst.SEAT_SOLD;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatCommandServiceImpl implements SeatCommandService {

    private final SeatRepository seatRepository;


    @Override
    public SelectSeatResponse selectSeat(SelectSeatRequest request) {
        //seat를 가져옴
        List<Seat> seats = seatRepository.findAllByIdWithLock(request.seatIds());

        //좌석 isSold = true로 변환
        seats.forEach(seat -> {
            if (seat.isSold()) {
                throw new IllegalArgumentException(SEAT_SOLD);
            }
            seat.selectSeat();
            });

        seatRepository.saveAll(seats);

        return SelectSeatResponse.fromSeats(seats);
    }
}
