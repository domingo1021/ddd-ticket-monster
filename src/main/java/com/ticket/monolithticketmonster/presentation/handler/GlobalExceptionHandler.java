package com.ticket.monolithticketmonster.presentation.handler;

import com.ticket.monolithticketmonster.application.exception.BaseAuthenticationException;
import com.ticket.monolithticketmonster.application.exception.ConstantExceptionCode;
import com.ticket.monolithticketmonster.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.application.exception.OAuthUserNoPwdException;
import com.ticket.monolithticketmonster.application.exception.TicketUnavailableException;
import com.ticket.monolithticketmonster.application.exception.UserAlreadyExistException;
import com.ticket.monolithticketmonster.presentation.dto.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception designed to handle exceptions thrown by both MVC controllers and WebFlux
 * handlers. And it will return a General JSON response with the appropriate status code and error
 * message.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // Customized Exception Handling
  @ExceptionHandler({
    JwtAuthFailedException.class,
    OAuthUserNoPwdException.class,
    UserAlreadyExistException.class
  })
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public ApiResponse<?> jwtAuthFailedException(BaseAuthenticationException ex, WebRequest _request) {
    logException(HttpStatus.UNAUTHORIZED, ex);
    return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
  }

  @ExceptionHandler(TicketUnavailableException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponse<?> ticketUnavailableException(
      TicketUnavailableException ex, WebRequest _request) {
    logException(HttpStatus.BAD_REQUEST, ex);
    return ApiResponse.error(ConstantExceptionCode.TICKET_UNAVAILABLE, ex.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ApiResponse<?> constraintViolationException(
      ConstraintViolationException ex, WebRequest _request) {
    logException(HttpStatus.BAD_REQUEST, ex);
    List<String> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(violation.getMessage());
    }

    return ApiResponse.error(ConstantExceptionCode.GENERAL_BAD_REQUEST, "BadRequest: " + errors);
  }

  // GeneralException Handling
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: '%s'",
        ex.getValue(),
        ex.getName(),
        Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
    logger.error("MethodArgumentTypeMismatchException: {}", errorMessage);
    return ApiResponse.error(ConstantExceptionCode.GENERAL_BAD_REQUEST, errorMessage);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    logger.error("MethodArgumentNotValidException: {}", errors);

    return ApiResponse.error(ConstantExceptionCode.GENERAL_BAD_REQUEST, errors.toString());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    String errorMessage = String.format("Missing required parameter: %s", ex.getParameterName());
    logger.error("MissingServletRequestParameterException: {}", errorMessage);
    return ApiResponse.error(ConstantExceptionCode.GENERAL_BAD_REQUEST, errorMessage);
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex) {
    var errors =
        ex.getAllValidationResults().stream()
            .map(ParameterValidationResult::getResolvableErrors)
            .flatMap(List::stream)
            .map(MessageSourceResolvable::getDefaultMessage)
            .peek(logger::error)
            .toList();
    String errorMessage = errors.toString();
    logger.error("HandlerMethodValidationException: {}", errorMessage);
    return ApiResponse.error(ConstantExceptionCode.GENERAL_BAD_REQUEST, errors.toString());
  }

  @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ApiResponse<?> noHandlerFoundException(Exception ex, WebRequest _request) {
    logException(HttpStatus.NOT_FOUND, ex);
    String msg = "Resource not found";
    return ApiResponse.error(ConstantExceptionCode.NOT_FOUND, msg);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public ApiResponse<?> authenticationException(Exception ex, WebRequest _request) {
    logException(HttpStatus.UNAUTHORIZED, ex);
    String msg = "Authentication failed";
    return ApiResponse.error(ConstantExceptionCode.GENERAL_AUTH_FAILED, msg);
  }

  @ExceptionHandler({Exception.class, Throwable.class})
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<?> globalExceptionHandler(Exception ex, WebRequest _request) {
    logException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    String msg = "Internal server error";
    return ApiResponse.error(ConstantExceptionCode.INTERNAL_SERVER_ERROR, msg);
  }

  private void logException(HttpStatus status, Throwable ex) {
    if (status.is4xxClientError()) {
      logger.warn("Client error: {}", ex.getMessage());
    } else {
      logger.error("Server error: {}", ex.getMessage(), ex);
    }
  }
}
