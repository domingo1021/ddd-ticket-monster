package com.ticket.monolithticketmonster.application.service;

import com.ticket.monolithticketmonster.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.application.exception.OAuthUserNoPwdException;
import com.ticket.monolithticketmonster.application.exception.UserNotFoundException;
import com.ticket.monolithticketmonster.domain.model.User;
import com.ticket.monolithticketmonster.infrastructure.repository.UserRepository;
import com.ticket.monolithticketmonster.presentation.dto.UserDTO;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class  UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public UserDTO getUserById(Long id) {
    return userRepository
        .findById(id)
        .map(UserMapper::convertUserEntityToDTO)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  public User getRawUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }

  public void authenticateUser(String email, String password) {
    Optional<User> userOptional = userRepository.findByEmail(email);
    if (userOptional.isEmpty()) throw new JwtAuthFailedException();

    var user = userOptional.get();
    if (user.getIsOAuth2User()) {
      throw new OAuthUserNoPwdException(
          String.format("User with email %s is an OAuth2 user", email));
    }

    if (!this.passwordEncoder.matches(password, user.getPassword())) {
      throw new JwtAuthFailedException();
    }
  }

  public UserDTO handleOAuth2User(String email, String username) {
    UserDTO user =
        userRepository.findByEmail(email).map(UserMapper::convertUserEntityToDTO).orElse(null);
    if (user != null) return user;

    User newUser = new User();
    newUser.setEmail(email);
    newUser.setUsername(username);
    newUser.setIsOAuth2User(true);
    userRepository.save(newUser);

    return UserMapper.convertUserEntityToDTO(newUser);
  }
}
