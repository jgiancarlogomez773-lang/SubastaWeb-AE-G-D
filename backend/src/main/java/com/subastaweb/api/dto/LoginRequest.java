package com.subastaweb.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio.") String email,
        @NotBlank(message = "La contraseña es obligatoria.") String password) {

    @Override
    public String toString() {
        return "LoginRequest[email=" + email + "]";
    }
}
