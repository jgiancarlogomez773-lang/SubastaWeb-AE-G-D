package com.subastaweb.domain;

import java.util.Arrays;
import java.util.List;

/** Estados permitidos por ck_auctions_status (V2 oficial). */
public enum AuctionStatus {
    /** RN-02: toda solicitud nace pendiente y no es visible en el catálogo. */
    PENDIENTE_REVISION(false),
    RECHAZADA(false),
    ACTIVA(true),
    FINALIZADA(true);

    private final boolean publicVisible;

    AuctionStatus(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    public boolean isPublicVisible() {
        return publicVisible;
    }

    /** Estados que aparecen en el catálogo público (ACTIVA y FINALIZADA). */
    public static List<AuctionStatus> publicStatuses() {
        return Arrays.stream(values()).filter(AuctionStatus::isPublicVisible).toList();
    }
}
