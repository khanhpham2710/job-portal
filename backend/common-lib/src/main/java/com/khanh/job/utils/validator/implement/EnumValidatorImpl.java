package com.khanh.job.utils.validator.implement;

import com.khanh.job.domain.enums.BaseEnum;
import com.khanh.job.domain.enums.EnumValidationType;
import com.khanh.job.utils.validator.EnumValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Object> {

  private Set<String> acceptedValues = Set.of();

  private boolean nullable = false;

  private EnumValidationType validationType = EnumValidationType.NAME;

  @Override
  public void initialize(EnumValidator annotation) {

    this.nullable = annotation.nullable();
    this.validationType = annotation.enumValidationType();

    this.acceptedValues =
        Arrays.stream(annotation.enumClass().getEnumConstants())
            .map(this::getValue)
            .collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {

    if (Objects.isNull(value)) {
      if (nullable) {
        return true;
      }

      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("Value must not be null")
          .addConstraintViolation();
    }

    if (acceptedValues.contains(value.toString())) {
      return true;
    }

    String defaultMessage = context.getDefaultConstraintMessageTemplate();

    if (!StringUtils.hasText(defaultMessage)) {
      context.disableDefaultConstraintViolation();

      String message =
          String.format(
              "Invalid value '%s'. Accepted values: %s", value, String.join(", ", acceptedValues));

      context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    return false;
  }

  private String getValue(Enum<?> enumValue) {

    return switch (validationType) {
      case NAME -> enumValue.name();

      case CODE -> {
        if (!(enumValue instanceof BaseEnum baseEnum)) {
          throw new IllegalArgumentException("Enum must implement BaseEnum when using CODE");
        }

        yield baseEnum.getCode();
      }

      case ORDINAL -> String.valueOf(enumValue.ordinal());
    };
  }
}
