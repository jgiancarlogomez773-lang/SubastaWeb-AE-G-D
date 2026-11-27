package com.subastaweb.api;

import com.subastaweb.domain.Category;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** T1-13: GET /api/v1/categories (cada prueba se revierte: @Transactional). */
@Transactional
class CategoryIntegrationTest extends IntegrationTestBase {

    @Test
    void listsActiveCategoriesWithoutSession() throws Exception {
        mvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isNotEmpty())
                .andExpect(jsonPath("$[0].slug").isNotEmpty())
                .andExpect(jsonPath("$[0].active").doesNotExist());
    }

    @Test
    void doesNotListInactiveCategories() throws Exception {
        String inactiveName = "Inactiva-" + UUID.randomUUID();
        categories.save(new Category(inactiveName, "inactiva-" + UUID.randomUUID(), false));

        mvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == '" + inactiveName + "')]").isEmpty());
    }

    @Test
    void returnsEmptyListWhenThereAreNoCategories() throws Exception {
        categories.deleteAllInBatch();

        mvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}
