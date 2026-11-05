# TRD - Diseño técnico, API REST, seguridad y base de datos

**Proyecto:** SubastaWeb · **ID:** TRD-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

## 1. Decisiones arquitectónicas y alcance técnico

**Arquitectura:** monolito modular; SPA Vue 3 / Vite y Vue Router, API REST Java 21 LTS + Spring Boot compatible, Spring Web, Spring Security, Validation, Spring Data JPA, Flyway y PostgreSQL. Almacenamiento de imagen local en volumen persistente del ambiente de demo; sin servicios externos obligatorios. Las versiones exactas de Spring Boot y PostgreSQL se fijarán en Sprint 1 tras comprobar el entorno disponible.

**Alternativas descartadas:** microservicios, Kafka, Kubernetes, Redis y websockets no son necesarios para 4 semanas. El precio se refresca por solicitud HTTP periódica de 5-10 segundos; la verdad está en PostgreSQL y el servidor. El frontend no realiza validaciones de negocio definitivas.

![Arquitectura de componentes](08_Diagramas/02_Arquitectura_Componentes.png)

## 2. Componentes y responsabilidades

| Componente | Tecnología | Responsabilidad | Dueños principales |
| --- | --- | --- | --- |
| SPA | Vue 3, Vite, Vue Router | Catálogo, solicitud, ADMIN, puja, resultado | D02-D04; D01 UX |
| API REST | Java/Spring Boot | DTO, reglas, contratos, autenticación | D05-D07 |
| Persistencia | PostgreSQL + Flyway | Transacciones, FK, auditoría, ranking | D08-D09 |
| Imágenes | Volumen local demo | Conservar binarios con storage_key opaco | D06 + D08 |
| Job de cierre | Spring @Scheduled | Cerrar ACTIVA vencida con lock | D07 + D09 |
## 3. Repositorio recomendado

```text
subastaweb/
  frontend/                # Vue 3 / Vite: src/views, src/components, src/services, router
  backend/                 # Spring Boot: api, application, domain, persistence, security
    src/main/resources/db/migration/
  docs/                    # SRS, PRD, TRD, flujos, diagramas, historias
  scripts/                 # Backup, restauracion, comandos de demo (sin secretos)
  README.md
  .env.example             # Variables sin valores secretos
```

**Convención:** rutas `/api/v1`; fechas ISO 8601 UTC; montos en COP enteros sin decimales. El servidor devuelve errores con `code`, `message`, `requestId` y `details` sin revelar secretos.

## 4. Modelo de seguridad proporcional al MVP

- **Autenticación:** sesión de Spring Security con cookie HttpOnly; Secure bajo HTTPS; SameSite=Lax y token CSRF exigido para métodos que cambian datos. El frontend usa proxy `/api` del servidor de desarrollo y mismo origen en la demo.
- **Hash:** almacenar BCrypt/Argon2, nunca contraseña legible. ADMIN creado únicamente por procedimiento de provisionamiento controlado y no por endpoint público.
- **Autorización:** USER, ADMIN y comprobación server-side del propietario del artículo. Las pantallas ocultas no sustituyen la protección API.
- **Validación:** Bean Validation para DTO y respuesta 422; imágenes JPG/PNG/WebP <=3MB, renombradas con clave opaca, sin ejecutar contenido recibido.
- **Privacidad:** catálogo público sin email ni password_hash; historial de pujas por alias y cantidad; no registrar contraseñas, cookies ni PII en logs.
- **Integridad:** clave ganadora compuesta, modificación solamente RECHAZADA, revisión única por ronda, bloqueo de fila en puja/revisión/cierre.
- **Operación:** variables de entorno para credenciales, protección de archivos `.env`, backups de demo y mecanismo reproducible de restauración.

## 5. API: catálogo de endpoints

| Método | Ruta (prefijo /api/v1) | Actor | Operación | HU | HTTP |
| --- | --- | --- | --- | --- | --- |
| POST | /auth/register | Visitante | Crear USER | HU-01 | 201 / 409 / 422 |
| POST | /auth/login | Visitante | Autenticar, cookie de sesión | HU-02 | 200 / 401 |
| POST | /auth/logout | USER/ADMIN | Invalidar sesión | HU-02 | 204 / 401 |
| GET | /auth/me | USER/ADMIN | Identidad autenticada | HU-02 | 200 / 401 |
| GET | /categories | Público | Categorías activas | HU-04 | 200 |
| POST | /auctions | USER | Crear solicitud privada + imagen | HU-04 | 201 / 401 / 422 |
| GET | /auctions/mine | USER | Mis solicitudes | HU-05 | 200 / 401 |
| PATCH | /auctions/{id} | Propietario | Modificar RECHAZADA | HU-06 | 200 / 403 / 409 |
| POST | /auctions/{id}/resubmit | Propietario | Reenviar nueva ronda | HU-06 | 200 / 409 |
| GET | /admin/auctions/pending | ADMIN | Cola pendiente | HU-07 | 200 / 403 |
| POST | /admin/auctions/{id}/review | ADMIN | Aprobar o rechazar con versión | HU-08 | 200 / 403 / 409 / 422 |
| GET | /auctions | Público | Catálogo aprobado | HU-09,HU-16 | 200 |
| GET | /auctions/{id} | Público o dueño | Detalle según visibilidad | HU-09 | 200 / 404 |
| POST | /auctions/{id}/bids | USER no vendedor | Crear puja válida | HU-10 | 201 / 401 / 403 / 409 / 422 |
| GET | /auctions/{id}/bids | Público | Historial público resumido | HU-11 | 200 / 404 |
| GET | /auctions/{id}/result | Público | Resultado final o aún activo | HU-14 | 200 / 404 / 409 |
| GET | /bids/mine | USER (opcional) | Mis ofertas | HU-15 | 200 / 401 |
| GET | /admin/auctions/{id}/reviews | ADMIN (opcional) | Historial administrativo | HU-17 | 200 / 403 |
| GET | /admin/users | ADMIN (opcional) | Listado usuarios | HU-18 | 200 / 403 |
### 5.1 Contratos JSON esenciales

**Registro** `POST /api/v1/auth/register`

```json
{"fullName":"Usuario Demo","email":"usuario@example.test","password":"********"}
```

**Nueva solicitud** `POST /api/v1/auctions`: multipart con `title`, `description`, `categoryId`, `startingPrice`, `durationHours`, `image` (archivo JPG/PNG/WebP).

**Decisión de moderación** `POST /api/v1/admin/auctions/{id}/review`

```json
{"decision":"RECHAZAR","reason":"La descripción necesita más detalles","reviewRound":1,"contentVersion":1}
```

**Puja** `POST /api/v1/auctions/{id}/bids`

```json
{"amount":21000}
```

**Detalle público** (salida parcial)

```json
{"id":42,"status":"ACTIVA","startingPrice":10000,"currentPrice":20000,
 "minNextBid":21000,"endsAt":"2026-11-02T17:00:00Z","serverTime":"2026-11-01T16:00:00Z"}
```

**Error estable**

```json
{"code":"BID_BELOW_MINIMUM","message":"La oferta no supera el mínimo vigente","requestId":"req-123"}
```

**Códigos clave:** 401 sesión no válida; 403 rol/propiedad; 404 no disponible o privado; 409 conflicto, subasta cerrada o versión antigua; 422 validación de campos. Códigos estables: `AUTH_REQUIRED`, `FORBIDDEN`, `AUCTION_NOT_PUBLIC`, `STALE_REVIEW`, `AUCTION_CLOSED`, `BID_BELOW_MINIMUM`, `SELF_BID_NOT_ALLOWED`, `INVALID_IMAGE`.

## 6. Motor transaccional y concurrencia

**Operación de puja recomendada:**

```text
BEGIN TRANSACTION;
SELECT * FROM auctions WHERE id = :auctionId FOR UPDATE;
VALIDAR role=USER, seller_id != authUser, status=ACTIVA,
        clock_timestamp() < ends_at;
SELECT amount FROM bids WHERE auction_id=:auctionId
  ORDER BY amount DESC, placed_at ASC, id ASC LIMIT 1;
VALIDAR monto >= (si no hay bids: starting_price;
                  si hay bids: max_bid + min_increment);
INSERT INTO bids(auction_id,bidder_id,amount) VALUES (...);
COMMIT;
```

Implementar con `@Transactional` y bloqueo de fila `PESSIMISTIC_WRITE`. No utilizar un `SELECT MAX(amount)` por fuera de la transacción. La misma fila también se bloquea al cerrar, evitando carrera entre job y oferta. Usar hora de servidor obtenida **después de adquirir el lock** (reloj de base de datos o equivalente confiable) para la validación del vencimiento. Confirmar la semántica de aislamiento en PostgreSQL durante las pruebas de Sprint 3.

**Moderación:** bloquear subasta, comprobar `review_round`, `content_version`, rol ADMIN y `PENDIENTE_REVISION`; insertar revisión; cambiar estado y registrar evento; commit. En aprobación: fijar `starts_at` con hora del servidor y `ends_at = starts_at + duration_hours`.

**Cierre:** job busca IDs con `status='ACTIVA' AND ends_at<=clock_timestamp()`, bloquea fila con revalidación y guarda `winning_bid_id` que corresponde a la mejor oferta, o NULL, `closed_at` y evento; repetir es seguro por la transición de estado.

## 7. Base de datos y modelado

![Modelo entidad relación](08_Diagramas/01_Modelo_Entidad_Relacion.png)

**Siete tablas:** `users`, `categories`, `auctions`, `auction_images`, `bids`, `moderation_reviews`, `auction_status_events`. Detalle de tipos, nulabilidad, índices, FK y checks en `07_Base_de_Datos/Modelo_Mejorado_y_Diccionario_v4.md` y script ejecutable propuesto `V1__schema_postgresql.sql`.

## 8. Flujos y estados

![Flujo de aprobación](08_Diagramas/05_Moderacion.png)

La máquina de estados admite únicamente: `PENDIENTE_REVISION -> RECHAZADA -> PENDIENTE_REVISION -> ACTIVA -> FINALIZADA`, con la ruta directa `PENDIENTE_REVISION -> ACTIVA`. La edición rechazada no inicia por sí misma una nueva ronda, solo el reenvío lo hace. La subasta ACTIVA no se puede editar ni cancelar desde el MVP.

![Flujo de puja concurrente](08_Diagramas/07_Puja_Transaccional.png)

![Cierre automático](08_Diagramas/08_Cierre_Automatico.png)

Flujos detallados, alternativos, secuencias y reglas RN-01..RN-16 se documentan en `06_Flujos/Flujos_Detallados_v4.md`.

## 9. Infraestructura y despliegue

**Desarrollo:** JDK compatible, Node LTS compatible con Vite, PostgreSQL local; Git y pipeline de build/test. La API usa variables `DB_URL`, `DB_USER`, `DB_PASSWORD`, `IMAGE_STORAGE_PATH` y parámetros de sesión sin publicar secretos. La SPA consume API en mismo origen/proxy.

**Entorno demo:** un servicio Spring Boot y un PostgreSQL; servir frontend compilado como estático o con hosting estático y HTTPS configurado. La elección exacta de hosting y cuotas es PEND-03. No asumir un plan gratuito indefinido ni un proveedor aprobado.

**CI:** compilar Vue, ejecutar tests; Maven tests para backend; migraciones en base de prueba; revisar PR antes de fusionar. **Entrega:** `README.md`, instrucciones de variables, restauración y datos ficticios.

## 10. Plan de pruebas y trazabilidad

| Prueba | Escenario esperado | HU |
| --- | --- | --- |
| TC-01 | Registro correcto y correo duplicado | HU-01 |
| TC-02 | Login correcto/erróneo y logout | HU-02 |
| TC-03 | 401, 403 y propiedad de artículo | HU-03 |
| TC-04 | Crear solicitud e imagen inválida | HU-04 |
| TC-05 | Mis solicitudes no filtra ajenos | HU-05 |
| TC-06 | Editar RECHAZADA, bloquear ACTIVA | HU-06 |
| TC-07 | Reenvío con/sin cambios | HU-06 |
| TC-08 | Cola solo ADMIN | HU-07 |
| TC-09 | Rechazar con motivo | HU-08 |
| TC-10 | Aprobar e iniciar reloj | HU-08 |
| TC-11 | Dos ADMIN, misma ronda -> un solo resultado | HU-08 |
| TC-12 | Catálogo oculta pendientes y rechazadas | HU-09 |
| TC-13 | Detalle público no filtra PII | HU-09 |
| TC-14 | Primera oferta >= precio inicial | HU-10 |
| TC-15 | Auto-puja y puja vencida rechazadas | HU-10 |
| TC-16 | 20 ofertas concurrentes sin doble líder | HU-10 |
| TC-17 | Precio e historial correcto | HU-11 |
| TC-18 | Reloj basado en fecha servidor | HU-12 |
| TC-19 | Cierre vencida con ganador | HU-13 |
| TC-20 | Cierre sin pujas -> NULL | HU-13 |
| TC-21 | Doble ejecución idempotente | HU-13 |
| TC-22 | Resultado público con ganador | HU-14 |
| TC-23 | Resultado sin ofertas | HU-14 |
| TC-24 | Mis ofertas / filtros condicionales | HU-15/HU-16 |
| TC-25 | Auditoría ADMIN condicional | HU-17 |
| TC-26 | Lista ADMIN sin hashes condicional | HU-18 |

**Entorno mínimo de pruebas:** 2 USER, 1 ADMIN creado de forma segura, una solicitud en cada estado y ofertas simultáneas. Separar pruebas positivas, negativas, concurrencia y una demostración integrada por sprint.

## 11. Decisiones de arquitectura (ADR) y pendientes

- ADR-01: monolito Spring Boot; elimina complejidad de microservicios.
- ADR-02: sesion basada en cookie con protección CSRF, no JWT sin necesidad.
- ADR-03: PostgreSQL con bloqueo por subasta y migraciones Flyway.
- ADR-04: Vue 3 + Vite; refresco periódico en vez de WebSockets.
- ADR-05: almacenamiento de archivos con `storage_key` opaco y limpieza de huérfanos.

**Pendientes:** confirmar Java/Spring disponibles, hosting, horas reales, retención de datos, tamaño de imagen y reglas de negocio propuestas. Las versiones exactas se bloquean en el repositorio en Sprint 1 para reproducibilidad.
