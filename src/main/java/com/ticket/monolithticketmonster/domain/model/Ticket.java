package com.ticket.monolithticketmonster.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String concert;
  private String venue;
  private BigDecimal price;
  private LocalDateTime date;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  public Ticket() {}
}
