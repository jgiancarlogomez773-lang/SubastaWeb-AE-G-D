package com.subastaweb.api.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** La duración de la subasta solo puede ser 24, 48 o 72 horas. */
@Documented
@Constraint(validatedBy = AllowedDurationValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedDuration {

    String message() default "La duración debe ser de 24, 48 o 72 horas.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
