package com.ticket.monolithticketmonster.ticket.presentation;

import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.ticket.application.TicketPurchaseUseCase;
import com.ticket.monolithticketmonster.user.domain.UserId;
import com.ticket.monolithticketmonster.user.domain.dto.UserDTO;
import com.ticket.monolithticketmonster.user.presentation.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TicketPurchaseController {

  private static final Logger logger = LoggerFactory.getLogger(TicketPurchaseController.class);
  private final TicketPurchaseUseCase ticketPurchaseUseCase;

  @PostMapping("/concerts/{concertId}/tickets")
  public ResponseEntity<?> buyTicket(
      @Valid @PathVariable String concertId, @AuthenticationPrincipal UserDTO user) {
    logger.info(
        "User {} is buying ticket for concert with id: {}", user.getUserId().toString(), concertId);
    var ticket =
        ticketPurchaseUseCase.purchaseTicket(
            new ConcertId(concertId), new UserId(user.getUserId()));
    return ResponseEntity.ok(
        ApiResponse.success(TicketPurchaseResponseMapping.mapToTicketPurchaseResponseDTO(ticket)));
  }
}
