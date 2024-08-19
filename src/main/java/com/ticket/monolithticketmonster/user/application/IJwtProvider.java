package com.ticket.monolithticketmonster.user.application;

import com.ticket.monolithticketmonster.user.domain.UserId;
import jakarta.servlet.http.HttpServletRequest;

public interface IJwtProvider {
  String generateToken(UserId userId);
  void validateToken(String token);
  String resolveToken(HttpServletRequest req);
  UserId getUserIdFromToken(String token);
}
