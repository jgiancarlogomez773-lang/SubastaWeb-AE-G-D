-- SubastaWeb v4 - PostgreSQL 16+ compatible (propuesta, verificar en instancia real)
-- V1__schema.sql. Flyway gestiona la transaccion; ejecutar sobre BD vacia.
-- No se crean usuarios ADMIN ni contrasenas conocidas.

CREATE TABLE users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT ck_users_name CHECK (char_length(btrim(full_name)) BETWEEN 2 AND 100),
    CONSTRAINT ck_users_email CHECK (position('@' in email) > 1 AND char_length(btrim(email)) > 4),
    CONSTRAINT ck_users_role CHECK (role IN ('USER','ADMIN'))
);
CREATE UNIQUE INDEX ux_users_email_ci ON users (lower(btrim(email)));

CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    slug VARCHAR(60) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_categories_name CHECK (char_length(btrim(name)) BETWEEN 2 AND 60),
    CONSTRAINT ck_categories_slug CHECK (char_length(btrim(slug)) BETWEEN 2 AND 60)
);

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
    winning_bid_id BIGINT,
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
-- MVP: maximo 1 imagen por subasta, posicion = 1; el servicio no expone posiciones 2 y 3.

CREATE TABLE bids (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auctions(id) ON DELETE RESTRICT,
    bidder_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    amount NUMERIC(14,0) NOT NULL,
    placed_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT ck_bids_positive CHECK (amount >= 1000),
    CONSTRAINT uq_bids_auction_id_id UNIQUE (auction_id,id)
);

-- La pareja (id,winning_bid_id) garantiza que la oferta ganadora pertenece a esa subasta.
ALTER TABLE auctions ADD CONSTRAINT fk_auction_winning_bid_same_auction
    FOREIGN KEY (id, winning_bid_id) REFERENCES bids(auction_id,id) ON DELETE RESTRICT;

CREATE TABLE moderation_reviews (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auctions(id) ON DELETE RESTRICT,
    admin_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    review_round INTEGER NOT NULL,
    reviewed_version INTEGER NOT NULL,
    decision VARCHAR(10) NOT NULL,
    reason TEXT,
    reviewed_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    CONSTRAINT uq_reviews_one_decision_per_round UNIQUE (auction_id,review_round),
    CONSTRAINT ck_reviews_round CHECK (review_round >= 1 AND reviewed_version >= 1),
    CONSTRAINT ck_reviews_decision CHECK (decision IN ('APROBAR','RECHAZAR')),
    CONSTRAINT ck_reviews_reason CHECK (
        (decision='APROBAR' AND reason IS NULL) OR
        (decision='RECHAZAR' AND reason IS NOT NULL AND char_length(btrim(reason)) BETWEEN 10 AND 500)
    )
);

CREATE TABLE auction_status_events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES auctions(id) ON DELETE RESTRICT,
    actor_user_id BIGINT REFERENCES users(id) ON DELETE RESTRICT,
    event_type VARCHAR(20) NOT NULL,
    from_status VARCHAR(22),
    to_status VARCHAR(22) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT clock_timestamp(),
    notes VARCHAR(500),
    CONSTRAINT ck_events_type CHECK (event_type IN ('ENVIADA','RECHAZADA','REENVIADA','APROBADA','FINALIZADA')),
    CONSTRAINT ck_events_to CHECK (to_status IN ('PENDIENTE_REVISION','RECHAZADA','ACTIVA','FINALIZADA')),
    CONSTRAINT ck_events_from CHECK (from_status IS NULL OR from_status IN ('PENDIENTE_REVISION','RECHAZADA','ACTIVA','FINALIZADA'))
);

CREATE INDEX ix_auctions_seller_recent ON auctions (seller_id,created_at DESC);
CREATE INDEX ix_auctions_pending ON auctions (status,created_at) WHERE status='PENDIENTE_REVISION';
CREATE INDEX ix_auctions_close_due ON auctions (ends_at) WHERE status='ACTIVA';
CREATE INDEX ix_auctions_public ON auctions (created_at DESC,id DESC) WHERE status IN ('ACTIVA','FINALIZADA');
CREATE INDEX ix_auctions_category ON auctions (category_id,status);
CREATE INDEX ix_bids_rank ON bids (auction_id,amount DESC,placed_at ASC,id ASC);
CREATE INDEX ix_bids_by_user ON bids (bidder_id,placed_at DESC);
CREATE INDEX ix_reviews_auction ON moderation_reviews (auction_id,reviewed_at DESC);
CREATE INDEX ix_events_auction ON auction_status_events (auction_id,occurred_at DESC);

-- Reglas entre varias tablas son responsabilidad de una transaccion backend:
--   impedir autopuja, verificar rol de ADMIN, evitar pujas tras ends_at,
--   validar minimo contra mejor oferta, exigir cambios para reenvio,
--   e insertar evento + cambio de estado en la misma transaccion.
