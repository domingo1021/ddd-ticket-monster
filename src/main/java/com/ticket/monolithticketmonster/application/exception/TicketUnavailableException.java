package com.ticket.monolithticketmonster.application.exception;

public class TicketUnavailableException extends RuntimeException {
  public TicketUnavailableException(String message) {
    super(message);
  }
}
