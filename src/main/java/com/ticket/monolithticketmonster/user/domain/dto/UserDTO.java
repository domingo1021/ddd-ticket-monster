package com.ticket.monolithticketmonster.user.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/** UserDTO is a DTO that exclude password hash field from User entity. */
@Getter
@Setter
@Builder
public class UserDTO {
  private UUID userId;
  private String username;
  private String email;
  private Boolean isOAuth2User;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
