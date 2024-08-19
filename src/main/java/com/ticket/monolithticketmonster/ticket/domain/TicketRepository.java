package com.ticket.monolithticketmonster.ticket.domain;

import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, TicketId> {}
