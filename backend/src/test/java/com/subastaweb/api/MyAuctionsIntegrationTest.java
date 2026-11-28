package com.subastaweb.api;
import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.Category;
import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
class MyAuctionsIntegrationTest extends IntegrationTestBase {
    private static final String PASSWORD = "ClaveSegura123";
    private Category firstCategory() {
        return categories.findByActiveTrueOrderByNameAsc().get(0);
    }
    @Test
    void withoutSessionReturns401() throws Exception {
        mvc.perform(get("/api/v1/auctions/mine"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void ownerSeesOnlyTheirOwnAuctionsAndStatus() throws Exception {
        User owner = createUser("owner@example.test", PASSWORD, Role.USER);
        User other = createUser("other@example.test", PASSWORD, Role.USER);
        Category category = firstCategory();
        auctions.save(new Auction(owner, category, "Camara analogica 35mm",
                "Camara fotografica clasica con lente 50mm en excelente estado.",
                120000L, 24, "camara.png", AuctionStatus.PENDIENTE_REVISION));
        auctions.save(new Auction(owner, category, "Tornamesa vintage",
                "Tornamesa funcionando perfectamente con capsula nueva.",
                250000L, 72, "tornamesa.png", AuctionStatus.RECHAZADA));
        auctions.save(new Auction(other, category, "Bicicleta de ruta",
                "Bicicleta ligera en aluminio lista para rodar sin fallas.",
                800000L, 48, "bici.png", AuctionStatus.PENDIENTE_REVISION));
        MockHttpSession ownerSession = login("owner@example.test", PASSWORD);
        String body = mvc.perform(get("/api/v1/auctions/mine").session(ownerSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(body).contains("Camara analogica 35mm", "Tornamesa vintage", "PENDIENTE_REVISION", "RECHAZADA");
        assertThat(body).doesNotContain("Bicicleta de ruta");
    }
    @Test
    void otherUserCannotAccessAnotherUsersAuctions() throws Exception {
        User firstUser = createUser("first@example.test", PASSWORD, Role.USER);
        User secondUser = createUser("second@example.test", PASSWORD, Role.USER);
        Category category = firstCategory();
        auctions.save(new Auction(firstUser, category, "Consola retro coleccion",
                "Consola clasica con dos controles y cables originales.",
                180000L, 48, "consola.png", AuctionStatus.PENDIENTE_REVISION));
        MockHttpSession secondSession = login("second@example.test", PASSWORD);
        mvc.perform(get("/api/v1/auctions/mine").session(secondSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}