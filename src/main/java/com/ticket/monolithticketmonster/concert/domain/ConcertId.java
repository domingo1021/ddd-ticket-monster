package com.ticket.monolithticketmonster.concert.domain;

import java.util.UUID;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class ConcertId {
  private final UUID id;
  private static final Pattern UUID_REGEX =
      Pattern.compile(
          "[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");

  public ConcertId(UUID id) {
    this.id = id;
  }

  public ConcertId (String s) {
    if (s == null || !UUID_REGEX.matcher(s).matches()) {
      throw new IllegalArgumentException("ConcertId must be UUID type");
    }
    this.id = UUID.fromString(s);
  }

  public ConcertId() {
    this(UUID.randomUUID());
  }
}
