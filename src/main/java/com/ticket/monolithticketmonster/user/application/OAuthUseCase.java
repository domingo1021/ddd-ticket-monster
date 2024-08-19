package com.ticket.monolithticketmonster.user.application;

import com.ticket.monolithticketmonster.UseCase;
import com.ticket.monolithticketmonster.user.domain.Email;
import com.ticket.monolithticketmonster.user.domain.User;
import com.ticket.monolithticketmonster.user.domain.UserRepository;
import com.ticket.monolithticketmonster.user.domain.dto.UserDTO;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public class OAuthUseCase {
  private final UserRepository userRepository;

  public UserDTO handleOAuth2User(Email email, String username) {
    UserDTO user =
        userRepository.findByEmail(email).map(UserMapper::convertUserEntityToDTO).orElse(null);
    if (user != null) return user;

    User newUser = new User(email, username, true);
    userRepository.save(newUser);

    return UserMapper.convertUserEntityToDTO(newUser);
  }
}
