-- =====================================================================
-- Verificación T1-16 — Migración auctions + estados iniciales (D08)
-- Ejecutar en una BD con V1 y V2 aplicadas (users, categories, auctions, auction_images)
-- Herramienta usada: pgAdmin 4 (Query Tool) sobre PostgreSQL 16
-- Ejecutar cada prueba por separado y comparar con el resultado esperado.
-- =====================================================================

-- PRUEBA 1: llaves foráneas
-- Esperado: 3 filas -> seller_id->users, category_id->categories, auction_id->auctions
SELECT conrelid::regclass AS tabla, conname AS llave, pg_get_constraintdef(oid) AS conecta_con
FROM pg_constraint
WHERE contype = 'f' AND conrelid::regclass::text IN ('auctions','auction_images');

-- PRUEBA 2: subasta válida, estado inicial
-- Esperado: 1 fila con status = PENDIENTE_REVISION
INSERT INTO categories (name, slug) VALUES ('Electrónica', 'electronica');
INSERT INTO users (full_name, email, password_hash)
VALUES ('Vendedor Prueba', 'vendedor@example.test', 'hash_de_prueba');
INSERT INTO auctions (seller_id, category_id, title, description, starting_price, duration_hours)
VALUES ((SELECT id FROM users WHERE email = 'vendedor@example.test'),
        (SELECT id FROM categories WHERE slug = 'electronica'),
        'Bicicleta usada', 'Bicicleta de montaña en buen estado general', 50000, 48)
RETURNING id, title, starting_price, duration_hours, status;

-- PRUEBA 3: precio menor a COP 1.000
-- Esperado: ERROR viola la restricción «check» «ck_auctions_price»
INSERT INTO auctions (seller_id, category_id, title, description, starting_price, duration_hours)
VALUES ((SELECT id FROM users WHERE email = 'vendedor@example.test'),
        (SELECT id FROM categories WHERE slug = 'electronica'),
        'Precio bajo', 'Descripcion suficientemente larga', 500, 24);

-- PRUEBA 4: duración distinta de 24/48/72
-- Esperado: ERROR viola la restricción «check» «ck_auctions_duration»
INSERT INTO auctions (seller_id, category_id, title, description, starting_price, duration_hours)
VALUES ((SELECT id FROM users WHERE email = 'vendedor@example.test'),
        (SELECT id FROM categories WHERE slug = 'electronica'),
        'Duracion mala', 'Descripcion suficientemente larga', 5000, 36);

-- PRUEBA 5: vendedor inexistente
-- Esperado: ERROR viola la llave foránea «auctions_seller_id_fkey»
INSERT INTO auctions (seller_id, category_id, title, description, starting_price, duration_hours)
VALUES (999999, (SELECT id FROM categories WHERE slug = 'electronica'),
        'Sin vendedor', 'Descripcion suficientemente larga', 5000, 24);

-- PRUEBA 6: activar sin fechas de aprobación/inicio/fin
-- Esperado: ERROR viola la restricción «check» «ck_auctions_approval_dates»
UPDATE auctions SET status = 'ACTIVA' WHERE title = 'Bicicleta usada';

-- LIMPIEZA: borrar los datos de prueba
DELETE FROM auctions WHERE title = 'Bicicleta usada';
DELETE FROM users WHERE email = 'vendedor@example.test';
DELETE FROM categories WHERE slug = 'electronica';
