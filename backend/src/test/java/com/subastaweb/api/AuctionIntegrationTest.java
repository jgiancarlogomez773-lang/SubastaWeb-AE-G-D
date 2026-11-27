package com.subastaweb.api;

import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.Role;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuctionIntegrationTest extends IntegrationTestBase {

    private static final String PASSWORD = "ClaveSegura123";
    private static final byte[] PNG_HEADER = {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 1, 2, 3, 4};

    private MockHttpSession userSession() throws Exception {
        createUser("user@example.test", PASSWORD, Role.USER);
        return login("user@example.test", PASSWORD);
    }

    private ResultActions postAuction(MockHttpSession session, MockMultipartFile image, String price,
                                      Long categoryId, String durationHours) throws Exception {
        MockMultipartHttpServletRequestBuilder request = multipart("/api/v1/auctions");
        if (image != null) {
            request.file(image);
        }
        request.param("title", "Reloj antiguo de bolsillo");
        request.param("description", "Reloj en buen estado, funciona perfectamente.");
        request.param("categoryId", String.valueOf(categoryId));
        request.param("startingPrice", price);
        request.param("durationHours", durationHours);
        if (session != null) {
            request.session(session);
        }
        return mvc.perform(request);
    }

    private Long firstCategoryId() {
        return categories.findByActiveTrueOrderByNameAsc().get(0).getId();
    }

    private MockMultipartFile png() {
        return new MockMultipartFile("image", "foto.png", "image/png", PNG_HEADER);
    }

    private void assertNothingPersisted() throws Exception {
        assertThat(auctions.count()).isZero();
        assertThat(auctionImages.count()).isZero();
        assertThat(uploadsCount()).isZero();
    }

    @Test
    void validRequestIsStoredAsPendingReviewWithItsImage() throws Exception {
        postAuction(userSession(), png(), "50000", firstCategoryId(), "48")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDIENTE_REVISION"))
                .andExpect(jsonPath("$.startingPrice").value(50000))
                .andExpect(jsonPath("$.durationHours").value(48));

        assertThat(auctions.findAll()).hasSize(1);
        assertThat(auctions.findAll().get(0).getStatus()).isEqualTo(AuctionStatus.PENDIENTE_REVISION);
        assertThat(auctionImages.findAll()).hasSize(1);
        assertThat(auctionImages.findAll().get(0).getMimeType()).isEqualTo("image/png");
        assertThat(auctionImages.findAll().get(0).getPosition()).isEqualTo(1);
        assertThat(uploadsCount()).isEqualTo(1);
    }

    @Test
    void allowedDurationsAre24And48And72() throws Exception {
        MockHttpSession session = userSession();
        for (String hours : new String[] {"24", "72"}) {
            postAuction(session, png(), "50000", firstCategoryId(), hours)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.durationHours").value(Integer.parseInt(hours)));
        }
    }

    @Test
    void durationOutsideAllowedValuesReturns422() throws Exception {
        postAuction(userSession(), png(), "50000", firstCategoryId(), "36")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("durationHours"));

        assertNothingPersisted();
    }

    @Test
    void requestWithoutSessionReturns401() throws Exception {
        postAuction(null, png(), "50000", firstCategoryId(), "48")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"));
        assertNothingPersisted();
    }

    @Test
    void invalidImageReturns422WithoutPartialPersistence() throws Exception {
        MockMultipartFile fake = new MockMultipartFile("image", "foto.png", "image/png",
                "no soy una imagen".getBytes(StandardCharsets.UTF_8));

        postAuction(userSession(), fake, "50000", firstCategoryId(), "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));

        assertNothingPersisted();
    }

    @Test
    void imageOverThreeMegabytesReturns422() throws Exception {
        byte[] big = new byte[3 * 1024 * 1024 + 1];
        System.arraycopy(PNG_HEADER, 0, big, 0, PNG_HEADER.length);
        MockMultipartFile tooBig = new MockMultipartFile("image", "grande.png", "image/png", big);

        postAuction(userSession(), tooBig, "50000", firstCategoryId(), "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));

        assertNothingPersisted();
    }

    @Test
    void missingImageReturns422() throws Exception {
        postAuction(userSession(), null, "50000", firstCategoryId(), "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));
        assertNothingPersisted();
    }

    @Test
    void priceBelowMinimumReturns422() throws Exception {
        postAuction(userSession(), png(), "999", firstCategoryId(), "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("startingPrice"));

        assertNothingPersisted();
    }

    @Test
    void nonNumericPriceReturns422() throws Exception {
        postAuction(userSession(), png(), "abc", firstCategoryId(), "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        assertNothingPersisted();
    }

    @Test
    void unknownCategoryReturns422WithoutPartialPersistence() throws Exception {
        postAuction(userSession(), png(), "50000", 999_999L, "48")
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("categoryId"));

        assertNothingPersisted();
    }
}
