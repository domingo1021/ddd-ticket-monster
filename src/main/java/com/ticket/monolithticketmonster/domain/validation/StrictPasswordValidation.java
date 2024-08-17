package com.ticket.monolithticketmonster.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * This class is used to validate the password of the user.
 * Password should contain at least 8 characters with 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.
 */
public class StrictPasswordValidation  implements ConstraintValidator<ValidPassword, String> {
  static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";
  static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

  @Override
  public boolean isValid(String pwd, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate("Password should contain at least 8 characters with 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.")
        .addConstraintViolation();

    return PASSWORD_PATTERN.matcher(pwd).matches();
  }
}
