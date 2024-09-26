package com.example.book_your_seat.user.repository;

import com.example.book_your_seat.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String Email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("select u from User u join fetch u.userCoupons uc join fetch uc.coupon where u.id = :userId")
    Optional<User> findByIdWithUserCoupons(@Param("userId") Long userId);
}
