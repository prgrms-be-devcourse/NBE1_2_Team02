package com.example.book_your_seat.likeconcert.service.query;

import com.example.book_your_seat.concert.controller.dto.ConcertListResponse;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import com.example.book_your_seat.likeconcert.domain.LikeConcert;
import com.example.book_your_seat.likeconcert.repository.LikeConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.book_your_seat.common.constants.Constants.INVALID_CONCERT;
import static com.example.book_your_seat.likeconcert.LikeConst.INVALID_LIKE;

@Service
@RequiredArgsConstructor
public class LikeConcertQueryService {

    private final ConcertRepository concertRepository;
    private final LikeConcertRepository likeConcertRepository;

    public List<ConcertListResponse> findAll(Long userId, Long lastLikeId) {
        List<Long> concertIds = likeConcertRepository.findByUserId(userId, lastLikeId).stream()
                .map(LikeConcert::getConcertId)
                .toList();

        return concertRepository.findAllById(concertIds).stream()
                .map(ConcertListResponse::new)
                .toList();
    }

    public ConcertResponse findById(Long likeConcertId) {
        LikeConcert likeConcert = likeConcertRepository.findById(likeConcertId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_LIKE));

        return concertRepository.findById(likeConcert.getConcertId()).stream()
                .map(ConcertResponse::from)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT));
    }
}
