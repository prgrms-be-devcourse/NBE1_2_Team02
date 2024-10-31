package com.example.book_your_seat.luckydraw.repository;

import com.example.book_your_seat.luckydraw.domain.LuckyDrawHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LuckyDrawHistoryRepository extends JpaRepository<LuckyDrawHistory, Long> {
}
