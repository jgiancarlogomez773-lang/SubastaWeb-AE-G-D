package com.subastaweb.backend.api;

import com.subastaweb.backend.application.AuthService;
import com.subastaweb.backend.domain.Role;
import com.subastaweb.backend.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void loginValido_conCsrf_devuelve200_yPermiteConsultarMe() throws Exception {
        UserResponse user = new UserResponse(
                1L,
                "Yeison Gelvez",
                "yeison@subastaweb.com",
                Role.USER,
                Instant.parse("2026-11-21T13:00:00Z")
        );
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(user);
        when(authService.getByEmail("yeison@subastaweb.com")).thenReturn(user);

        String payload = "{\"email\":\"yeison@subastaweb.com\",\"password\":\"ClaveSegura123\"}";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("yeison@subastaweb.com"))
                .andExpect(jsonPath("$.fullName").value("Yeison Gelvez"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();
        assertThat(session.isInvalid()).isFalse();

        mockMvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("yeison@subastaweb.com"));
    }

    @Test
    void loginInvalido_conCsrf_devuelve401() throws Exception {
        when(authService.authenticate(any(LoginRequest.class)))
                .thenThrow(new AuthService.InvalidCredentialsException("Credenciales invalidas"));

        String payload = "{\"email\":\"yeison@subastaweb.com\",\"password\":\"Incorrecta\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void loginSinCsrf_devuelve403() throws Exception {
        String payload = "{\"email\":\"yeison@subastaweb.com\",\"password\":\"ClaveSegura123\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }

    @Test
    void meSinSesion_devuelve401() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @Test
    void logout_invalidaSesion_yBloqueaConsultasPosteriores() throws Exception {
        UserResponse user = new UserResponse(
                1L,
                "Yeison Gelvez",
                "yeison@subastaweb.com",
                Role.USER,
                Instant.parse("2026-11-21T13:00:00Z")
        );
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(user);

        String payload = "{\"email\":\"yeison@subastaweb.com\",\"password\":\"ClaveSegura123\"}";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        mockMvc.perform(post("/api/v1/auth/logout")
                        .with(csrf())
                        .session(session))
                .andExpect(status().isNoContent());

        assertThat(session.isInvalid()).isTrue();

        mockMvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isUnauthorized());
    }
}