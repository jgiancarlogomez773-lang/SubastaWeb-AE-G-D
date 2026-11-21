package com.subastaweb.backend.api;

import com.subastaweb.backend.domain.Role;
import java.time.Instant;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        Role role,
        Instant createdAt
) {}
