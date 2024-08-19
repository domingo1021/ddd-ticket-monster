package com.ticket.monolithticketmonster.concert.domain.dto;

import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import java.time.LocalDateTime;

public record ConcertDTO(
    ConcertId id, String name, Long ticketCount, Double ticketPrice, LocalDateTime date) {}
