package com.subastaweb.domain;

import java.util.Set;

/** Reglas de negocio de la solicitud de subasta (Sprint 1). */
public final class AuctionRules {

    /** RN-07: precio inicial desde COP 1.000, entero positivo. */
    public static final long MIN_STARTING_PRICE = 1_000L;
    /** Supuesto a confirmar con el PO (el contrato no define un máximo). */
    public static final long MAX_STARTING_PRICE = 1_000_000_000L;

    /** RN-06: la duración solo puede ser 24, 48 o 72 horas. */
    public static final Set<Integer> ALLOWED_DURATION_HOURS = Set.of(24, 48, 72);

    /** RN-15: una sola imagen JPG/PNG/WebP de hasta 3 MB. */
    public static final int MAX_IMAGE_BYTES = 3 * 1024 * 1024;

    public static final int TITLE_MIN = 5;
    public static final int TITLE_MAX = 120;
    public static final int DESCRIPTION_MIN = 20;
    public static final int DESCRIPTION_MAX = 2000;

    private AuctionRules() {
    }

    public static boolean isAllowedDuration(int hours) {
        return ALLOWED_DURATION_HOURS.contains(hours);
    }
}
