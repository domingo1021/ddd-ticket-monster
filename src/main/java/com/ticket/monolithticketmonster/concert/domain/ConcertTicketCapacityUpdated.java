package com.ticket.monolithticketmonster.concert.domain;

import org.jmolecules.event.types.DomainEvent;

public record ConcertTicketCapacityUpdated(ConcertId concertId, Long newCapacity)
    implements DomainEvent {
  public String toJSON() {
    return "{\"concertId\":\"" + concertId.getId() + "\",\"newCapacity\":" + newCapacity + "}";
  }
}
