-- =====================================================================
-- Rollback T1-15 — deshace V1__create_users_categories.sql
-- Solo para entorno local/pruebas. Si ya existe V2 (auctions), revertir V2 primero.
-- =====================================================================
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Si la migración la aplicó Flyway (desde Spring), borrar también su registro:
-- DELETE FROM flyway_schema_history WHERE version = '1';
