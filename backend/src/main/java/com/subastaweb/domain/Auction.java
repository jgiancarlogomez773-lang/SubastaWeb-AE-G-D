package com.subastaweb.domain;

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
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Tabla auctions (V2 oficial). Las demás columnas (min_increment, review_round, content_version, fechas de
 * aprobación/cierre, updated_at...) las llena la base con sus valores por defecto o las usarán sprints posteriores.
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

    @Column(name = "starting_price", nullable = false, precision = 14, scale = 0)
    private BigDecimal startingPrice;

    @Column(name = "duration_hours", nullable = false)
    private short durationHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 22)
    private AuctionStatus status;

    @Column(name = "starts_at")
    private Instant startsAt;

    @Column(name = "ends_at")
    private Instant endsAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Auction() {
    }

    public Auction(User seller, Category category, String title, String description,
                   long startingPrice, int durationHours, AuctionStatus status) {
        this.seller = seller;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startingPrice = BigDecimal.valueOf(startingPrice);
        this.durationHours = (short) durationHours;
        this.status = status;
        this.createdAt = Instant.now();
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
        return startingPrice.longValueExact();
    }

    public int getDurationHours() {
        return durationHours;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public Instant getStartsAt() {
        return startsAt;
    }

    public Instant getEndsAt() {
        return endsAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
