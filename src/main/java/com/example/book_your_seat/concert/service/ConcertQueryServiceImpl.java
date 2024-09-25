package com.example.book_your_seat.concert.service;

import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.book_your_seat.concert.ConcertConst.INVALID_CONCERT_ID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ConcertQueryServiceImpl implements ConcertQueryService {

    private final ConcertRepository concertRepository;

    @Override
    public List<ConcertResponse> findAll() {
        return concertRepository.findAll().stream()
                .map(ConcertResponse::from)
                .toList();
    }

    @Override
    public ConcertResponse findById(final Long id) {
        return concertRepository.findById(id)
                .map(ConcertResponse::from)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT_ID + id));
    }
}
