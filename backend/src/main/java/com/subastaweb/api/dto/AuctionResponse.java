package com.subastaweb.api.dto;

import com.subastaweb.domain.Auction;
import java.time.Instant;

public record AuctionResponse(
        Long id,
        String title,
        String description,
        Long categoryId,
        long startingPrice,
        int durationHours,
        String status,
        Instant createdAt) {

    public static AuctionResponse from(Auction auction) {
        return new AuctionResponse(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getCategory().getId(),
                auction.getStartingPrice(),
                auction.getDurationHours(),
                auction.getStatus().name(),
                auction.getCreatedAt());
    }
}
