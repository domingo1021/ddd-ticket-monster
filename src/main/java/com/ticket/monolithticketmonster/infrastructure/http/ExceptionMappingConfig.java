package com.ticket.monolithticketmonster.infrastructure.http;

import com.ticket.monolithticketmonster.application.exception.*;
import com.ticket.monolithticketmonster.presentation.dto.ApiResponse;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Configuration
public class ExceptionMappingConfig {

  @Bean
  public Map<Class<? extends Throwable>, HttpStatus> exceptionStatusMapper() {
    return Map.of(
        IllegalArgumentException.class,
        HttpStatus.BAD_REQUEST,
        TicketUnavailableException.class,
        HttpStatus.BAD_REQUEST,
        MethodArgumentNotValidException.class,
        HttpStatus.BAD_REQUEST,
        JwtAuthFailedException.class,
        HttpStatus.UNAUTHORIZED,
        UserNotFoundException.class,
        HttpStatus.UNAUTHORIZED,
        UserAlreadyExistException.class,
        HttpStatus.UNAUTHORIZED,
        OAuthUserNoPwdException.class,
        HttpStatus.UNAUTHORIZED);
  }

  @Bean
  public Map<Class<? extends Throwable>, ApiResponse<Void>> exceptionResponseMapper() {
    return Map.of(
        IllegalArgumentException.class, ApiResponse.error(40000, "Invalid request"),
        MethodArgumentNotValidException.class, ApiResponse.error(40001, "Invalid request"),
        TicketUnavailableException.class, ApiResponse.error(40002, "Ticket is unavailable"),
        JwtAuthFailedException.class, ApiResponse.error(40100, "Authentication failed"),
        UserAlreadyExistException.class, ApiResponse.error(40101, "User already exists"),
        OAuthUserNoPwdException.class,
            ApiResponse.error(40102, "User uses password approach to login"),
        UserNotFoundException.class, ApiResponse.error(40103, "User not found"));
  }
}
