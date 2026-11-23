package com.subastaweb.application;

import com.subastaweb.domain.Auction;
import com.subastaweb.domain.AuctionStatus;
import com.subastaweb.domain.DomainException;
import com.subastaweb.domain.ErrorCode;
import com.subastaweb.domain.Role;
import com.subastaweb.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** T1-14: propiedad (403) y visibilidad de solicitudes privadas (404). */
class AuctionAccessPolicyTest {

    private final AuctionAccessPolicy policy = new AuctionAccessPolicy();

    private static Auction pendingAuctionOf(long sellerId) {
        User seller = new User("Vendedor", "vendedor@example.test", "hash", Role.USER);
        ReflectionTestUtils.setField(seller, "id", sellerId);
        return new Auction(seller, null, "Titulo", "Descripcion larga", 1000, 24, "x.png",
                AuctionStatus.PENDIENTE_REVISION);
    }

    @Test
    void ownerIsRecognized() {
        Auction auction = pendingAuctionOf(1L);
        assertThat(policy.isOwner(1L, auction)).isTrue();
        assertThat(policy.isOwner(2L, auction)).isFalse();
        assertThat(policy.isOwner(null, auction)).isFalse();
    }

    @Test
    void nonOwnerGetsForbiddenWithoutDetails() {
        Auction auction = pendingAuctionOf(1L);

        assertThatCode(() -> policy.requireOwner(1L, auction)).doesNotThrowAnyException();
        assertThatThrownBy(() -> policy.requireOwner(2L, auction))
                .isInstanceOfSatisfying(DomainException.class, e -> {
                    assertThat(e.code()).isEqualTo(ErrorCode.FORBIDDEN);
                    assertThat(e.details()).isEmpty();
                    assertThat(e.getMessage()).doesNotContain("vendedor@example.test");
                });
    }

    @Test
    void pendingAuctionIsHiddenFromOthersWith404() {
        Auction auction = pendingAuctionOf(1L);

        assertThatThrownBy(() -> policy.requireVisible(2L, Role.USER, auction))
                .isInstanceOfSatisfying(DomainException.class,
                        e -> assertThat(e.code()).isEqualTo(ErrorCode.AUCTION_NOT_PUBLIC));
        assertThatThrownBy(() -> policy.requireVisible(null, null, auction))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void pendingAuctionIsVisibleToOwnerAndAdmin() {
        Auction auction = pendingAuctionOf(1L);

        assertThatCode(() -> policy.requireVisible(1L, Role.USER, auction)).doesNotThrowAnyException();
        assertThatCode(() -> policy.requireVisible(99L, Role.ADMIN, auction)).doesNotThrowAnyException();
    }
}
