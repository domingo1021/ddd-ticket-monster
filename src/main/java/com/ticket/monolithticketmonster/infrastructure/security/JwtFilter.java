package com.ticket.monolithticketmonster.infrastructure.security;

import com.ticket.monolithticketmonster.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.application.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final UserService userService;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final RequestAuthenticationWhitelist requestAuthenticationWhitelist;

  public JwtFilter(
      JwtProvider jwtProvider,
      UserService userService,
      AuthenticationEntryPoint authenticationEntryPoint,
      RequestAuthenticationWhitelist requestAuthenticationWhitelist) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.requestAuthenticationWhitelist = requestAuthenticationWhitelist;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Skip authentication If isWhitelisted
    if (this.isRequestWhitelist(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = jwtProvider.resolveToken(request);
      if (token == null) {
        throw new JwtAuthFailedException("Bearer token not found");
      }
      jwtProvider.validateToken(token);

      String userId = jwtProvider.getUserIdFromToken(token);
      var userDetails = userService.getUserById(Long.parseLong(userId));

      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(userDetails, null, null);
      auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(auth);

      filterChain.doFilter(request, response);
    } catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();

      authenticationEntryPoint.commence(request, response, ex);
    } catch (AccessDeniedException ex) {
      SecurityContextHolder.clearContext();

      response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
    }
  }

  private boolean isRequestWhitelist(HttpServletRequest request) {
    HttpMethod method;
    try {
      method = HttpMethod.valueOf(request.getMethod());
    } catch (IllegalArgumentException e) {
      return false;
    }

    String requestUri = request.getRequestURI();

    return requestAuthenticationWhitelist
        .getAuthenticationMatcher()
        .getOrDefault(method, List.of())
        .stream()
        .anyMatch(pattern -> new AntPathMatcher().match(pattern, requestUri));
  }
}
