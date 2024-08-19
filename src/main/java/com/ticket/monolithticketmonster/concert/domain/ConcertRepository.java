package com.ticket.monolithticketmonster.concert.domain;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface ConcertRepository extends CrudRepository<Concert, ConcertId> {
  @Modifying
  @Query("UPDATE Concert c SET c.ticketCapacity = c.ticketCapacity - 1 WHERE c.id = :id")
  int decrementTicketCapacity(ConcertId id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c.ticketCapacity FROM Concert c WHERE c.id = :id")
  Optional<Long> findByIdAndLock(ConcertId id);

  @Query("SELECT c.ticketCapacity FROM Concert c WHERE c.id = :id")
  Optional<Long> findTicketCapacityById(ConcertId id);
}
