package com.ticket.monolithticketmonster.user.infrastructure.security;

import com.ticket.monolithticketmonster.user.application.exception.BaseAuthenticationException;
import com.ticket.monolithticketmonster.user.application.exception.ConstantExceptionCode;
import com.ticket.monolithticketmonster.user.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.user.presentation.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPoint implements
    org.springframework.security.web.AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

  private int getErrorCodeFromExceptionOrDefault(AuthenticationException ex) {
    if (ex instanceof JwtAuthFailedException) {
      return ((BaseAuthenticationException) ex).getErrorCode(); // Downcast to IErrorCodeProvider
    }
    return ConstantExceptionCode.GENERAL_AUTH_FAILED;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    int errorCode = getErrorCodeFromExceptionOrDefault(authException);

    // Customize the response as needed
    String responseMessage = ApiResponse.error(errorCode, authException.getMessage()).toJson();
    logger.warn("Authentication failed: {}", responseMessage);
    response.getWriter().write(responseMessage);
  }
}