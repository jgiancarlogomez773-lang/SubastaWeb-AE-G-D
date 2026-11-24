-- =====================================================================
-- T1-17 — Usuarios de prueba reproducibles: 1 USER y 1 ADMIN
-- SOLO PARA ENTORNO LOCAL / PRUEBAS. No ejecutar en producción.
--
-- RN-01: el ADMIN nunca se crea por la API pública; solo por este
-- procedimiento controlado (script ejecutado por BD).
--
-- Cómo usarlo:
--   1. Cambia las dos contraseñas de abajo (no las subas a Git).
--   2. Ejecútalo en la BD después de V1, V2 y V3.
--   3. Se puede ejecutar varias veces: si el usuario ya existe,
--      actualiza su contraseña y su rol.
--
-- Las contraseñas se guardan como hash BCrypt (pgcrypto), el mismo
-- formato que usa Spring Security (BCryptPasswordEncoder).
-- =====================================================================
CREATE EXTENSION IF NOT EXISTS pgcrypto;

DO $$
DECLARE
    clave_user  TEXT := 'CAMBIAR_CLAVE_USER';
    clave_admin TEXT := 'CAMBIAR_CLAVE_ADMIN';
BEGIN
    IF clave_user LIKE 'CAMBIAR%' OR clave_admin LIKE 'CAMBIAR%' THEN
        RAISE EXCEPTION 'Cambia las contraseñas de prueba antes de ejecutar el script';
    END IF;

    INSERT INTO users (full_name, email, password_hash, role)
    VALUES ('Usuario Demo', 'usuario.demo@example.test', crypt(clave_user, gen_salt('bf', 10)), 'USER')
    ON CONFLICT ((lower(btrim(email))))
    DO UPDATE SET password_hash = EXCLUDED.password_hash, role = EXCLUDED.role, updated_at = clock_timestamp();

    INSERT INTO users (full_name, email, password_hash, role)
    VALUES ('Administrador Demo', 'admin.demo@example.test', crypt(clave_admin, gen_salt('bf', 10)), 'ADMIN')
    ON CONFLICT ((lower(btrim(email))))
    DO UPDATE SET password_hash = EXCLUDED.password_hash, role = EXCLUDED.role, updated_at = clock_timestamp();
END $$;

-- Resultado esperado: 2 filas (USER y ADMIN), con password_hash que empieza por $2a$
SELECT id, full_name, email, role, left(password_hash, 7) AS inicio_hash
FROM users
WHERE email IN ('usuario.demo@example.test', 'admin.demo@example.test')
ORDER BY role;
