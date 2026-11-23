-- =====================================================================
-- Rollback T1-16 — deshace V2__create_auctions.sql
-- Solo para entorno local/pruebas. Revertir antes que V1 (T1-15).
-- =====================================================================
DROP TABLE IF EXISTS auction_images;
DROP TABLE IF EXISTS auctions;

-- Si la migración la aplicó Flyway (desde Spring), borrar también su registro:
-- DELETE FROM flyway_schema_history WHERE version = '2';
