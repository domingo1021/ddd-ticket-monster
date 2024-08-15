package com.ticket.monolithticketmonster.infrastructure.security;

import com.ticket.monolithticketmonster.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.application.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final UserService userService;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  public JwtFilter(JwtProvider jwtProvider, UserService userService, AuthenticationEntryPoint authenticationEntryPoint) {
    this.jwtProvider = jwtProvider;
    this.userService = userService;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
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
}
