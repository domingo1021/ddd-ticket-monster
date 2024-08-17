package com.ticket.monolithticketmonster.presentation.dto.request;

import com.ticket.monolithticketmonster.domain.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInRequestDTO(
    @NotNull @Email String email, @NotNull @ValidPassword String password) {}
