package com.ticket.monolithticketmonster.presentation.controller;

import com.ticket.monolithticketmonster.application.service.AuthService;
import com.ticket.monolithticketmonster.presentation.dto.ApiResponse;
import com.ticket.monolithticketmonster.presentation.dto.AuthResponseDTO;
import com.ticket.monolithticketmonster.presentation.dto.UserDTO;
import com.ticket.monolithticketmonster.presentation.dto.request.SignInRequestDTO;
import com.ticket.monolithticketmonster.presentation.dto.request.SignUpRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Validated @RequestBody SignUpRequestDTO dto) {
    var user = authService.signup(dto.email(), dto.password(), dto.username());
    return ResponseEntity.ok(ApiResponse.success(user));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Validated @RequestBody SignInRequestDTO dto) {
    var token = authService.signin(dto.email(), dto.password());
    return ResponseEntity.ok(ApiResponse.success(new AuthResponseDTO(token)));
  }

  @GetMapping("/validate")
  public ResponseEntity<?> validateToken(@AuthenticationPrincipal UserDTO user) {
    return ResponseEntity.ok(ApiResponse.success(user));
  }
}
