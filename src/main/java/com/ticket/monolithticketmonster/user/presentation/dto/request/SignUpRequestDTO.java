package com.ticket.monolithticketmonster.user.presentation.dto.request;

import com.ticket.monolithticketmonster.user.domain.Password;
import com.ticket.monolithticketmonster.user.domain.Email;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
    Email email,
    Password password,
    @Size(max = 30, message = "Username can only have at most 30 chars") String username) {}
