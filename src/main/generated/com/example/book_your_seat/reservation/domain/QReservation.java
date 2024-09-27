package com.example.book_your_seat.reservation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = 917800006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final com.example.book_your_seat.user.domain.QAddress address;

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    public final NumberPath<Integer> finalPrice = createNumber("finalPrice", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.book_your_seat.payment.domain.QPayment payment;

    public final ListPath<com.example.book_your_seat.seat.domain.Seat, com.example.book_your_seat.seat.domain.QSeat> seats = this.<com.example.book_your_seat.seat.domain.Seat, com.example.book_your_seat.seat.domain.QSeat>createList("seats", com.example.book_your_seat.seat.domain.Seat.class, com.example.book_your_seat.seat.domain.QSeat.class, PathInits.DIRECT2);

    public final EnumPath<ReservationStatus> status = createEnum("status", ReservationStatus.class);

    public final com.example.book_your_seat.user.domain.QUser user;

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.example.book_your_seat.user.domain.QAddress(forProperty("address"), inits.get("address")) : null;
        this.payment = inits.isInitialized("payment") ? new com.example.book_your_seat.payment.domain.QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.user = inits.isInitialized("user") ? new com.example.book_your_seat.user.domain.QUser(forProperty("user")) : null;
    }

}

