package com.ticket.monolithticketmonster.concert.application;

import com.ticket.monolithticketmonster.UseCase;
import com.ticket.monolithticketmonster.concert.application.exception.ConcertNotFoundException;
import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.concert.domain.ConcertRepository;
import com.ticket.monolithticketmonster.concert.domain.dto.ConcertDTO;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class ConcertUseCase {

  private final ConcertRepository concertRepository;

  public List<ConcertDTO> getAllConcerts() {
    return StreamSupport.stream(concertRepository.findAll().spliterator(), false).toList().stream()
        .map(ConcertMapper::convertConcertEntityToDTO)
        .toList();
  }

  public ConcertDTO getConcertById(ConcertId concertId) {
    return concertRepository
        .findById(concertId)
        .map(ConcertMapper::convertConcertEntityToDTO)
        .orElseThrow(() -> new ConcertNotFoundException("Concert not found"));
  }

  public Long getConcertTicketCapacity(ConcertId concertId) {
    return concertRepository
        .findByIdAndLock(concertId)
        .orElseThrow(() -> new ConcertNotFoundException("Concert not found"));
  }

  public void decrementTicketCapacity(ConcertId concertId) {
    int updatedRowCount = concertRepository.decrementTicketCapacity(concertId);
    if (updatedRowCount == 0) {
      throw new IllegalStateException(
          "Ticket purchase for concert " + concertId + " has failed");
    }
  }
}
