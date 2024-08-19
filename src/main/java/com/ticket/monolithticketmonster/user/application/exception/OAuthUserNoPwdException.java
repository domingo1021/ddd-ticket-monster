package com.ticket.monolithticketmonster.user.application.exception;

public class OAuthUserNoPwdException extends BaseAuthenticationException {
  public OAuthUserNoPwdException(String message) {
    super(message, ConstantExceptionCode.OAUTH_USER_NO_PWD);
  }
}
