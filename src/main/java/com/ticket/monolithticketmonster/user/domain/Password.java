package com.ticket.monolithticketmonster.user.domain;

import java.util.regex.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Password {
  private String value;
  public static final Pattern PASSWORD_PATTERN =
      Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$");

  public Password(String value) {
    this.value = value;
    if (!this.isValid(value)) {
      throw new IllegalArgumentException(
          "Password should contain at least 8 characters with 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.");
    }
  }

  public void setValue(String value) {
    if (!this.isValid(value)) {
      throw new IllegalArgumentException("invalid email: " + value);
    }
    this.value = value;
  }

  private boolean isValid(String pwd) {
    System.out.println("Password: " + pwd);
    return PASSWORD_PATTERN.matcher(pwd).matches();
  }
}
