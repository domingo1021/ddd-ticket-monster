package com.ticket.monolithticketmonster.ticket.presentation;

import com.ticket.monolithticketmonster.ticket.domain.Ticket;
import com.ticket.monolithticketmonster.ticket.presentation.dto.TicketPurchaseResponseDTO;

public class TicketPurchaseResponseMapping {
  public static TicketPurchaseResponseDTO mapToTicketPurchaseResponseDTO(Ticket ticket) {
    return new TicketPurchaseResponseDTO(
        ticket.getId().getId().toString(),
        ticket.getStatus().status(),
        ticket.getUserId().id().toString(),
        ticket.getConcertId().getId().toString(),
        ticket.getCreatedAt().toString(),
        ticket.getUpdatedAt().toString());
  }
}
