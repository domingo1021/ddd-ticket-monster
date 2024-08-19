package com.ticket.monolithticketmonster.concert.application;

import com.ticket.monolithticketmonster.concert.domain.Concert;
import com.ticket.monolithticketmonster.concert.domain.dto.ConcertDTO;

public class ConcertMapper {
  public static ConcertDTO convertConcertEntityToDTO(Concert concert) {
    return new ConcertDTO(
        concert.getId(),
        concert.getName(),
        concert.getTicketCapacity(),
        concert.getTicketPrice(),
        concert.getDate());
  }
}
