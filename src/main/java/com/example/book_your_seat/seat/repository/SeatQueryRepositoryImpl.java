package com.example.book_your_seat.seat.repository;

import com.example.book_your_seat.seat.domain.Seat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_your_seat.seat.domain.QSeat.seat;

@RequiredArgsConstructor
@Repository
public class SeatQueryRepositoryImpl implements SeatQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Seat> findValidSeats(Long concertId, List<Long> seatsId) {
        return queryFactory.selectFrom(seat)
                .where(
                        seat.concert.id.eq(concertId),
                        seat.id.in(seatsId),
                        seat.isSold.eq(false)
                )
                .fetch();
    }

    @Override
    public Long reserveSeats(Long concertId, List<Long> seatId) {
        return queryFactory.update(seat)
                .where(
                        seat.concert.id.eq(concertId),
                        seat.id.in(seatId)
                )
                .set(seat.isSold, true)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .execute();
    }

    @Override
    public void cancelSeats(List<Long> seatId) {
        queryFactory.update(seat)
                .where(seat.id.in(seatId))
                .set(seat.isSold, false)
                .execute();
    }
}
