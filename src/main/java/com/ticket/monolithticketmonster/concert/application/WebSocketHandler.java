package com.ticket.monolithticketmonster.concert.application;

import com.google.gson.Gson;
import com.ticket.monolithticketmonster.concert.domain.dto.WsSubscriptionMessage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
  private final ConcurrentMap<String, List<WebSocketSession>> subscriptions =
      new ConcurrentHashMap<>();
  private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
  private final List<String> subscriptionWhitelist = List.of(WebSocketConstants.TICKET_CAPACITY_UPDATE);
  private final Gson gson = new Gson();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();

    if (!isSubscriptionMessage(payload)) {
      logger.warn("Received an invalid message: {}", payload);
      return;
    }

    String subscriptionType = parseSubscriptionType(payload);
    if (subscriptionWhitelist.contains(subscriptionType)) {
      subscribe(session, subscriptionType);
      session.sendMessage(new TextMessage("Subscribed to: " + subscriptionType));
    } else {
      logger.warn("Received an invalid message: {}", payload);
      session.sendMessage(new TextMessage("Invalid subscription type: " + subscriptionType));
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessions.remove(session);
    subscriptions.forEach((messageType, sessionList) -> sessionList.remove(session));
  }

  /**
   * Parse the message type from the payload.
   *
   * @param payload the message payload, example: {"action": "subscription", "data": { "type":
   *     "ticket-capacity-update" }}
   * @return the message type
   */
  private String parseSubscriptionType(String payload) {
    try {
      return gson.fromJson(payload, WsSubscriptionMessage.class).data().type();
    } catch (Exception e) {
      return WebSocketConstants.INVALID_SUBSCRIPTION_TYPE;
    }
  }

  private boolean isSubscriptionMessage(String payload) {
    try {
      return gson.fromJson(payload, WsSubscriptionMessage.class)
          .action()
          .equals(WebSocketConstants.SUBSCRIPTION_ACTION);
    } catch (Exception e) {
      logger.error("Failed to determine if message is a subscription from payload: {}", payload, e);
      return false;
    }
  }

  /**
   * Subscribes the given WebSocket session to a specific message type. The session will receive all
   * future messages of this type.
   *
   * @param session the WebSocket session to subscribe
   * @param messageType the type of message the session is subscribing to
   */
  private void subscribe(WebSocketSession session, String messageType) {
    subscriptions.computeIfAbsent(messageType, k -> new CopyOnWriteArrayList<>()).add(session);
  }

  /**
   * Sends a message to all WebSocket sessions subscribed to the given message type.
   *
   * @param messageType the type of message being sent
   * @param message the message content to send
   */
  public void sendMessageToSubscribers(String messageType, String message) {
    List<WebSocketSession> sessions = subscriptions.get(messageType);
    if (sessions == null) return;

    for (WebSocketSession session : sessions) {
      try {
        session.sendMessage(new TextMessage(message));
      } catch (IOException e) {
        logger.error("Failed to send message to session {}: {}", session.getId(), e.getMessage());
      }
    }
  }

  public static final class WebSocketConstants {
    public static final String SUBSCRIPTION_ACTION = "subscription";
    public static final String INVALID_SUBSCRIPTION_TYPE = "invalidMessageType";
    public static final String TICKET_CAPACITY_UPDATE = "ticket-capacity-update";

    private WebSocketConstants() {}
  }

  public static final class WebSocketEndpoints {
    public static final String CONCERT_TICKET_CAPACITY_UPDATED = "/ws/ticket-capacity-update";
  }
}
