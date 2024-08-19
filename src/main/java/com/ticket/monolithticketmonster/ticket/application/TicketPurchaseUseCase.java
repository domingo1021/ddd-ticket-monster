package com.ticket.monolithticketmonster.ticket.application;

import com.ticket.monolithticketmonster.UseCase;
import com.ticket.monolithticketmonster.concert.application.ConcertUseCase;
import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.ticket.application.exception.ConcertTicketNoStockException;
import com.ticket.monolithticketmonster.ticket.domain.Ticket;
import com.ticket.monolithticketmonster.ticket.domain.TicketRepository;
import com.ticket.monolithticketmonster.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class TicketPurchaseUseCase {
  private static final Logger logger = LoggerFactory.getLogger(TicketPurchaseUseCase.class);
  private static final String TICKET_STATUS_TOPIC = "ticket-status-topic";
  private final TicketRepository ticketRepository;
  private final ConcertUseCase concertUseCase;

  @Transactional
  public Ticket purchaseTicket(ConcertId concertId, UserId userId) {
    Long ticketCapacity = concertUseCase.getConcertTicketCapacity(concertId);
    System.out.println("Ticket capacity at t0: " + ticketCapacity);
    if (ticketCapacity <= 0) {
      throw new ConcertTicketNoStockException("No tickets available for concert");
    }
    concertUseCase.decrementTicketCapacity(concertId);

    var newTicket = new Ticket(concertId, userId);
    ticketRepository.save(newTicket);
    logger.info(
        "Ticket({}) purchased for concert with id: {}",
        newTicket.getId().getId().toString(),
        concertId.getId().toString());

    return newTicket;
  }
}
