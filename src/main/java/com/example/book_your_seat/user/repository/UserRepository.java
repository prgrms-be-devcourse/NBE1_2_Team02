package com.example.book_your_seat.user.repository;

import com.example.book_your_seat.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
