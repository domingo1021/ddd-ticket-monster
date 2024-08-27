package com.ticket.monolithticketmonster.user.application;

import com.ticket.monolithticketmonster.user.domain.UserId;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface IJwtProvider {
  String generateToken(UserId userId);
  Claims validateToken(String token);
  String resolveToken(HttpServletRequest req);
  UserId getUserId(Claims claims);
}
