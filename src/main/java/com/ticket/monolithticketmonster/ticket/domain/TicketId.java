package com.ticket.monolithticketmonster.ticket.domain;

import java.util.UUID;
import java.util.regex.Pattern;
import lombok.Getter;

@Getter
public class TicketId {
  private final UUID id;
  private static final Pattern UUID_REGEX =
      Pattern.compile(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

  public TicketId(UUID id) {
    this.id = id;
  }

  public TicketId (String s) {
    if (s == null || UUID_REGEX.matcher(s).matches()) {
      throw new IllegalArgumentException("TicketId must be UUID type");
    }
    this.id = UUID.fromString(s);
  }

  public TicketId() {
    this(UUID.randomUUID());
  }
}
