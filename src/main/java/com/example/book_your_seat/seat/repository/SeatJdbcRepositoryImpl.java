package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatJdbcRepositoryImpl implements SeatJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void insertBulkSeats(List<Seat> seats) {
        String sql = "INSERT INTO seat (concert_id, seat_number, zone, is_sold) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                seats,
                seats.size(),
                (PreparedStatement ps, Seat seat) -> {
                    ps.setLong(1, seat.getId().getConcertId());
                    ps.setLong(2, seat.getId().getSeatNumber());
                    ps.setString(3, seat.getZone().toString());
                    ps.setBoolean(4, false);
                });
    }
}