package com.subastaweb.domain;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuctionRejectedResubmitTest {

    private static Auction rejectedAuction(long id, int contentVersion, int rejectedContentVersion, int reviewRound) {
        User seller = new User("Vendedor", "vendedor@example.test", "hash", Role.USER);
        ReflectionTestUtils.setField(seller, "id", 1L);
        Category category = new Category("Electronica", true);
        ReflectionTestUtils.setField(category, "id", 10L);

        Auction auction = new Auction(seller, category, "Titulo inicial", "Descripcion valida", 50000L, 24, "img.png", AuctionStatus.RECHAZADA);
        ReflectionTestUtils.setField(auction, "id", id);
        ReflectionTestUtils.setField(auction, "contentVersion", contentVersion);
        ReflectionTestUtils.setField(auction, "rejectedContentVersion", rejectedContentVersion);
        ReflectionTestUtils.setField(auction, "reviewRound", reviewRound);
        return auction;
    }

    @Test
    void applyRejectedEditUpdatesFieldsAndIncrementsContentVersion() {
        Auction auction = rejectedAuction(100L, 1, 1, 1);
        Category newCategory = new Category("Hogar", true);
        ReflectionTestUtils.setField(newCategory, "id", 20L);

        auction.applyRejectedEdit("Titulo corregido", "Descripcion corregida", newCategory, 75000L, 48);

        assertThat(auction.getTitle()).isEqualTo("Titulo corregido");
        assertThat(auction.getDescription()).isEqualTo("Descripcion corregida");
        assertThat(auction.getCategory()).isEqualTo(newCategory);
        assertThat(auction.getStartingPrice()).isEqualTo(75000L);
        assertThat(auction.getDurationHours()).isEqualTo(48);
        assertThat(auction.getContentVersion()).isEqualTo(2);
    }

    @Test
    void markResubmittedMovesToPendingReviewAndIncrementsReviewRound() {
        Auction auction = rejectedAuction(101L, 2, 1, 1);

        auction.markResubmitted();

        assertThat(auction.getStatus()).isEqualTo(AuctionStatus.PENDIENTE_REVISION);
        assertThat(auction.getReviewRound()).isEqualTo(2);
    }

    @Test
    void rejectedAuctionHasExpectedVersionFields() {
        Auction auction = rejectedAuction(102L, 3, 2, 2);

        assertThat(auction.getContentVersion()).isEqualTo(3);
        assertThat(auction.getRejectedContentVersion()).isEqualTo(2);
        assertThat(auction.getReviewRound()).isEqualTo(2);
        assertThatThrownBy(() -> {
            if (auction.getContentVersion() <= auction.getRejectedContentVersion()) {
                throw new DomainException(ErrorCode.AUCTION_CONFLICT, "Sin cambios respecto a la version rechazada");
            }
        }).isInstanceOf(DomainException.class)
          .extracting(e -> ((DomainException) e).code())
          .isEqualTo(ErrorCode.AUCTION_CONFLICT);
    }
}