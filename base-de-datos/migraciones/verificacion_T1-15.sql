-- =====================================================================
-- Verificación T1-15 — Migración users y categories (D08)
-- Ejecutar en una BD vacía DESPUÉS de V1__create_users_categories.sql
-- Herramienta usada: pgAdmin 4 (Query Tool) sobre PostgreSQL 16
-- Ejecutar cada prueba por separado y comparar con el resultado esperado.
-- =====================================================================

-- PRUEBA 1: existen las tablas
-- Esperado: 2 filas -> users, categories
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' AND table_name IN ('users','categories');

-- PRUEBA 2: usuario válido, rol por defecto
-- Esperado: 1 fila con role = USER
INSERT INTO users (full_name, email, password_hash)
VALUES ('Prueba Uno', 'prueba@example.test', 'hash_de_prueba')
RETURNING id, full_name, email, role;

-- PRUEBA 3: correo duplicado con otras mayúsculas (requiere la PRUEBA 2)
-- Esperado: ERROR llave duplicada viola restricción de unicidad «ux_users_email_ci»
INSERT INTO users (full_name, email, password_hash)
VALUES ('Prueba Dos', 'PRUEBA@example.test', 'hash_de_prueba');

-- PRUEBA 4: rol inválido
-- Esperado: ERROR viola la restricción «check» «ck_users_role»
INSERT INTO users (full_name, email, password_hash, role)
VALUES ('Prueba Mal', 'mal@example.test', 'hash_de_prueba', 'SUPER');

-- LIMPIEZA: borrar los datos de prueba
DELETE FROM users WHERE email = 'prueba@example.test';
