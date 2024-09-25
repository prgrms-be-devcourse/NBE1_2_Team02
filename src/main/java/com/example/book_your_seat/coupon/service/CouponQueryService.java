package com.example.book_your_seat.coupon.service;

import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse;
import java.util.List;

public interface CouponQueryService {

    List<CouponDetailResponse> getCouponDetail(Long userId);

}
