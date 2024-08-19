package com.ticket.monolithticketmonster.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
public record PasswordHash(
    @Column(name = "password_hash", columnDefinition = "CHAR(60)") String value) {
  public PasswordHash(String value) {
    this.value = value;
  }

  // Static Factory Method
  public static PasswordHash create(Password pwd, PasswordEncoder encoder) {
    return new PasswordHash(encoder.encode(pwd.getValue()));
  }
}
