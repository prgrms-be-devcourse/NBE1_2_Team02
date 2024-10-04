package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.dto.ReservationsCommand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_your_seat.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepositoryImpl implements ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> findAllReservations(ReservationsCommand command) {
        return List.of();
    }


    /**
     * 수정해
     * @param seatsId
     * @return
     */
    @Override
    public Reservation findReservationBySeatsId(List<Long> seatsId) {
        return queryFactory.selectFrom(reservation)
                .fetchOne();
    }
}
