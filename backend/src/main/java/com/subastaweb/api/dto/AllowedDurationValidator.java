package com.subastaweb.api.dto;

import com.subastaweb.domain.AuctionRules;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AllowedDurationValidator implements ConstraintValidator<AllowedDuration, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // null lo controla @NotNull
        return value == null || AuctionRules.isAllowedDuration(value);
    }
}
