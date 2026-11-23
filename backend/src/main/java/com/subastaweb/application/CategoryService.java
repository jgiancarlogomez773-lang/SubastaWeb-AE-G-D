package com.subastaweb.application;

import com.subastaweb.domain.Category;
import com.subastaweb.persistence.CategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** T1-13: categorías activas disponibles para crear una solicitud. */
@Service
public class CategoryService {

    private final CategoryRepository categories;

    public CategoryService(CategoryRepository categories) {
        this.categories = categories;
    }

    @Transactional(readOnly = true)
    public List<Category> listActive() {
        return categories.findByActiveTrueOrderByNameAsc();
    }
}
