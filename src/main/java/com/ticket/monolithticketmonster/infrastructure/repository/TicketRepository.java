package com.ticket.monolithticketmonster.infrastructure.repository;

import com.ticket.monolithticketmonster.domain.model.Ticket;
import com.ticket.monolithticketmonster.domain.model.TicketStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.status = :status")
  Optional<Ticket> findByIdAndLock(Long id, TicketStatus status);

  Optional<Ticket> findById(Long id);

  @Query("SELECT t FROM Ticket t WHERE t.status = :status")
  Optional<List<Ticket>> findByStatus(TicketStatus status);
}
