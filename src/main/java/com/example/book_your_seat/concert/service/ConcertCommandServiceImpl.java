package com.example.book_your_seat.concert.service;


import com.example.book_your_seat.concert.domain.Concert;
import com.example.book_your_seat.concert.controller.dto.AddConcertRequest;
import com.example.book_your_seat.concert.controller.dto.ConcertResponse;
import com.example.book_your_seat.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ConcertCommandServiceImpl implements ConcertCommandService {

    private final ConcertRepository concertRepository;

    @Override
    public ConcertResponse add(final AddConcertRequest request) {
        Concert concert = AddConcertRequest.to(request);
        Concert savedConcert = concertRepository.save(concert);
        return ConcertResponse.from(savedConcert);
    }

    @Override
    public void delete(final Long id) {
        concertRepository.deleteById(id);
    }
}
