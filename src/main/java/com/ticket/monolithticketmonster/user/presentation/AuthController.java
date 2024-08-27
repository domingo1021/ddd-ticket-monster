package com.ticket.monolithticketmonster.user.presentation;

import com.ticket.monolithticketmonster.user.application.AuthUseCase;
import com.ticket.monolithticketmonster.user.presentation.dto.ApiResponse;
import com.ticket.monolithticketmonster.user.presentation.dto.AuthResponseDTO;
import com.ticket.monolithticketmonster.user.domain.dto.UserDTO;
import com.ticket.monolithticketmonster.user.presentation.dto.request.SignInRequestDTO;
import com.ticket.monolithticketmonster.user.presentation.dto.request.SignUpRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthUseCase authUseCase;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Validated @RequestBody SignUpRequestDTO dto) {
    var user = authUseCase.signup(dto.email(), dto.password(), dto.username());
    return ResponseEntity.ok(ApiResponse.success(user));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@Validated @RequestBody SignInRequestDTO dto) {
    var token = authUseCase.signin(dto.email(), dto.password());
    return ResponseEntity.ok(ApiResponse.success(new AuthResponseDTO(token)));
  }

  @GetMapping("/validate")
  public ResponseEntity<?> validateToken(@AuthenticationPrincipal UserDTO user) {
    return ResponseEntity.ok(ApiResponse.success(user));
  }
}
