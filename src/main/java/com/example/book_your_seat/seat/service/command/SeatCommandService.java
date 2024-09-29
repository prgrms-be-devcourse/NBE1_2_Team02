package com.example.book_your_seat.seat.service.command;

import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest;
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse;

public interface SeatCommandService {
    SelectSeatResponse selectSeat(final SelectSeatRequest request);
}
