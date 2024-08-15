package com.ticket.monolithticketmonster.presentation.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception designed to handle exceptions thrown by both MVC controllers and WebFlux
 * handlers. And it will return a General JSON response with the appropriate status code and error
 * message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler{

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public String noHandlerFoundException(Exception ex, WebRequest _request) {
    logException(HttpStatus.NOT_FOUND, ex);
    return ex.getMessage();
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public String authenticationException(Exception ex, WebRequest _request) {
    logException(HttpStatus.UNAUTHORIZED, ex);
    return ex.getMessage();
  }

  @ExceptionHandler({Exception.class, Throwable.class})
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public String globalExceptionHandler(Exception ex, WebRequest _request) {
    logException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    return ex.getMessage();
  }

  private void logException(HttpStatus status, Throwable ex) {
    if (status.is4xxClientError()) {
      logger.warn("Client error: {}", ex.getMessage());
    } else {
      logger.error("Server error: {}", ex.getMessage(), ex);
    }
  }
}
