package com.ticket.monolithticketmonster.ticket.presentation.dto;

public record TicketPurchaseResponseDTO(
    String ticketId,
    String ticketStatus,
    String userId,
    String concertId,
    String createdAt,
    String updatedAt) {}
