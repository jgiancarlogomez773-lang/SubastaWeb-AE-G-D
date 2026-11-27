package com.subastaweb.api.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** RN-06: la duración solo puede ser 24, 48 o 72 horas. */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedDurationValidator.class)
public @interface AllowedDuration {

    String message() default "La duración debe ser 24, 48 o 72 horas.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
