package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatJdbcRepositoryImpl implements SeatJdbcRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void insertBulkSeats(List<Seat> seats) {
        String sql = "INSERT INTO Seat (concertId, seatNumber, zone, isSold) " +
                "VALUES (:concertId, :seatNumber, :zone, :isSold)";

        jdbcClient.sql(sql).params(seats).update();
    }
}
