package com.example.book_your_seat.reservation.repository;

import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.reservation.service.command.ReservationsCommand;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_your_seat.reservation.ReservationConst.INVALID_USER;
import static com.example.book_your_seat.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepositoryImpl implements ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> findAllReservations(ReservationsCommand command) {

        Long reservationId = command.getReservationId();
        Long userId = command.getUserId();
        int pageSize = command.getPageSize();

        return queryFactory
                .selectFrom(reservation)
                .where(
                        eqUserId(userId),
                        ltReservationId(reservationId)
                )
                .orderBy(reservation.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanExpression eqUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(INVALID_USER);
        }
        return reservation.user.id.eq(userId);
    }

    private BooleanExpression ltReservationId(Long reservationId) {
        if (reservationId == null) {
            return null;
        }
        return reservation.id.lt(reservationId);
    }
}
