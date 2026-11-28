package com.subastaweb.api.dto;

public record UpdateAuctionRequest(
        String title,
        String description,
        Long categoryId,
        Long startingPrice,
        Integer durationHours) {
}
