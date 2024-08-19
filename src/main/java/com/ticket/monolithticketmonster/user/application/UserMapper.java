package com.ticket.monolithticketmonster.user.application;

import com.ticket.monolithticketmonster.user.domain.User;
import com.ticket.monolithticketmonster.user.domain.dto.UserDTO;

public class UserMapper {
  public static UserDTO convertUserEntityToDTO(User user) {
    return UserDTO.builder()
        .userId(user.getId().id())
        .username(user.getUsername())
        .email(user.getEmail().getValue())
        .isOAuth2User(user.getIsOAuth2User())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
