package com.ticket.monolithticketmonster.user.application.exception;

public class JwtAuthFailedException extends BaseAuthenticationException {

  public static final String JWT_AUTHENTICATION_FAILED = "Jwt Authentication Failed";

  public JwtAuthFailedException() {
    super(JWT_AUTHENTICATION_FAILED, ConstantExceptionCode.JWT_AUTH_FAILED);
  }

  public JwtAuthFailedException(String message) {
    super(JWT_AUTHENTICATION_FAILED + ": " + message, ConstantExceptionCode.JWT_AUTH_FAILED);
  }
}
