package com.subastaweb.application;

import com.subastaweb.domain.Category;
import com.subastaweb.persistence.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    private final CategoryRepository repository = mock(CategoryRepository.class);
    private final CategoryService service = new CategoryService(repository);

    @Test
    void returnsActiveCategories() {
        when(repository.findByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(new Category("Arte", "arte", true), new Category("Hogar", "hogar", true)));

        assertThat(service.listActive()).extracting(Category::getName).containsExactly("Arte", "Hogar");
    }

    @Test
    void returnsEmptyListWhenThereAreNone() {
        when(repository.findByActiveTrueOrderByNameAsc()).thenReturn(List.of());

        assertThat(service.listActive()).isEmpty();
    }
}
