package com.example.book_your_seat.likeconcert.service.facade;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.likeconcert.service.command.LikeConcertCommandService;
import com.example.book_your_seat.likeconcert.service.query.LikeConcertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeConcertFacade {

    private final LikeConcertCommandService commandService;
    private final LikeConcertQueryService queryService;

    public void addLike(Long userId, Long concertId) {
        commandService.like(userId, concertId);
    }

    public List<ConcertListResponse> findLikesByUserId(Long userId) {
        return queryService.findAll(userId);
    }

    public void deleteLike(Long likeConcertId) {
        commandService.delete(likeConcertId);
    }
}
