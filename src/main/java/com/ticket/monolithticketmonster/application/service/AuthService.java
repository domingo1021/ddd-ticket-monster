package com.ticket.monolithticketmonster.application.service;

import com.ticket.monolithticketmonster.application.exception.JwtAuthFailedException;
import com.ticket.monolithticketmonster.application.exception.OAuthUserNoPwdException;
import com.ticket.monolithticketmonster.application.exception.UserAlreadyExistException;
import com.ticket.monolithticketmonster.application.exception.UserNotFoundException;
import com.ticket.monolithticketmonster.domain.model.User;
import com.ticket.monolithticketmonster.infrastructure.repository.UserRepository;
import com.ticket.monolithticketmonster.infrastructure.security.JwtProvider;
import com.ticket.monolithticketmonster.presentation.dto.UserDTO;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  public AuthService(
      UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO signup(String email, String password, String username) {
    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent() && existingUser.get().getIsOAuth2User()) {
      throw new OAuthUserNoPwdException("User with email " + email + " is an OAuth2 user");
    } else if (existingUser.isPresent()) {
      throw new UserAlreadyExistException("User with email " + email + " already exists");
    }

    User user = new User();
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setUsername(username);
    user.setIsOAuth2User(false);

    return UserMapper.convertUserEntityToDTO(userRepository.save(user));
  }

  public String signin(String email, String password) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Cannot find user with email " + email));

    if (user.getIsOAuth2User()) {
      throw new OAuthUserNoPwdException(
          "User with email " + email + " is an OAuth2 user, please sign in with OAuth2");
    }

    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new JwtAuthFailedException();
    }

    return jwtProvider.generateToken(user.getUserId());
  }
}
