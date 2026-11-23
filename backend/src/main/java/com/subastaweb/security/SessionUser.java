package com.subastaweb.security;

import com.subastaweb.domain.Role;
import java.io.Serializable;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/** Identidad guardada en la sesión. Nunca contiene la contraseña ni su hash. */
public record SessionUser(Long id, String fullName, String email, Role role) implements Serializable {

    public List<GrantedAuthority> authorities() {
        return List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
