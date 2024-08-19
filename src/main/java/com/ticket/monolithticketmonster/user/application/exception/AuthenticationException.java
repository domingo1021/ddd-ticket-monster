package com.ticket.monolithticketmonster.user.application.exception;

import com.ticket.monolithticketmonster.user.application.exception.BaseAuthenticationException;

public class AuthenticationException extends BaseAuthenticationException {
  public AuthenticationException(String message) {
    super(message);
  }
}
