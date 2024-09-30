package com.example.book_your_seat.kafak.consumer;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.UserCoupon;
import com.example.book_your_seat.coupon.manager.CouponManager;
import com.example.book_your_seat.coupon.manager.UserCouponManager;
import com.example.book_your_seat.kafak.event.UserCouponEvent;
import com.example.book_your_seat.user.manager.UserManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class CouponCreateConsumer{

    private final UserManager userManager;
    private final CouponManager couponManager;
    private final UserCouponManager userCouponManager;

    @KafkaListener(topics = "issue-coupon", groupId = "group_1")
    public void listener(UserCouponEvent userCouponEvent) {
        log.info("시작");
        Coupon coupon = couponManager.findById(userCouponEvent.couponId());
        coupon.issue();
        couponManager.saveAndFlush(coupon);
        userCouponManager.save(new UserCoupon(userManager.getUser(userCouponEvent.userId()), coupon));
        log.info("끝");
    }

}
