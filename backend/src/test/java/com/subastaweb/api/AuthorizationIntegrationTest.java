package com.subastaweb.api;

import com.subastaweb.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** T1-14: pruebas negativas 401 / 403 / 404 que no exponen datos privados. */
class AuthorizationIntegrationTest extends IntegrationTestBase {

    private static final String PASSWORD = "ClaveSegura123";

    @Test
    void adminRouteWithoutSessionReturns401() throws Exception {
        mvc.perform(get("/api/v1/admin/ping"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"));
    }

    @Test
    void adminRouteAsUserReturns403() throws Exception {
        createUser("user@example.test", PASSWORD, Role.USER);
        MockHttpSession session = login("user@example.test", PASSWORD);

        mvc.perform(get("/api/v1/admin/ping").session(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("user@example.test"))));
    }

    @Test
    void adminRouteAsAdminReturns200() throws Exception {
        createUser("admin@example.test", PASSWORD, Role.ADMIN);
        MockHttpSession session = login("admin@example.test", PASSWORD);

        mvc.perform(get("/api/v1/admin/ping").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void adminCannotCreateAuctionRequests() throws Exception {
        createUser("admin@example.test", PASSWORD, Role.ADMIN);
        MockHttpSession session = login("admin@example.test", PASSWORD);

        mvc.perform(multipart("/api/v1/auctions").session(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("FORBIDDEN"));
    }

    @Test
    void unknownPrivateRouteReturns401WithoutSessionAnd404WithSession() throws Exception {
        mvc.perform(get("/api/v1/no-existe"))
                .andExpect(status().isUnauthorized());

        createUser("user@example.test", PASSWORD, Role.USER);
        MockHttpSession session = login("user@example.test", PASSWORD);

        mvc.perform(get("/api/v1/no-existe").session(session))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.requestId").isNotEmpty());
    }
}
