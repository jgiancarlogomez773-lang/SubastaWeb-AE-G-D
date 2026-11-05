# Modelo de datos PostgreSQL - MER y diccionario

**Proyecto:** SubastaWeb · **ID:** BD-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

## 1. Principios de diseño

**Versión mejorada:** siete tablas normalizadas, valores monetarios exactos en COP como `NUMERIC(14,0)`, llaves compuestas para impedir ganadores de otras subastas, historial por ronda de moderación, eventos de cambio de estado y metadatos de imágenes independientes del artículo. Las imágenes binarias se guardan en almacenamiento configurado y la BD registra únicamente claves opacas. Se evita guardar un `precio_actual` redundante: la mejor oferta se calcula desde `bids` bajo bloqueo de la subasta.

**Fuente editable del diagrama:** `08_Diagramas/01_Modelo_Entidad_Relacion.mmd`. **Imagen:** `08_Diagramas/01_Modelo_Entidad_Relacion.png`.

![Modelo entidad relación](08_Diagramas/01_Modelo_Entidad_Relacion.png)

## 2. Entidades y relaciones

| Tabla | Propósito | Relaciones principales | Clave o control crítico |
| --- | --- | --- | --- |
| users | Autenticados y ADMIN | 1 usuario publica N auctions y oferta N bids | email case-insensitive único; rol USER/ADMIN |
| categories | Clasifica publicaciones | 1 categoría para N auctions | slug único |
| auctions | Estado, precio inicial, duración, ganador | N:1 users y categories; 1:N bids | CHECK fechas, estado, winner FK compuesta |
| auction_images | Metadatos de imágenes | N:1 auctions | storage_key y posición únicos; MIME/tamaño |
| bids | Ofertas aceptadas inmutables | N:1 auctions; N:1 users | monto exacto, índices ranking |
| moderation_reviews | Decisiones ADMIN por ronda | N:1 auctions; N:1 users | UNIQUE(auction_id,review_round) |
| auction_status_events | Auditoría de transiciones | N:1 auctions y opcional actor users | evento y estados válidos |

## 3. Diccionario de datos físico

### users

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| full_name | VARCHAR(100) | - | NO | 2..100 caracteres |
| email | VARCHAR(254) | UNIQUE case insensitive | NO | correo normalizado y validado |
| password_hash | TEXT | - | NO | solo hash |
| role | VARCHAR(10) | CHECK | NO | USER/ADMIN |
| created_at, updated_at | TIMESTAMPTZ | - | NO | marcas de auditoría |

### categories

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| name | VARCHAR(60) | - | NO | nombre mostrado |
| slug | VARCHAR(60) | UNIQUE | NO | clave legible |
| is_active | BOOLEAN | - | NO | bandera vigente |

### auctions

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| seller_id | BIGINT | FK users | NO | propietario |
| category_id | BIGINT | FK categories | NO | categoría |
| title | VARCHAR(120) | CHECK | NO | 5..120 |
| description | TEXT | CHECK | NO | 20..2000 |
| starting_price | NUMERIC(14,0) | CHECK | NO | COP >= 1000 |
| min_increment | NUMERIC(14,0) | CHECK | NO | COP >= 1000 |
| duration_hours | SMALLINT | CHECK | NO | 24/48/72 |
| status | VARCHAR(22) | CHECK | NO | 4 estados válidos |
| review_round | INTEGER | CHECK | NO | >=1 |
| content_version | INTEGER | CHECK | NO | control cambios |
| rejected_content_version | INTEGER | CHECK | NO | impide reenvío igual |
| approved_at,starts_at,ends_at,closed_at | TIMESTAMPTZ | CHECK | SÍ | fechas de ciclo de vida |
| winning_bid_id | BIGINT | FK (id,winning_bid_id) | SÍ | mismo auction que bid |
| created_at,updated_at | TIMESTAMPTZ | - | NO | tiempos servidor |

### auction_images

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| auction_id | BIGINT | FK auctions | NO | subasta |
| storage_key | VARCHAR(240) | UNIQUE | NO | ruta opaca sin datos sensibles |
| mime_type | VARCHAR(20) | CHECK | NO | JPEG PNG WebP |
| file_size_bytes | INTEGER | CHECK | NO | 1..3145728 |
| position | SMALLINT | UNIQUE por subasta | NO | en MVP solo 1 |
| created_at | TIMESTAMPTZ | - | NO | fecha |

### bids

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| auction_id | BIGINT | FK auctions, UNIQUE compuesta | NO | subasta |
| bidder_id | BIGINT | FK users | NO | comprador |
| amount | NUMERIC(14,0) | CHECK | NO | COP >= 1000 |
| placed_at | TIMESTAMPTZ | - | NO | hora del servidor |

### moderation_reviews

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| auction_id | BIGINT | FK auctions / UNIQUE ronda | NO | subasta |
| admin_id | BIGINT | FK users | NO | moderador |
| review_round | INTEGER | UNIQUE compuesta | NO | ronda |
| reviewed_version | INTEGER | CHECK | NO | versión de contenido |
| decision | VARCHAR(10) | CHECK | NO | APROBAR/RECHAZAR |
| reason | TEXT | CHECK | SÍ | 10..500 solo para rechazo |
| reviewed_at | TIMESTAMPTZ | - | NO | hora |

### auction_status_events

| Columna | Tipo | Restricción | Nulo | Uso |
| --- | --- | --- | --- | --- |
| id | BIGINT | PK | NO | Identity |
| auction_id | BIGINT | FK auctions | NO | subasta |
| actor_user_id | BIGINT | FK users | SÍ | NULL si operación de sistema |
| event_type | VARCHAR(20) | CHECK | NO | enviada/rechazada/... |
| from_status | VARCHAR(22) | CHECK | SÍ | estado anterior |
| to_status | VARCHAR(22) | CHECK | NO | estado posterior |
| occurred_at | TIMESTAMPTZ | - | NO | hora |
| notes | VARCHAR(500) | - | SÍ | resumen sin secretos |

## 4. Reglas que debe garantizar el servicio y NO solo SQL

- Las tablas no pueden expresar con un CHECK simple que `bids.bidder_id != auctions.seller_id`: lo comprueba Spring Boot bajo transacción.
- Solo ADMIN puede aprobar/rechazar. La FK `admin_id` valida existencia, no rol; Spring Security valida rol.
- La revisión bloquea la fila `auctions`, verifica estado, ronda y versión, guarda `moderation_reviews` y actualiza `auctions` y `auction_status_events` en una única transacción.
- Para ofertar: `SELECT auctions ... FOR UPDATE`; verificar hora `clock_timestamp()` y condición ACTIVA, propietario, monto contra `MAX(bids.amount)`; insertar oferta; commit. Rechazo concurrente -> 409.
- Para cerrar: worker transaccional con bloqueo de subasta, comprobar vencimiento y elegir mayor importe; registrar evento y cambiar estado una sola vez; repetir no altera resultado.
- Los archivos de imagen se almacenan con nombres opacos, validando MIME, extensión, tamaño y contenido. Si falla la transacción, eliminar temporal huérfano.
- Se documentará política de borrado/retención cuando exista aprobación institucional; no afirmar una política inventada.

## 5. Scripts y orden de migración

1. `V1__schema_postgresql.sql`: crea 7 tablas, restricciones e índices.
2. `V2__categorias.sql`: precarga categorías sin usuarios ADMIN.
3. `Consultas_De_Control_PostgreSQL.sql`: verifica datos de prueba sin modificarlos.

**Instalación sugerida:** gestionar V1/V2 con Flyway dentro de Spring Boot; no combinar Hibernate `ddl-auto=create` con migraciones. Usar `ddl-auto=validate` al desarrollar con esquemas aplicados.

## 6. Consultas y rendimiento

La API pública usa `ix_auctions_public`. La cola de revisión usa `ix_auctions_pending`. El worker selecciona vencidas con `ix_auctions_close_due`; ranking de pujas con `ix_bids_rank`. El plan de acceso real requiere pruebas EXPLAIN en datos de laboratorio; la existencia del índice no garantiza su uso. El máximo de una imagen por artículo en MVP se valida en servicio (la tabla admite posiciones adicionales para evolución).
