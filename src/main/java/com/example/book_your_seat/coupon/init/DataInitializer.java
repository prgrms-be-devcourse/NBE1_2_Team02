package com.example.book_your_seat.coupon.init;

import com.example.book_your_seat.coupon.domain.Coupon;
import com.example.book_your_seat.coupon.domain.DiscountRate;
import com.example.book_your_seat.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CouponRepository couponRepository;


    @EventListener(ApplicationReadyEvent.class) //스프링 부트가 애플리케이션 준비된 후 자동으로 메서드가 호출
    @Transactional
    public void init(){
        Coupon coupon = new Coupon(100, DiscountRate.FIFTEEN);

        couponRepository.save(coupon);
    }


}
