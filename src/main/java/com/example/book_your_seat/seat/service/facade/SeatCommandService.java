package com.example.book_your_seat.seat.service.facade;

import com.example.book_your_seat.seat.service.dto.SelectSeatsCommand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SeatCommandService {

    void selectSeats(SelectSeatsCommand command);


    void cancelSeats(List<Long> seatsId);
}
