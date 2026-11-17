package com.khanh.job.utils.validator;

import com.khanh.job.domain.enums.EnumValidationType;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumValidator {

  Class<? extends Enum<?>> enumClass();

  EnumValidationType enumValidationType() default EnumValidationType.NAME;

  boolean nullable() default true;

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
