package com.ticket.monolithticketmonster.user.domain;

import java.util.UUID;
import org.springframework.util.Assert;

public record UserId(UUID id) {

  public UserId {
    Assert.notNull(id, "User id must not be null");
  }

  public UserId() {
    this(UUID.randomUUID());
  }

}
