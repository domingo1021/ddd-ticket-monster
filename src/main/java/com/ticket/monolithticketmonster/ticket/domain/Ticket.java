package com.ticket.monolithticketmonster.ticket.domain;

import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.user.domain.AbstractAudit;
import com.ticket.monolithticketmonster.user.domain.UserId;
import java.time.LocalDateTime;
import org.jmolecules.event.annotation.Externalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tickets")
public class Ticket extends AbstractAggregateRoot<Ticket> {
  @EmbeddedId
  private TicketId id;

  @Embedded
  private TicketStatus status;
  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "user_id"))
  private UserId userId;
  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "concert_id"))
  private ConcertId concertId;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt;

  public Ticket(ConcertId concertId, UserId userId) {
    this.id = new TicketId();
    this.concertId = concertId;
    this.userId = userId;
    this.status = new TicketStatus(TicketStatus.RESERVED);
    registerEvent(new TicketReserved(userId, concertId));
  }
}
