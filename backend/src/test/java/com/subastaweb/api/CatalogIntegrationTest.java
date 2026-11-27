package com.subastaweb.api;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.Category;
import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** T2-13 (catálogo público) y T2-14 (detalle protegido por estado). */
class CatalogIntegrationTest extends IntegrationTestBase {

    private static final String PASSWORD = "ClaveSegura123";
    private static final String SELLER_EMAIL = "vendedor@example.test";

    private User seller() {
        return users.findByEmail(SELLER_EMAIL).orElseGet(() -> createUser(SELLER_EMAIL, PASSWORD, Role.USER));
    }

    private Auction saveAuction(String title, AuctionStatus status) {
        Category category = categories.findByActiveTrueOrderByNameAsc().get(0);
        Auction auction = new Auction(seller(), category, title,
                "Descripción de prueba de la subasta " + title, 50000, 48, status);
        if (status.isPublicVisible()) {
            Instant start = Instant.now().minus(1, ChronoUnit.HOURS);
            ReflectionTestUtils.setField(auction, "startsAt", start);
            ReflectionTestUtils.setField(auction, "endsAt", start.plus(48, ChronoUnit.HOURS));
        }
        return auctions.save(auction);
    }

    // ---------- T2-13: catálogo ----------

    @Test
    void catalogShowsOnlyActiveAndFinalizedAuctionsWithoutSession() throws Exception {
        saveAuction("Pendiente uno", AuctionStatus.PENDIENTE_REVISION);
        saveAuction("Rechazada uno", AuctionStatus.RECHAZADA);
        saveAuction("Activa uno", AuctionStatus.ACTIVA);
        saveAuction("Finalizada uno", AuctionStatus.FINALIZADA);

        mvc.perform(get("/api/v1/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[?(@.status == 'PENDIENTE_REVISION')]").isEmpty())
                .andExpect(jsonPath("$.items[?(@.status == 'RECHAZADA')]").isEmpty())
                .andExpect(jsonPath("$.items[0].category.name").isNotEmpty())
                .andExpect(jsonPath("$.items[0].seller").doesNotExist())
                .andExpect(content().string(Matchers.not(Matchers.containsString(SELLER_EMAIL))));
    }

    @Test
    void catalogIsPaginatedNewestFirst() throws Exception {
        saveAuction("Subasta 1", AuctionStatus.ACTIVA);
        saveAuction("Subasta 2", AuctionStatus.ACTIVA);
        saveAuction("Subasta 3", AuctionStatus.ACTIVA);

        mvc.perform(get("/api/v1/auctions").param("page", "0").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].title").value("Subasta 3"))
                .andExpect(jsonPath("$.items[1].title").value("Subasta 2"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalItems").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));

        mvc.perform(get("/api/v1/auctions").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].title").value("Subasta 1"));
    }

    @Test
    void emptyCatalogReturnsEmptyPage() throws Exception {
        saveAuction("Solo pendiente", AuctionStatus.PENDIENTE_REVISION);

        mvc.perform(get("/api/v1/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.totalItems").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void invalidPagingParametersReturn422() throws Exception {
        mvc.perform(get("/api/v1/auctions").param("size", "0"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("size"));
        mvc.perform(get("/api/v1/auctions").param("size", "51"))
                .andExpect(status().isUnprocessableEntity());
        mvc.perform(get("/api/v1/auctions").param("page", "-1"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.details[0].field").value("page"));
        mvc.perform(get("/api/v1/auctions").param("size", "abc"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("size"));
    }

    // ---------- T2-14: detalle ----------

    @Test
    void publicAuctionDetailIsVisibleToAnyoneWithoutSellerData() throws Exception {
        Auction auction = saveAuction("Activa detalle", AuctionStatus.ACTIVA);

        mvc.perform(get("/api/v1/auctions/" + auction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(auction.getId()))
                .andExpect(jsonPath("$.title").value("Activa detalle"))
                .andExpect(jsonPath("$.description").isNotEmpty())
                .andExpect(jsonPath("$.status").value("ACTIVA"))
                .andExpect(jsonPath("$.startingPrice").value(50000))
                .andExpect(jsonPath("$.seller").doesNotExist())
                .andExpect(jsonPath("$.sellerId").doesNotExist())
                .andExpect(content().string(Matchers.not(Matchers.containsString(SELLER_EMAIL))));
    }

    @Test
    void privateAuctionDetailReturns404ToAnonymousAndToOtherUsers() throws Exception {
        Auction pending = saveAuction("Pendiente detalle", AuctionStatus.PENDIENTE_REVISION);
        Auction rejected = saveAuction("Rechazada detalle", AuctionStatus.RECHAZADA);
        createUser("otro@example.test", PASSWORD, Role.USER);
        MockHttpSession other = login("otro@example.test", PASSWORD);

        for (Auction auction : new Auction[] {pending, rejected}) {
            mvc.perform(get("/api/v1/auctions/" + auction.getId()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("AUCTION_NOT_PUBLIC"))
                    .andExpect(content().string(Matchers.not(Matchers.containsString(auction.getTitle()))));
            mvc.perform(get("/api/v1/auctions/" + auction.getId()).session(other))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("AUCTION_NOT_PUBLIC"));
        }
    }

    @Test
    void privateAuctionDetailIsVisibleOnlyToItsOwner() throws Exception {
        Auction pending = saveAuction("Pendiente propia", AuctionStatus.PENDIENTE_REVISION);
        MockHttpSession sellerSession = login(SELLER_EMAIL, PASSWORD);
        createUser("admin@example.test", PASSWORD, Role.ADMIN);
        MockHttpSession adminSession = login("admin@example.test", PASSWORD);

        mvc.perform(get("/api/v1/auctions/" + pending.getId()).session(sellerSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDIENTE_REVISION"));
        // El ADMIN revisa por /admin/auctions/pending; este detalle es "Público o dueño".
        mvc.perform(get("/api/v1/auctions/" + pending.getId()).session(adminSession))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("AUCTION_NOT_PUBLIC"));
    }

    @Test
    void unknownOrMalformedIdReturns404() throws Exception {
        mvc.perform(get("/api/v1/auctions/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("AUCTION_NOT_PUBLIC"));

        // Un id que no es número no entra a la ruta pública: sin sesión exige iniciar sesión.
        mvc.perform(get("/api/v1/auctions/abc"))
                .andExpect(status().isUnauthorized());
    }
}
