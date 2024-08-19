package com.ticket.monolithticketmonster.user.presentation.dto.request;

import com.ticket.monolithticketmonster.user.domain.Email;
import com.ticket.monolithticketmonster.user.domain.Password;

public record SignInRequestDTO(Email email, Password password) {}
