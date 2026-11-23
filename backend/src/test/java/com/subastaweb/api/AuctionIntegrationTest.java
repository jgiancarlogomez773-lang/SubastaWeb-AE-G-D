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
                                      Long categoryId) throws Exception {
        MockMultipartHttpServletRequestBuilder request = multipart("/api/v1/auctions");
        if (image != null) {
            request.file(image);
        }
        request.param("title", "Reloj antiguo de bolsillo");
        request.param("description", "Reloj en buen estado, funciona perfectamente.");
        request.param("categoryId", String.valueOf(categoryId));
        request.param("startingPrice", price);
        request.param("durationHours", "48");
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

    @Test
    void validRequestIsStoredAsPendingReview() throws Exception {
        postAuction(userSession(), png(), "50000", firstCategoryId())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDIENTE_REVISION"))
                .andExpect(jsonPath("$.startingPrice").value(50000));

        assertThat(auctions.findAll()).hasSize(1);
        assertThat(auctions.findAll().get(0).getStatus()).isEqualTo(AuctionStatus.PENDIENTE_REVISION);
        assertThat(uploadsCount()).isEqualTo(1);
    }

    @Test
    void requestWithoutSessionReturns401() throws Exception {
        postAuction(null, png(), "50000", firstCategoryId())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_REQUIRED"));
        assertThat(auctions.count()).isZero();
    }

    @Test
    void invalidImageReturns422WithoutPartialPersistence() throws Exception {
        MockMultipartFile fake = new MockMultipartFile("image", "foto.png", "image/png",
                "no soy una imagen".getBytes(StandardCharsets.UTF_8));

        postAuction(userSession(), fake, "50000", firstCategoryId())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));

        assertThat(auctions.count()).isZero();
        assertThat(uploadsCount()).isZero();
    }

    @Test
    void imageOverThreeMegabytesReturns422() throws Exception {
        byte[] big = new byte[3 * 1024 * 1024 + 1];
        System.arraycopy(PNG_HEADER, 0, big, 0, PNG_HEADER.length);
        MockMultipartFile tooBig = new MockMultipartFile("image", "grande.png", "image/png", big);

        postAuction(userSession(), tooBig, "50000", firstCategoryId())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));

        assertThat(auctions.count()).isZero();
        assertThat(uploadsCount()).isZero();
    }

    @Test
    void missingImageReturns422() throws Exception {
        postAuction(userSession(), null, "50000", firstCategoryId())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("INVALID_IMAGE"));
        assertThat(auctions.count()).isZero();
    }

    @Test
    void priceBelowMinimumReturns422() throws Exception {
        postAuction(userSession(), png(), "999", firstCategoryId())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("startingPrice"));

        assertThat(auctions.count()).isZero();
        assertThat(uploadsCount()).isZero();
    }

    @Test
    void nonNumericPriceReturns422() throws Exception {
        postAuction(userSession(), png(), "abc", firstCategoryId())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        assertThat(auctions.count()).isZero();
    }

    @Test
    void unknownCategoryReturns422WithoutPartialPersistence() throws Exception {
        postAuction(userSession(), png(), "50000", 999_999L)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details[0].field").value("categoryId"));

        assertThat(auctions.count()).isZero();
        assertThat(uploadsCount()).isZero();
    }
}
