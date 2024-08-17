package com.ticket.monolithticketmonster.application.exception;

public class UserAlreadyExistException extends BaseAuthenticationException {
  public UserAlreadyExistException(String message) {
    super(message, ConstantExceptionCode.USER_ALREADY_EXIST);
  }
}
