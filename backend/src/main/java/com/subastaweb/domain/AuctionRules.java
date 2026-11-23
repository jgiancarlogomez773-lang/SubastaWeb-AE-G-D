package com.subastaweb.domain;

/** Reglas de negocio de la solicitud de subasta (Sprint 1). */
public final class AuctionRules {

    /** RN-07: precio inicial desde COP 1.000, entero positivo. */
    public static final long MIN_STARTING_PRICE = 1_000L;
    public static final long MAX_STARTING_PRICE = 1_000_000_000L;

    /** RN-06: la duración solo puede ser 24, 48 o 72 horas. */
    public static final java.util.Set<Integer> ALLOWED_DURATIONS_HOURS = java.util.Set.of(24, 48, 72);

    /** RN-15: una sola imagen JPG/PNG/WebP de hasta 3 MB. */
    public static final int MAX_IMAGE_BYTES = 3 * 1024 * 1024;

    private AuctionRules() {
    }
}
