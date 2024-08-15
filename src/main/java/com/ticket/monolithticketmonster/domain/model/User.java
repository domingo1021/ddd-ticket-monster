package com.ticket.monolithticketmonster.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;
  private String username;

  @Column(columnDefinition = "Boolean default false")
  private Boolean isOAuth2User;

  public User() {}

  public User(Long userId, String username, String password, String email, Boolean isOAuth2User) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.email = email;
    this.isOAuth2User = isOAuth2User;
  }

  @Override
  public String toString() {
    return "User{"
        + "userId="
        + userId
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
