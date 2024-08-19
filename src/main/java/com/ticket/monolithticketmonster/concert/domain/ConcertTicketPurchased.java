package com.ticket.monolithticketmonster.concert.domain;

import com.ticket.monolithticketmonster.user.domain.UserId;

public record ConcertTicketPurchased(ConcertId concertId, UserId userId) {}

