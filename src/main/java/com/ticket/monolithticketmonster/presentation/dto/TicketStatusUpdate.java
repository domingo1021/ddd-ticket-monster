package com.ticket.monolithticketmonster.presentation.dto;

import com.ticket.monolithticketmonster.domain.model.TicketStatus;

public record TicketStatusUpdate(Long ticketId, TicketStatus fromStatus, TicketStatus toStatus) {
  public String toJson() {
    return String.format(
        "{\"ticketId\":%d,\"fromStatus\":\"%s\",\"toStatus\":\"%s\"}",
        ticketId(), fromStatus(), toStatus());
  }
}
