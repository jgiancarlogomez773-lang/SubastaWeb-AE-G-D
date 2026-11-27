package com.subastaweb.api.dto;

import com.subastaweb.domain.Auction;
import java.time.Instant;

/** Elemento del catálogo público. No incluye ningún dato del vendedor. */
public record PublicAuctionSummary(
        Long id,
        String title,
        CategoryResponse category,
        long startingPrice,
        int durationHours,
        String status,
        Instant startsAt,
        Instant endsAt) {

    public static PublicAuctionSummary from(Auction auction) {
        return new PublicAuctionSummary(
                auction.getId(),
                auction.getTitle(),
                CategoryResponse.from(auction.getCategory()),
                auction.getStartingPrice(),
                auction.getDurationHours(),
                auction.getStatus().name(),
                auction.getStartsAt(),
                auction.getEndsAt());
    }
}
