package com.ticket.monolithticketmonster.presentation.dto.request;

import com.ticket.monolithticketmonster.domain.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
    @NotNull @Email String email,
    @NotNull @ValidPassword String password,
    @Size(max = 30, message = "Username can only have at most 30 chars") String username) {}
