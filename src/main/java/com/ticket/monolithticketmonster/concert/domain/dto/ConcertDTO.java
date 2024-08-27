package com.ticket.monolithticketmonster.concert.domain.dto;

import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConcertDTO(
    UUID id, String name, Long ticketCount, Double ticketPrice, LocalDateTime date) {}
