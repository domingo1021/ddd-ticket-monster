package com.ticket.monolithticketmonster.concert.domain;

import com.ticket.monolithticketmonster.user.domain.AbstractAudit;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "concerts")
public class Concert extends AbstractAudit {
  @EmbeddedId
  private ConcertId id;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(nullable = false)
  private Long ticketCapacity;

  @Column(nullable = false, updatable = false)
  private Double ticketPrice;

  @Column(nullable = false)
  private LocalDateTime date;

  public Concert(String name, Long ticketCapacity, Double ticketPrice, LocalDateTime date) {
    this.id = new ConcertId();
    this.name = name;
    this.ticketCapacity = ticketCapacity;
    this.ticketPrice = ticketPrice;
    this.date = date;
  }
}
