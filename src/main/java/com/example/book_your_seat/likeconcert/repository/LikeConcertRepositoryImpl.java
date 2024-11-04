package com.example.book_your_seat.likeconcert.repository;

import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.book_your_seat.likeconcert.domain.QLikeConcert.likeConcert;

@Repository
@RequiredArgsConstructor
public class LikeConcertRepositoryImpl implements LikeConcertRepositoryCustom {

    private static final int PAGE_SIZE = 10;
    private final JPAQueryFactory queryFactory;
    
    @Override
    public List<LikeConcert> findByUserId(Long userId, Long likeId) {
        return queryFactory
                .selectFrom(likeConcert)
                .where(
                        ltLikeId(likeId),
                        likeConcert.userId.eq(userId)
                )
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression ltLikeId(Long likeId) {
        if (likeId == null) {
            return null;
        }
        return likeConcert.id.lt(likeId);
    }
}
