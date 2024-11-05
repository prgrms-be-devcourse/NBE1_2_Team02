package com.example.book_your_seat.review.repository;

import com.example.book_your_seat.review.controller.dto.ReviewListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_your_seat.review.domain.QReview.review;
import static com.example.book_your_seat.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ReviewListResponse> pageNationReviewList(Long concertId, Long reviewId, Pageable pageable) {

        List<ReviewListResponse> reviewList = queryFactory
                .select(Projections.constructor(ReviewListResponse.class,
                        user.username,
                        review.content,
                        review.starCount,
                        review.id
                ))
                .from(review)
                .join(user).on(review.userId.eq(user.id))
                .where(ltReviewId(reviewId))
                .orderBy(review.createdAt.desc())
                .limit(pageable.getPageSize() + 1) //후에 값이 더 있나 체크하기 위해 하나 더 가져옴
                .fetch();

        boolean hasNext = false;

        if(reviewList.size()> pageable.getPageSize()){
            hasNext = true; //값이 더 있는지 체크
            reviewList.remove(pageable.getPageSize()); // 후에 값이 더 있으면 마지막 값을 지워줌
        }


        return new SliceImpl<>(reviewList, pageable, hasNext);
    }

    @Override
    public Slice<ReviewListResponse> pageNationUserReviewList(Long userId, Long reviewId, Pageable pageable) {

        List<ReviewListResponse> reviewList = queryFactory
                .select(Projections.constructor(ReviewListResponse.class,
                        user.username,
                        review.content,
                        review.starCount,
                        review.id
                ))
                .from(review)
                .join(user).on(review.userId.eq(user.id))
                .where(ltReviewId(reviewId))
                .limit(pageable.getPageSize() + 1) //후에 값이 더 있나 체크하기 위해 하나 더 가져옴
                .orderBy(review.createdAt.desc())
                .fetch();

        boolean hasNext = false;

        if(reviewList.size()> pageable.getPageSize()){
            hasNext = true; //값이 더 있는지 체크
            reviewList.remove(pageable.getPageSize()); // 후에 값이 더 있으면 마지막 값을 지워줌
        }


        return new SliceImpl<>(reviewList, pageable, hasNext);
    }

    private BooleanExpression ltReviewId(Long reviewId) {
        return reviewId != null ? review.id.lt(reviewId) : null;
    }
}
