package com.subastaweb.api.dto;

import com.subastaweb.domain.User;
import com.subastaweb.security.SessionUser;

public record UserResponse(Long id, String fullName, String email, String role) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    public static UserResponse from(SessionUser user) {
        return new UserResponse(user.id(), user.fullName(), user.email(), user.role().name());
    }
}
