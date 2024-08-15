package com.ticket.monolithticketmonster.application.exception;

public class OAuthUserNoPwdException extends RuntimeException {
  public OAuthUserNoPwdException(String message) {
    super(message);
  }
}
