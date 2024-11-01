package com.example.book_your_seat.review.repository;

import com.example.book_your_seat.review.domain.Review;
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
    public Slice<Review> pageNationReviewList(Long reviewId, Long concertId, Pageable pageable) {

        List<Review> reviewList = queryFactory.selectFrom(review)
                .where(eqConcertId(concertId).and(ltReviewId(reviewId)))
                .join(review.user, user).fetchJoin()
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

    private BooleanExpression eqConcertId(Long concertId){
        return review.concert.id.eq(concertId);
    }
}
