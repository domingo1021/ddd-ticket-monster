package com.ticket.monolithticketmonster.concert.presentation;

import com.ticket.monolithticketmonster.concert.application.ConcertUseCase;
import com.ticket.monolithticketmonster.concert.domain.ConcertId;
import com.ticket.monolithticketmonster.user.presentation.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concerts")
@AllArgsConstructor
public class ConcertController {

  private final ConcertUseCase concertUseCase;

  @GetMapping
  public ResponseEntity<?> getAllConcerts() {
    return ResponseEntity.ok(ApiResponse.success(concertUseCase.getAllConcerts()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getConcertById(@Valid @PathVariable("id") @UUID String id) {
    return ResponseEntity.ok(ApiResponse.success(concertUseCase.getConcertById(new ConcertId(id))));
  }
}
