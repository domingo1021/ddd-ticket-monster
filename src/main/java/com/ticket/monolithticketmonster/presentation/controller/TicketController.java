package com.ticket.monolithticketmonster.presentation.controller;

import com.ticket.monolithticketmonster.application.service.TicketService;
import com.ticket.monolithticketmonster.domain.model.Ticket;
import com.ticket.monolithticketmonster.domain.model.TicketStatus;
import com.ticket.monolithticketmonster.domain.validation.ValidEnum;
import com.ticket.monolithticketmonster.presentation.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

  private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
  private final TicketService ticketService;

  public TicketController(TicketService ticketService) {
    this.ticketService = ticketService;
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<Ticket>>> getTickets(
      @ValidEnum(enumClass = TicketStatus.class, message = "Invalid ticket status")
          @RequestParam(name = "status")
          String status) {
    logger.info("Getting all tickets with status {}", status);

    TicketStatus ticketStatus = TicketStatus.valueOf(status);
    var tickets = ticketService.getAllTicketsByStatus(ticketStatus);
    return ResponseEntity.ok(ApiResponse.success(tickets));
  }

  @PostMapping("/{ticketId}/purchases")
  public ResponseEntity<ApiResponse<Void>> buyTicket(
      @Valid
          @Min(value = 1, message = "Should be integer greater than 1")
          @Max(value = Long.MAX_VALUE, message = "Invalid ticket id number")
          @PathVariable Long ticketId) {
    logger.info("Buying ticket with id: {}", ticketId);
    ticketService.purchaseTicket(ticketId);
    return ResponseEntity.ok(ApiResponse.success());
  }
}
