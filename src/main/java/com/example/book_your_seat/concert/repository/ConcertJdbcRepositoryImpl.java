package com.example.book_your_seat.concert.repository;

import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.seat.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import static com.example.book_your_seat.concert.ConcertConst.*;

@RequiredArgsConstructor
@Repository
public class ConcertJdbcRepositoryImpl implements ConcertJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long saveBulkData(Concert concert) {
        Long concertId = saveConcert(concert);
        saveBulkSeat(concert, concertId);
        return concertId;
    }

    @Override
    public void deleteBulkData(Long concertId) {
        String seatSql = "DELETE FROM seat WHERE concert_id = ?";
        String concertSql = "DELETE FROM concert WHERE concert_id = ?";

        jdbcTemplate.update(seatSql, concertId);
        jdbcTemplate.update(concertSql, concertId);

    }

    private Long saveConcert(Concert concert) {
        String concertSql = "INSERT INTO concert (" +
                "title, " +
                "start_date, " +
                "end_date, " +
                "price, " +
                "start_hour, " +
                "total_stock, " +
                "reservation_start_at" +
                ") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(concertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, concert.getTitle());
            ps.setDate(2, Date.valueOf(concert.getStartDate()));
            ps.setDate(3, Date.valueOf(concert.getEndDate()));
            ps.setInt(4, concert.getPrice());
            ps.setInt(5, concert.getStartHour());
            ps.setInt(6, TOTAL_STOCK);
            ps.setTimestamp(7, Timestamp.valueOf(concert.getReservationStartAt()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private void saveBulkSeat(Concert concert, Long concertId) {
        String seatSql = "INSERT INTO seat (concert_id, is_sold) " +
                "VALUES (?, ?)";

        jdbcTemplate.batchUpdate(seatSql,
                concert.getSeats(),
                concert.getSeats().size(),
                (PreparedStatement ps, Seat seat) -> {
                    ps.setLong(1, concertId);
                    ps.setBoolean(2, seat.isSold());
                });
    }
}
