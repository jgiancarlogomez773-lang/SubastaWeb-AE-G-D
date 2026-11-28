package com.subastaweb.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Subasta. Solo se mapean las columnas que usa el Sprint 1; el resto de columnas de la tabla
 * (min_increment, review_round, fechas de aprobaciÃ³n, etc.) tienen valor por defecto en la base de datos.
 */
@Entity
@Table(name = "auctions")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    /** La base usa NUMERIC(14,0). */
    @JdbcTypeCode(SqlTypes.NUMERIC)
    @Column(name = "starting_price", nullable = false, precision = 14, scale = 0)
    private long startingPrice;

    /** La base usa SMALLINT. */
    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "duration_hours", nullable = false)
    private int durationHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 22)
    private AuctionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuctionImage> images = new ArrayList<>();

    protected Auction() {
    }

    public Auction(User seller, Category category, String title, String description,
                   long startingPrice, int durationHours, AuctionStatus status) {
        this.seller = seller;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startingPrice = startingPrice;
        this.durationHours = durationHours;
        this.status = status;
        this.createdAt = Instant.now();
    }

    /**
     * Constructor de compatibilidad: crea la subasta y su imagen a partir del nombre del archivo.
     * Lo conserva para cÃ³digo y pruebas que ya lo usaban (el tamaÃ±o real se registra con addImage).
     */
    public Auction(User seller, Category category, String title, String description,
                   long startingPrice, int durationHours, String imagePath, AuctionStatus status) {
        this(seller, category, title, description, startingPrice, durationHours, status);
        addImage(new AuctionImage(imagePath, guessMimeType(imagePath), 1));
    }

    public void addImage(AuctionImage image) {
        image.attachTo(this);
        images.add(image);
    }

    private static String guessMimeType(String name) {
        String lower = name == null ? "" : name.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        return "image/png";
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getStartingPrice() {
        return startingPrice;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<AuctionImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    /** Nombre del archivo de la primera imagen (null si no tiene). */
    public String getImagePath() {
        return images.isEmpty() ? null : images.get(0).getStorageKey();
    }

    @Column(name = "review_round", nullable = false)
    private int reviewRound = 1;

    @Column(name = "content_version", nullable = false)
    private int contentVersion = 1;

    @Column(name = "rejected_content_version", nullable = false)
    private int rejectedContentVersion = 0;

    public int getReviewRound() {
        return reviewRound;
    }

    public int getContentVersion() {
        return contentVersion;
    }

    public int getRejectedContentVersion() {
        return rejectedContentVersion;
    }

    public void applyRejectedEdit(String title, String description, Category category,
                                  long startingPrice, int durationHours) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startingPrice = startingPrice;
        this.durationHours = durationHours;
        this.contentVersion += 1;
    }

    public void markResubmitted() {
        this.status = AuctionStatus.PENDIENTE_REVISION;
        this.reviewRound += 1;
    }
}
