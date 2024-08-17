package com.ticket.monolithticketmonster.application.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class BaseAuthenticationException extends AuthenticationException implements IErrorCodeProvider{

    private final int errorCode;

    public BaseAuthenticationException(String message, int code) {
      super(message);
      this.errorCode = code;
    }

    public BaseAuthenticationException(String message) {
      this(message, ConstantExceptionCode.GENERAL_AUTH_FAILED);
    }

}
