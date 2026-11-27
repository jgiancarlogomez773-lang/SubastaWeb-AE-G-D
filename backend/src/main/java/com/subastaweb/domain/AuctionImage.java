package com.subastaweb.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

/** Tabla auction_images (V2 oficial). MVP: una imagen por solicitud, posición 1. */
@Entity
@Table(name = "auction_images")
public class AuctionImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    @Column(name = "storage_key", nullable = false, length = 240, unique = true)
    private String storageKey;

    @Column(name = "mime_type", nullable = false, length = 20)
    private String mimeType;

    @Column(name = "file_size_bytes", nullable = false)
    private int fileSizeBytes;

    // "position" va entre comillas porque es palabra clave de SQL.
    @Column(name = "`position`", nullable = false)
    private short position;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected AuctionImage() {
    }

    public AuctionImage(Auction auction, String storageKey, String mimeType, int fileSizeBytes) {
        this.auction = auction;
        this.storageKey = storageKey;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
        this.position = 1;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Auction getAuction() {
        return auction;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getFileSizeBytes() {
        return fileSizeBytes;
    }

    public int getPosition() {
        return position;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
