package com.subastaweb.api;

import com.subastaweb.api.dto.CategoryResponse;
import com.subastaweb.application.CategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** T1-13: GET /api/v1/categories (público). Devuelve [] si no hay categorías activas. */
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.listActive().stream().map(CategoryResponse::from).toList();
    }
}
