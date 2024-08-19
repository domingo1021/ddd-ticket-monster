package com.ticket.monolithticketmonster.ticket.domain;

import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.user.domain.UserId;

public record TicketReserved(UserId userId, ConcertId concertId) {}
