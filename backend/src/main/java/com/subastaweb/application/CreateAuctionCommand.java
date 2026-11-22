package com.subastaweb.application;

public record CreateAuctionCommand(
        String title,
        String description,
        Long categoryId,
        long startingPrice,
        int durationHours,
        byte[] image) {
}
