package com.ticket.monolithticketmonster.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

  private List<String> enums;

  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    enums = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && enums.contains(value);
  }
}
