package com.subastaweb.application;

public record UpdateAuctionCommand(
        String title,
        String description,
        Long categoryId,
        Long startingPrice,
        Integer durationHours) {
}
