package com.example.book_your_seat.luckydraw.service;

import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.luckydraw.domain.LuckyDrawHistory;
import com.example.book_your_seat.luckydraw.repository.LuckyDrawHistoryRepository;
import com.example.book_your_seat.reservation.repository.ReservationRepository;
import com.example.book_your_seat.user.domain.User;
import com.example.book_your_seat.user.mail.util.MailUtil;
import com.example.book_your_seat.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class LuckyDrawScheduler {

    private final ReservationRepository reservationRepository;
    private final LuckyDrawHistoryRepository luckyDrawHistoryRepository;
    private final MailUtil mailUtil;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 1 * ?")
    public void luckyDraw() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Long> reservedUserIds = getReservedUserIds(startOfLastMonth, endOfLastMonth);
        Collections.shuffle(new ArrayList<>(reservedUserIds));

        pickWinnerAndSendMail(reservedUserIds.subList(0, 5), DiscountRate.TWENTY);
        pickWinnerAndSendMail(reservedUserIds.subList(5, 15), DiscountRate.FIFTEEN);
        pickWinnerAndSendMail(reservedUserIds.subList(15, 30), DiscountRate.TEN);
        pickWinnerAndSendMail(reservedUserIds.subList(30, 50), DiscountRate.FIVE);
    }

    private void pickWinnerAndSendMail(List<Long> users, DiscountRate discountRate) {
        for (Long userId : users) {
            luckyDrawHistoryRepository.save(new LuckyDrawHistory(userId, discountRate));
            mailUtil.sendWinningMail(getUser(userId).getEmail(), discountRate);
        }
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    private List<Long> getReservedUserIds(LocalDateTime startOfLastMonth, LocalDateTime endOfLastMonth) {
        return reservationRepository.findShippedReservationsLastMonth(startOfLastMonth, endOfLastMonth);
    }

}
