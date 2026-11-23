
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

/** Imagen de una subasta (tabla auction_images). MVP: una imagen por subasta, posición 1 (valor por defecto en BD). */
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

    protected AuctionImage() {
    }

    public AuctionImage(String storageKey, String mimeType, int fileSizeBytes) {
        this.storageKey = storageKey;
        this.mimeType = mimeType;
        this.fileSizeBytes = fileSizeBytes;
    }

    void attachTo(Auction auction) {
        this.auction = auction;
    }

    public Long getId() {
        return id;
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
}
