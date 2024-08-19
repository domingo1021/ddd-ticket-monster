package com.ticket.monolithticketmonster.ticket.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.List;

@Embeddable
public record TicketStatus(@Column(name = "status", length = 20, nullable = false) String status) {
  public static final String RESERVED = "RESERVED";
  public static final String SOLD = "SOLD";
  public static final List<String> VALID_STATUSES = List.of(RESERVED, SOLD);

  public TicketStatus {
    if (!VALID_STATUSES.contains(status)) {
      throw new IllegalArgumentException("Invalid ticket status: " + status);
    }
  }
}
