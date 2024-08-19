package com.ticket.monolithticketmonster.user.application;

import com.ticket.monolithticketmonster.UseCase;
import com.ticket.monolithticketmonster.user.application.exception.UserNotFoundException;
import com.ticket.monolithticketmonster.user.domain.Email;
import com.ticket.monolithticketmonster.user.domain.Password;
import com.ticket.monolithticketmonster.user.domain.User;
import com.ticket.monolithticketmonster.user.domain.UserId;
import com.ticket.monolithticketmonster.user.domain.UserRepository;
import com.ticket.monolithticketmonster.user.application.exception.AuthenticationException;
import com.ticket.monolithticketmonster.user.application.exception.OAuthUserNoPwdException;
import com.ticket.monolithticketmonster.user.application.exception.UserAlreadyExistException;
import com.ticket.monolithticketmonster.user.domain.dto.UserDTO;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@UseCase
@AllArgsConstructor
public class AuthUseCase {
  private final UserRepository userRepository;
  private final IJwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;

  public UserDTO signup(Email email, Password password, String username) {
    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent() && existingUser.get().getIsOAuth2User()) {
      throw new OAuthUserNoPwdException("User with email " + email + " is an OAuth2 user");
    } else if (existingUser.isPresent()) {
      throw new UserAlreadyExistException("User with email " + email + " already exists");
    }

    var user = new User(email, password, username, false, passwordEncoder);

    return UserMapper.convertUserEntityToDTO(userRepository.save(user));
  }

  public String signin(Email email, Password password) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () -> new AuthenticationException("Invalid email(" + email + ") or password"));

    if (user.getIsOAuth2User()) {
      throw new OAuthUserNoPwdException(
          "User with email " + email + " is an OAuth2 user, please sign in with OAuth2");
    }

    if (!passwordEncoder.matches(password.getValue(), user.getPasswordHash().value())) {
      throw new AuthenticationException("Invalid email(" + email + ") or password");
    }

    return jwtProvider.generateToken(user.getId());
  }

  public UserDTO getUserById(UserId id) {
    return userRepository
        .findById(id)
        .map(UserMapper::convertUserEntityToDTO)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
  }
}
