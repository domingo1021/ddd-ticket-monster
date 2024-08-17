package com.ticket.monolithticketmonster.application.exception;

public class OAuthUserNoPwdException extends BaseAuthenticationException {
  public OAuthUserNoPwdException(String message) {
    super(message, ConstantExceptionCode.OAUTH_USER_NO_PWD);
  }
}
