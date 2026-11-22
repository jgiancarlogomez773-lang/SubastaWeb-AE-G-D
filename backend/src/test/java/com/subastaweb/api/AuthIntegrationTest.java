package com.subastaweb.api;

import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegrationTestBase {

    private static final String PASSWORD = "ClaveSegura123";

    private String registerBody(String email) throws Exception {
        return jsonOf("fullName", "Ana Pérez", "email", email, "password", PASSWORD);
    }

    @Test
    void registerCreatesUserWithHashedPassword() throws Exception {
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("Ana@Example.test")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana@example.test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        User stored = users.findByEmail("ana@example.test").orElseThrow();
        assertThat(stored.getPasswordHash()).startsWith("$2").isNotEqualTo(PASSWORD);
    }

    @Test
    void registerWithSameEmailIgnoringCaseReturns409() throws Exception {
        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                .content(registerBody("ana@example.test"))).andExpect(status().isCreated());

        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody("ANA@example.TEST")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_REGISTERED"))
                .andExpect(jsonPath("$.requestId").isNotEmpty());
    }

    @Test
    void registerIgnoresRequestedAdminRole() throws Exception {
        String body = "{\"fullName\":\"Ana Pérez\",\"email\":\"ana@example.test\","
                + "\"password\":\"" + PASSWORD + "\",\"role\":\"ADMIN\"}";

        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"));
        assertThat(users.findByEmail("ana@example.test").orElseThrow().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void registerWithInvalidDataReturns422() throws Exception {
        String body = jsonOf("fullName", "Ana Pérez", "email", "no-es-un-correo", "password", "123");

        mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details.length()").value(2));
        assertThat(users.count()).isZero();
    }

    @Test
    void loginThenMeThenLogout() throws Exception {
        createUser("ana@example.test", PASSWORD, Role.USER);

        MockHttpSession session = login("ANA@example.test", PASSWORD);

        mvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ana@example.test"))
                .andExpect(jsonPath("$.role").value("USER"));

        mvc.perform(post("/api/v1/auth/logout").session(session))
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"));
    }

    @Test
    void loginWithWrongPasswordOrUnknownEmailReturns401() throws Exception {
        createUser("ana@example.test", PASSWORD, Role.USER);

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf("email", "ana@example.test", "password", "otra-clave-123")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));

        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf("email", "nadie@example.test", "password", PASSWORD)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void meAndLogoutWithoutSessionReturn401() throws Exception {
        mvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"))
                .andExpect(jsonPath("$.requestId").isNotEmpty());

        mvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isUnauthorized());
    }
}
