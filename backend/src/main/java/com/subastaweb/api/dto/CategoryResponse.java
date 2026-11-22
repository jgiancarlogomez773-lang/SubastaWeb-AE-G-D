package com.subastaweb.api.dto;

import com.subastaweb.domain.Category;

public record CategoryResponse(Long id, String name) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
