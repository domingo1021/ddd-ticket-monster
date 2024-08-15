package com.ticket.monolithticketmonster.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

  private Enum<?>[] enumValues;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    enumValues = constraintAnnotation.enumClass().getEnumConstants();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // validate by @NotNull
    }
    return Arrays.stream(enumValues).anyMatch(enumValue -> enumValue.name().equals(value));
  }
}
