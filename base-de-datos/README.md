# Base de datos — PostgreSQL

Responsables: D08, D09.

- `migraciones/`: borradores y revisión de scripts SQL (las migraciones ejecutables viven en `backend/src/main/resources/db/migration/`).
- `semillas/`: datos de prueba (categorías, usuarios demo, fixtures de pujas). Sin secretos.
- `scripts/`: respaldo y restauración de la demo.

Siete tablas: `users`, `categories`, `auctions`, `auction_images`, `bids`, `moderation_reviews`, `auction_status_events`.
Modelo en `../Docs/08_Diagramas/01_Modelo_Entidad_Relacion.png`.
