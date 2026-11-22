package com.subastaweb.domain;

public enum AuctionStatus {
    /** RN-02: toda solicitud nace pendiente y no es visible en el catálogo. */
    PENDIENTE_REVISION(false);

    private final boolean publicVisible;

    AuctionStatus(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    public boolean isPublicVisible() {
        return publicVisible;
    }
}
