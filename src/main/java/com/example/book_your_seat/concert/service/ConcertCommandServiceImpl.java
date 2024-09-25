package com.example.book_your_seat.concert.service;


import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.book_your_seat.concert.ConcertConst.INVALID_CONCERT_ID;

@Transactional
@RequiredArgsConstructor
@Service
public class ConcertCommandServiceImpl implements ConcertCommandService {

    private final ConcertRepository concertRepository;

    @Override
    public Long add(final AddConcertRequest request) {
        Concert concert = AddConcertRequest.to(request);
        return concertRepository.saveBulkData(concert);
    }

    @Override
    public void delete(final Long id) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_CONCERT_ID + id));
        concertRepository.deleteBulkData(concert.getId());
    }
}
