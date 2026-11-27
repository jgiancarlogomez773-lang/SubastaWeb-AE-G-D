package com.subastaweb.api.dto;

import com.subastaweb.domain.Auction;
import java.time.Instant;

/** Detalle público de una subasta. No incluye ningún dato del vendedor (sin PII). */
public record PublicAuctionDetail(
        Long id,
        String title,
        String description,
        CategoryResponse category,
        long startingPrice,
        int durationHours,
        String status,
        Instant startsAt,
        Instant endsAt,
        Instant createdAt) {

    public static PublicAuctionDetail from(Auction auction) {
        return new PublicAuctionDetail(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                CategoryResponse.from(auction.getCategory()),
                auction.getStartingPrice(),
                auction.getDurationHours(),
                auction.getStatus().name(),
                auction.getStartsAt(),
                auction.getEndsAt(),
                auction.getCreatedAt());
    }
}
