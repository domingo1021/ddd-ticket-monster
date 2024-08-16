package com.ticket.monolithticketmonster.presentation.controller;

import com.ticket.monolithticketmonster.application.service.TicketService;
import com.ticket.monolithticketmonster.domain.model.Ticket;
import com.ticket.monolithticketmonster.domain.model.TicketStatus;
import com.ticket.monolithticketmonster.domain.validation.ValidEnum;
import com.ticket.monolithticketmonster.presentation.dto.ApiResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
      @Validated
      @ValidEnum(enumClass = TicketStatus.class, message = "Invalid ticket status.")
          @RequestParam(name = "status")
          TicketStatus ticketStatus) {
    logger.info("Getting all tickets with status {}", ticketStatus);
    var tickets = ticketService.getAllTicketsByStatus(ticketStatus);
    return ResponseEntity.ok(ApiResponse.success(tickets));
  }

  @PostMapping("/{ticketId}/purchases")
  public ResponseEntity<ApiResponse<Void>> buyTicket(@PathVariable Long ticketId) {
    logger.info("Buying ticket with id: {}", ticketId);
    ticketService.purchaseTicket(ticketId);
    return ResponseEntity.ok(ApiResponse.success());
  }

  // handle with @ExceptionHandler(MethodArgumentNotValidException.class), reference:
  // https://www.baeldung.com/spring-boot-bean-validation
}
