package com.ticket.monolithticketmonster.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User extends AbstractAudit {
  @EmbeddedId private UserId id;

  @Embedded
  private Email email;

  @Embedded
  private PasswordHash passwordHash;

  @Column(name = "username", length = 50)
  private String username;

  @Column(columnDefinition = "Boolean default false")
  private Boolean isOAuth2User;

  public User(
      Email email,
      Password password,
      String username,
      Boolean isOAuth2User,
      PasswordEncoder encoder) {
    Assert.notNull(email, "email must not be null");
    if (!isOAuth2User) {
      Assert.notNull(password, "password must not be null");
      Assert.notNull(encoder, "Password encoder must not be null");
    }

    this.id = new UserId();
    this.email = email;
    if (password != null) this.passwordHash = PasswordHash.create(password, encoder);
    this.username = username;
    this.isOAuth2User = isOAuth2User;
  }

  public User(Email email, String username, Boolean isOAuth2User) {
    this(email, null, username, isOAuth2User, null);
  }

  @Override
  public String toString() {
    return "User{"
        + "userId="
        + id
        + ", email='"
        + email
        + '\''
        + ", username='"
        + username
        + '\''
        + ", isOAuth2User="
        + isOAuth2User
        + '}';
  }
}
