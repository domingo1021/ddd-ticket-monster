package com.ticket.monolithticketmonster.user.presentation.handler;

import com.ticket.monolithticketmonster.user.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.user.application.exception.OAuthUserNoPwdException;
import com.ticket.monolithticketmonster.user.application.exception.UserAlreadyExistException;
import com.ticket.monolithticketmonster.user.presentation.dto.ApiResponse;
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
        MethodArgumentNotValidException.class,
        HttpStatus.BAD_REQUEST,
        JwtAuthFailedException.class,
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
        JwtAuthFailedException.class, ApiResponse.error(40100, "Authentication failed"),
        UserAlreadyExistException.class, ApiResponse.error(40101, "User already exists"),
        OAuthUserNoPwdException.class,
            ApiResponse.error(40102, "User uses password approach to login"));
  }
}
