package com.ticket.monolithticketmonster.concert.application.exception;

public class ConcertNotFoundException extends RuntimeException{
  public ConcertNotFoundException(String message) {
    super(message);
  }
}
