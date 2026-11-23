package com.subastaweb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.text.Normalizer;
import java.util.Locale;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 60, unique = true)
    private String slug;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    protected Category() {
    }

    /** Genera el slug a partir del nombre (ej. "Electrónica" -> "electronica"). */
    public Category(String name, boolean active) {
        this(name, slugify(name), active);
    }

    public Category(String name, String slug, boolean active) {
        this.name = name;
        this.slug = slug;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isActive() {
        return active;
    }

    static String slugify(String text) {
        String base = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");
        return base.isEmpty() ? "categoria" : base;
    }
}
