package com.ticket.monolithticketmonster.concert.application;

import com.ticket.monolithticketmonster.concert.domain.ConcertRepository;
import com.ticket.monolithticketmonster.concert.domain.ConcertTicketCapacityUpdated;
import com.ticket.monolithticketmonster.concert.application.WebSocketHandler.WebSocketConstants;
import com.ticket.monolithticketmonster.concert.domain.SnowflakeIdGenerator;
import com.ticket.monolithticketmonster.ticket.application.exception.ConcertTicketNoStockException;
import com.ticket.monolithticketmonster.ticket.domain.TicketReserved;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventListener {
  public static final String CONCERT_TICKET_CAPACITY_TOPIC = "concert-ticket-capacity-topic";
  private final ConcertRepository concertRepository;
  private final WebSocketHandler webSocketHandler;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final IConcertEventProducer concertEventProducer;

  @ApplicationModuleListener
  public void handle(TicketReserved event) {
    Long concertTicketCapacity =
        concertRepository.findTicketCapacityById(event.concertId()).orElse(null);
    if (concertTicketCapacity == null) return;

    concertEventProducer.sendMessage(
        CONCERT_TICKET_CAPACITY_TOPIC,
        new ConcertTicketCapacityUpdated(event.concertId(), concertTicketCapacity).toJSON());
  }

  @KafkaListener(
      topics = CONCERT_TICKET_CAPACITY_TOPIC,
      groupId = "#{T(java.lang.String).valueOf(@snowflakeIdGenerator.getId())}")
  public void consume(ConcertTicketCapacityUpdated capacityUpdate) {
    String message = capacityUpdate.toJSON();
    webSocketHandler.sendMessageToSubscribers(WebSocketConstants.TICKET_CAPACITY_UPDATE, message);
  }
}
