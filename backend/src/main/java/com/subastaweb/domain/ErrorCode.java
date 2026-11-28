package com.subastaweb.domain;

/** CÃ³digos de error estables de la API (ver contratos/sprint-1.md, secciÃ³n 1). */
public enum ErrorCode {
    AUTH_REQUIRED(401),
    INVALID_CREDENTIALS(401),
    FORBIDDEN(403),
    AUCTION_NOT_PUBLIC(404),
    EMAIL_ALREADY_REGISTERED(409),
    AUCTION_CONFLICT(409),
    VALIDATION_ERROR(422),
    INVALID_IMAGE(422),
    INTERNAL_ERROR(500);

    private final int status;

    ErrorCode(int status) {
        this.status = status;
    }

    public int status() {
        return status;
    }
}

