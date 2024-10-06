package com.example.book_your_seat.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1438871060L;

    public static final QUser user = new QUser("user");

    public final com.example.book_your_seat.common.entity.QBaseEntity _super = new com.example.book_your_seat.common.entity.QBaseEntity(this);

    public final ListPath<Address, QAddress> addressList = this.<Address, QAddress>createList("addressList", Address.class, QAddress.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<com.example.book_your_seat.concert.domain.LikeConcert, com.example.book_your_seat.concert.domain.QLikeConcert> likeConcerts = this.<com.example.book_your_seat.concert.domain.LikeConcert, com.example.book_your_seat.concert.domain.QLikeConcert>createList("likeConcerts", com.example.book_your_seat.concert.domain.LikeConcert.class, com.example.book_your_seat.concert.domain.QLikeConcert.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.example.book_your_seat.reservation.domain.Reservation, com.example.book_your_seat.reservation.domain.QReservation> reservations = this.<com.example.book_your_seat.reservation.domain.Reservation, com.example.book_your_seat.reservation.domain.QReservation>createList("reservations", com.example.book_your_seat.reservation.domain.Reservation.class, com.example.book_your_seat.reservation.domain.QReservation.class, PathInits.DIRECT2);

    public final ListPath<com.example.book_your_seat.review.domain.Review, com.example.book_your_seat.review.domain.QReview> reviews = this.<com.example.book_your_seat.review.domain.Review, com.example.book_your_seat.review.domain.QReview>createList("reviews", com.example.book_your_seat.review.domain.Review.class, com.example.book_your_seat.review.domain.QReview.class, PathInits.DIRECT2);

    public final ListPath<com.example.book_your_seat.coupon.domain.UserCoupon, com.example.book_your_seat.coupon.domain.QUserCoupon> userCoupons = this.<com.example.book_your_seat.coupon.domain.UserCoupon, com.example.book_your_seat.coupon.domain.QUserCoupon>createList("userCoupons", com.example.book_your_seat.coupon.domain.UserCoupon.class, com.example.book_your_seat.coupon.domain.QUserCoupon.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final EnumPath<UserRole> userRole = createEnum("userRole", UserRole.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

