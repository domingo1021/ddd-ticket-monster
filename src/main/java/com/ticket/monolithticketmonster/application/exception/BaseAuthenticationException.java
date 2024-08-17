package com.ticket.monolithticketmonster.application.exception;

import org.springframework.security.core.AuthenticationException;

public class BaseAuthenticationException extends AuthenticationException implements IErrorCodeProvider{

    private final int errorCode;

    public BaseAuthenticationException(String message, int code) {
      super(message);
      this.errorCode = code;
    }

    public BaseAuthenticationException(String message) {
      this(message, ConstantExceptionCode.GENERAL_AUTH_FAILED);
    }

    public int getErrorCode() {
      return errorCode;
    }
}
