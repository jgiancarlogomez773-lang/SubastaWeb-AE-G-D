-- =====================================================================
-- T1-18 — Consultas de control del Sprint 1 (integridad de datos)
-- Ejecutar DESPUÉS de la prueba integrada (registro -> login -> nueva subasta).
-- Herramienta: pgAdmin 4 (Query Tool) o psql, sobre la BD del backend.
--
-- Cómo leerlas:
--   * Las consultas marcadas "DEBE DAR 0 FILAS" son controles: si devuelven
--     algo, hay un defecto que se debe reportar en la tarea.
--   * Las marcadas "INFORMATIVA" sirven como evidencia del recorrido.
-- Basado en Docs/07_Base_de_Datos/Consultas_De_Control_PostgreSQL.sql
-- =====================================================================

-- C1. INFORMATIVA: tablas del Sprint 1 presentes
-- Esperado: users, categories, auctions, auction_images (y flyway_schema_history si corrió Spring)
SELECT table_name
FROM information_schema.tables
WHERE table_schema = 'public'
ORDER BY table_name;

-- C2. DEBE DAR 0 FILAS: correos repetidos sin importar mayúsculas/espacios (RN-01)
SELECT lower(btrim(email)) AS correo, count(*) AS veces
FROM users
GROUP BY lower(btrim(email))
HAVING count(*) > 1;

-- C3. DEBE DAR 0 FILAS: contraseñas guardadas sin hash BCrypt
SELECT id, email
FROM users
WHERE password_hash NOT LIKE '$2_$%';

-- C4. INFORMATIVA: cuántos usuarios hay por rol
-- Revisar que los ADMIN sean solo los creados por procedimiento controlado (RN-01)
SELECT role, count(*) AS usuarios
FROM users
GROUP BY role
ORDER BY role;

-- C5. DEBE DAR 0 FILAS: subastas cuyo vendedor o categoría no existen
SELECT a.id, a.seller_id, a.category_id
FROM auctions a
LEFT JOIN users u ON u.id = a.seller_id
LEFT JOIN categories c ON c.id = a.category_id
WHERE u.id IS NULL OR c.id IS NULL;

-- C6. DEBE DAR 0 FILAS: en Sprint 1 no hay moderación, así que toda
-- solicitud debe seguir en PENDIENTE_REVISION (RN-02)
SELECT id, title, status
FROM auctions
WHERE status <> 'PENDIENTE_REVISION';

-- C7. DEBE DAR 0 FILAS: subastas con más de una imagen (RN-15, MVP = 1 imagen)
SELECT auction_id, count(*) AS imagenes
FROM auction_images
GROUP BY auction_id
HAVING count(*) > 1;

-- C8. DEBE DAR 0 FILAS: subastas sin imagen (la solicitud exige una imagen)
SELECT a.id, a.title
FROM auctions a
LEFT JOIN auction_images i ON i.auction_id = a.id
WHERE i.id IS NULL;

-- C9. DEBE DAR 0 FILAS: subastas en categorías inactivas
SELECT a.id, a.title, c.name AS categoria
FROM auctions a
JOIN categories c ON c.id = a.category_id
WHERE NOT c.is_active;

-- C10. INFORMATIVA: evidencia del recorrido (últimas subastas con su vendedor)
SELECT a.id, a.title, a.starting_price, a.duration_hours, a.status,
       u.email AS vendedor, c.name AS categoria, i.mime_type, i.file_size_bytes,
       a.created_at
FROM auctions a
JOIN users u ON u.id = a.seller_id
JOIN categories c ON c.id = a.category_id
LEFT JOIN auction_images i ON i.auction_id = a.id
ORDER BY a.created_at DESC
LIMIT 20;

-- C11. INFORMATIVA: migraciones aplicadas por Flyway (solo si el backend ya arrancó)
-- Si sale "no existe la relación flyway_schema_history", Spring todavía no ha corrido.
SELECT version, description, success, installed_on
FROM flyway_schema_history
ORDER BY installed_rank;
