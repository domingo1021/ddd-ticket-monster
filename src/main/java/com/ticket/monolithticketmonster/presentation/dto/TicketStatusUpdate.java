package com.ticket.monolithticketmonster.presentation.dto;

import com.ticket.monolithticketmonster.domain.model.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusUpdate {
  private Long ticketId;
  private Long userId;
  private TicketStatus fromStatus;
  private TicketStatus toStatus;

  public String toJson() {
    return String.format(
        "{\"ticketId\":%d,\"userId\":%d,\"fromStatus\":\"%s\",\"toStatus\":\"%s\"}",
        ticketId, userId, fromStatus, toStatus);
  }
}
