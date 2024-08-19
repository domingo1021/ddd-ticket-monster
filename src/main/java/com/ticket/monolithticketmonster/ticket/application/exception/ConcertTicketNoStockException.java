package com.ticket.monolithticketmonster.ticket.application.exception;

public class ConcertTicketNoStockException extends RuntimeException{
  public ConcertTicketNoStockException() {
    super("No stock available for this concert");
  }
  public ConcertTicketNoStockException(String message) {
    super(message);
  }
}
