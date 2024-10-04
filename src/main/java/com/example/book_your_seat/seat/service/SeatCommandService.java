package com.example.book_your_seat.seat.service;

import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatCommandService {

    void selectSeats(SelectSeatsCommand command);


    void cancelSeats(List<Long> seatsId);
}
