-- SubastaWeb v4 - T1-16 Migración auctions + estados iniciales (Sprint 1, D08)
-- Basado en Docs/07_Base_de_Datos/V1__schema_postgresql.sql (modelo oficial)
-- Reglas: Acta G1-01, RN-02, RN-06, RN-07, RN-15, RF-SUB-01
-- Requiere V1 (users, categories). Flyway gestiona la transacción.

CREATE TABLE auctions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    seller_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    category_id BIGINT NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    title VARCHAR(120) NOT NULL,
    description TEXT NOT NULL,
    starting_price NUMERIC(14,0) NOT NULL,
    min_increment NUMERIC(14,0) NOT NULL DEFAULT 1000,
    duration_hours SMALLINT NOT NULL,
    status VARCHAR(22) NOT NULL DEFAULT 'PENDIENTE_REVISION',
    review_round INTEGER NOT NULL DEFAULT 1,
    content_version INTEGER NOT NULL DEFAULT 1,
    rejected_content_version INTEGER NOT NULL DEFAULT 0,
    approved_at TIMESTAMPTZ,
    starts_at TIMESTAMPTZ,
    ends_at TIMESTAMPTZ,
    closed_at TIMESTAMPTZ,
    winning_bid_id BIGINT, -- la FK hacia bids se agrega en T3-15
    created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT ck_auctions_title CHECK (char_length(btrim(title)) BETWEEN 5 AND 120),
    CONSTRAINT ck_auctions_description CHECK (char_length(btrim(description)) BETWEEN 20 AND 2000),
    CONSTRAINT ck_auctions_price CHECK (starting_price >= 1000),
    CONSTRAINT ck_auctions_increment CHECK (min_increment >= 1000),
    CONSTRAINT ck_auctions_duration CHECK (duration_hours IN (24,48,72)),
    CONSTRAINT ck_auctions_status CHECK (status IN ('PENDIENTE_REVISION','RECHAZADA','ACTIVA','FINALIZADA')),
    CONSTRAINT ck_auctions_round CHECK (review_round >= 1),
    CONSTRAINT ck_auctions_version CHECK (content_version >= 1),
    CONSTRAINT ck_auctions_rejected_version CHECK (rejected_content_version BETWEEN 0 AND content_version),
    CONSTRAINT ck_auctions_approval_dates CHECK (
        status NOT IN ('ACTIVA','FINALIZADA') OR
        (approved_at IS NOT NULL AND starts_at IS NOT NULL AND ends_at IS NOT NULL)
    ),
    CONSTRAINT ck_auctions_dates_order CHECK (ends_at IS NULL OR (starts_at IS NOT NULL AND ends_at > starts_at)),
    CONSTRAINT ck_auctions_closed CHECK (status <> 'FINALIZADA' OR closed_at IS NOT NULL),
    CONSTRAINT ck_auctions_winner_closed CHECK (winning_bid_id IS NULL OR status = 'FINALIZADA')
);

-- Imagen de la solicitud (necesaria para T1-12: guardar solicitud con imagen)
CREATE TABLE auction_images (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    storage_key VARCHAR(240) NOT NULL UNIQUE,
    mime_type VARCHAR(20) NOT NULL,
    file_size_bytes INTEGER NOT NULL,
    position SMALLINT NOT NULL DEFAULT 1,
    created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT uq_auction_images_position UNIQUE (auction_id,position),
    CONSTRAINT ck_auction_images_position CHECK (position BETWEEN 1 AND 3),
    CONSTRAINT ck_auction_images_type CHECK (mime_type IN ('image/jpeg','image/png','image/webp')),
    CONSTRAINT ck_auction_images_size CHECK (file_size_bytes BETWEEN 1 AND 3145728)
);
-- MVP: máximo 1 imagen por subasta, posición = 1.

CREATE INDEX ix_auctions_seller_recent ON auctions (seller_id,created_at DESC);
CREATE INDEX ix_auctions_pending ON auctions (status,created_at) WHERE status='PENDIENTE_REVISION';
CREATE INDEX ix_auctions_close_due ON auctions (ends_at) WHERE status='ACTIVA';
CREATE INDEX ix_auctions_public ON auctions (created_at DESC,id DESC) WHERE status IN ('ACTIVA','FINALIZADA');
CREATE INDEX ix_auctions_category ON auctions (category_id,status);
