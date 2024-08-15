package com.ticket.monolithticketmonster.application.service;

import com.ticket.monolithticketmonster.domain.model.SnowflakeIdGenerator;
import com.ticket.monolithticketmonster.presentation.dto.TicketStatusUpdate;
import com.ticket.monolithticketmonster.presentation.handler.WebSocketHandler;
import com.ticket.monolithticketmonster.presentation.handler.WebSocketHandler.WebSocketConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {
  private static final String TICKET_STATUS_TOPIC = "ticket-status-topic";
  private final WebSocketHandler webSocketHandler;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  public KafkaListenerService(
      WebSocketHandler webSocketHandler, SnowflakeIdGenerator snowflakeIdGenerator) {
    this.webSocketHandler = webSocketHandler;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  @KafkaListener(
      topics = KafkaListenerService.TICKET_STATUS_TOPIC,
      groupId = "#{T(java.lang.String).valueOf(@snowflakeIdGenerator.getId())}")
  public void consume(TicketStatusUpdate ticketStatusUpdate) {
    String message = ticketStatusUpdate.toJson();
    webSocketHandler.sendMessageToSubscribers(WebSocketConstants.TICKET_STATUS, message);
  }
}
