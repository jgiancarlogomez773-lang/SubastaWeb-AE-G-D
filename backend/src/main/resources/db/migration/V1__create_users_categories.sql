-- SubastaWeb v4 - T1-15 Migración users y categories (Sprint 1, D08)
-- Basado en Docs/07_Base_de_Datos/V1__schema_postgresql.sql (modelo oficial)
-- Reglas: Acta G1-01, RN-01, RF-AUT-01, RF-SUB-01
-- Flyway gestiona la transacción; ejecutar sobre BD vacía.
-- No se crean usuarios ADMIN ni contraseñas conocidas.

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
-- Correo único sin importar mayúsculas ni espacios (RN-01: duplicado -> 409)
CREATE UNIQUE INDEX ux_users_email_ci ON users (lower(btrim(email)));

CREATE TABLE categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    slug VARCHAR(60) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_categories_name CHECK (char_length(btrim(name)) BETWEEN 2 AND 60),
    CONSTRAINT ck_categories_slug CHECK (char_length(btrim(slug)) BETWEEN 2 AND 60)
);
-- Las llaves foráneas hacia users y categories (auctions.seller_id,
-- auctions.category_id) se crean en T1-16 (V2).
