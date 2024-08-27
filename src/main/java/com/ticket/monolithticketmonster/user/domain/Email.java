package com.ticket.monolithticketmonster.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

@NoArgsConstructor
@Getter
@Embeddable
public class Email {
  @Column(name = "email", length = 100, nullable = false, unique = true)
  private String value;
  private static final EmailValidator emailValidator = new EmailValidator();

  public Email(String value) {
    if (!emailValidator.isValid(value, null)) {
      throw new IllegalArgumentException("invalid email: " + value);
    }
    this.value = value;
  }

  public void setValue(String value) {
    if (!emailValidator.isValid(value, null)) {
      throw new IllegalArgumentException("invalid email: " + value);
    }
    this.value = value;
  }
}
