package com.ticket.monolithticketmonster.concert.infrastructure.exception;

public class MessageSendFailureException extends RuntimeException {
  public MessageSendFailureException(String message) {
    super(message);
  }
}