package com.example.book_your_seat.likeconcert.service.facade;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.likeconcert.service.command.LikeConcertCommandService;
import com.example.book_your_seat.likeconcert.service.query.LikeConcertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class LikeConcertFacade {

    private final LikeConcertCommandService commandService;
    private final LikeConcertQueryService queryService;

    public void addLike(Long userId, Long concertId) {
        commandService.like(userId, concertId);
    }

    @Transactional(readOnly = true)
    public List<ConcertListResponse> findLikesByUserId(Long userId, Long lastLikeId) {
        return queryService.findAll(userId, lastLikeId);
    }

    @Transactional(readOnly = true)
    public ConcertResponse findById(Long likeId) {
        return queryService.findById(likeId);
    }

}
