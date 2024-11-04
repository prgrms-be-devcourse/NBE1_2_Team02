package com.example.book_your_seat.coupon.repository;

import com.example.book_your_seat.coupon.controller.dto.UserCouponRequest;
import com.example.book_your_seat.coupon.controller.dto.UserCouponResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.book_your_seat.coupon.domain.QCoupon.coupon;
import static com.example.book_your_seat.coupon.domain.QUserCoupon.userCoupon;
import static com.example.book_your_seat.user.domain.QUser.user;

public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<UserCouponResponse> selectUserCoupons(UserCouponRequest userCouponRequest,Long memberId, Pageable pageable) {
        List<UserCouponResponse> result = queryFactory
                .select(Projections.constructor(UserCouponResponse.class))
                .from(userCoupon)
                .join(user).on(user.id.eq(userCoupon.id))
                .join(coupon).on(coupon.id.eq(userCoupon.id))
                .where(isUsed(userCouponRequest.used()), isMember(memberId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 한개를 더 반환
                .fetch();

        boolean hasNext = false;

        int pageSize = pageable.getPageSize();

        if(result.size() > pageSize){  //다음 페이지에 데이터가 더 있으면
            hasNext = true;
            result.remove(pageSize); //마지막꺼 제거
        }

        return new SliceImpl<>(result, pageable, hasNext);

    }

    private BooleanExpression isMember(Long memberId) {
        return user.id.eq(memberId);
    }

    private BooleanExpression isUsed(Boolean used) {
        return (used == null) ? null : (used ? userCoupon.isUsed.isTrue() : userCoupon.isUsed.isFalse());
    }
}
