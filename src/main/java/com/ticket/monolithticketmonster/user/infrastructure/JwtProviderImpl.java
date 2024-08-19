package com.ticket.monolithticketmonster.user.infrastructure;

import com.ticket.monolithticketmonster.user.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.user.application.IJwtProvider;
import com.ticket.monolithticketmonster.user.domain.UserId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProviderImpl implements IJwtProvider {

  private final SecretKey signingKey;
  private final String ISSUER = "Ticket Monster";

  @Value("${jwt.expiration}")
  private Long expiration;

  public JwtProviderImpl(@Value("${jwt.secret}") String secret) {
    this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
  }

  /**
   * reference: https://github.com/jwtk/jjwt
   *
   * @param userId the subject username
   * @return the generated token
   */
  public String generateToken(UserId userId) {
    return Jwts.builder()
        .issuer(this.ISSUER)
        .subject(userId.id().toString())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .issuedAt(new Date(System.currentTimeMillis()))
        .signWith(this.signingKey)
        .compact();
  }

  public void validateToken(String token) {
    var claims = getAllClaimsFromToken(token);
    if (isTokenExpired(claims) || !isTokenIssuedByUs(claims)) {
      throw new JwtAuthFailedException();
    }
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public UserId getUserIdFromToken(String token) {
    return new UserId(UUID.fromString(getClaimFromToken(token, Claims::getSubject)));
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().verifyWith(this.signingKey).build().parseSignedClaims(token).getPayload();
  }

  private Boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }

  private Boolean isTokenIssuedByUs(Claims claims) {
    return this.ISSUER.equals(claims.getIssuer());
  }
}
