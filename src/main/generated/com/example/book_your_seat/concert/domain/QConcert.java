package com.example.book_your_seat.concert.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConcert is a Querydsl query type for Concert
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConcert extends EntityPathBase<Concert> {

    private static final long serialVersionUID = -246643962L;

    public static final QConcert concert = new QConcert("concert");

    public final com.example.book_your_seat.common.entity.QBaseEntity _super = new com.example.book_your_seat.common.entity.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<LikeConcert, QLikeConcert> likeConcerts = this.<LikeConcert, QLikeConcert>createList("likeConcerts", LikeConcert.class, QLikeConcert.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> reservationStartAt = createDateTime("reservationStartAt", java.time.LocalDateTime.class);

    public final ListPath<com.example.book_your_seat.review.domain.Review, com.example.book_your_seat.review.domain.QReview> reviews = this.<com.example.book_your_seat.review.domain.Review, com.example.book_your_seat.review.domain.QReview>createList("reviews", com.example.book_your_seat.review.domain.Review.class, com.example.book_your_seat.review.domain.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.example.book_your_seat.seat.domain.Seat, com.example.book_your_seat.seat.domain.QSeat> seats = this.<com.example.book_your_seat.seat.domain.Seat, com.example.book_your_seat.seat.domain.QSeat>createList("seats", com.example.book_your_seat.seat.domain.Seat.class, com.example.book_your_seat.seat.domain.QSeat.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final NumberPath<Integer> startHour = createNumber("startHour", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalStock = createNumber("totalStock", Integer.class);

    public QConcert(String variable) {
        super(Concert.class, forVariable(variable));
    }

    public QConcert(Path<? extends Concert> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConcert(PathMetadata metadata) {
        super(Concert.class, metadata);
    }

}

