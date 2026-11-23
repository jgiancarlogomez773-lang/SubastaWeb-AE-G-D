package com.subastaweb.persistence;

import com.subastaweb.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByActiveTrueOrderByNameAsc();

    Optional<Category> findByIdAndActiveTrue(Long id);
}
