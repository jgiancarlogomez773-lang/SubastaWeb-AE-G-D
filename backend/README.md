# Backend — Java + Spring Boot (Maven)

Responsables: D05, D06, D07.

Capas: `api`, `application`, `domain`, `persistence`, `security`. Migraciones Flyway: `src/main/resources/db/migration/`.
Contratos de cada sprint: ver `../contratos/`. Rutas bajo `/api/v1`.

## Requisitos
- Java 17+ y Maven 3.9+
- PostgreSQL (base `subastaweb` por defecto)

## Variables de entorno
Ver `.env.example`. Las obligatorias: `DB_PASSWORD` (y `DB_URL` / `DB_USER` si no usas los valores por defecto).
`ADMIN_EMAIL` + `ADMIN_PASSWORD` crean un ADMIN inicial al arrancar (RN-01).

## Ejecutar
```bash
cd backend
mvn spring-boot:run
```

## Pruebas (usan H2 en memoria, no necesitan PostgreSQL)
El esquema de pruebas se genera desde las entidades. Las migraciones reales de PostgreSQL son las de Base de datos
(T1-15, T1-16, T1-17) y se prueban arrancando la aplicación contra PostgreSQL.
```bash
mvn test
```

## Sprint 1 — qué hay
| Tarea | Qué implementa |
|-------|----------------|
| T1-09 | `POST /auth/register` con hash BCrypt, correo único sin distinguir mayúsculas |
| T1-10 | `POST /auth/login` (cookie de sesión), `POST /auth/logout`, `GET /auth/me` |
| T1-11 | Proyecto Spring Boot, errores `code/message/requestId/details` |
| T1-12 | `POST /auctions` multipart → `PENDIENTE_REVISION`, imagen JPG/PNG/WebP ≤ 3 MB |
| T1-13 | `GET /categories` (público) + manejo global de errores |
| T1-14 | Reglas de acceso en `SecurityConfig` + `AuctionAccessPolicy` (propiedad y visibilidad) |

Las entidades siguen el modelo oficial `Docs/07_Base_de_Datos` (tablas `users`, `categories`, `auctions`, `auction_images`).
La duración de una subasta solo puede ser 24, 48 o 72 horas (RN-06).

## Sprint 2 — qué hay
| Tarea | Qué implementa |
|-------|----------------|
| T2-13 | `GET /auctions?page=0&size=12` — catálogo público paginado, solo ACTIVA y FINALIZADA (más recientes primero, `size` máx. 50) |
| T2-14 | `GET /auctions/{id}` — detalle con DTO público sin datos del vendedor; una subasta privada responde 404 (`AUCTION_NOT_PUBLIC`) salvo a su dueño ("Público o dueño") |

