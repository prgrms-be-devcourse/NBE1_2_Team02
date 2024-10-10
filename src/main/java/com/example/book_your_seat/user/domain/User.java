package com.example.book_your_seat.user.domain;

import com.example.book_your_seat.common.entity.BaseEntity;
import com.example.book_your_seat.concert.domain.LikeConcert;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.reservation.domain.Reservation;
import com.example.book_your_seat.review.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String username;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Address> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<UserCoupon> userCoupons = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<LikeConcert> likeConcerts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Reservation> reservations = new ArrayList<>();

    public User(String nickname, String username, String email, String password) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
    }

    public void changeRoleToAdmin() {
        this.userRole = UserRole.ADMIN;
    }

    public void setAddress(Address address) {
        this.addressList.add(address);
    }

    public void adduserCoupon(UserCoupon userCoupon) {
        this.userCoupons.add(userCoupon);
    }

    public void addLikeConcert(LikeConcert likeConcert) {
        this.likeConcerts.add(likeConcert);
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }
}
