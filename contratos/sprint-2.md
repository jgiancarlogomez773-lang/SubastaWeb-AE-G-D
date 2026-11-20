# Contrato del Sprint 2 — Moderación administrativa y publicación

**Proyecto:** SubastaWeb · **Documentos base:** SRS-SW-004 v4.0 y PLAN-SW-004 v4.0 · **Hito de cierre:** HITO-S2
**Fechas:** inicio mar 24/11/2026 · entrega dom 29/11/2026

> Este contrato fija lo que el equipo se compromete a entregar y lo que Frontend, Backend y Base de datos deben respetar entre sí. Cualquier cambio de alcance lo decide PO-01 y se registra en este archivo mediante Pull Request.

## 1. Convenciones comunes de la API
- Prefijo de rutas: `/api/v1`. Fechas en ISO 8601 UTC. Montos en COP, enteros sin decimales.
- Errores con `code`, `message`, `requestId` y `details`, sin revelar secretos.
- Códigos HTTP: 401 sesión no válida · 403 rol/propiedad · 404 no disponible o privado · 409 conflicto/cerrada/versión antigua · 422 validación.
- Códigos estables: `AUTH_REQUIRED`, `FORBIDDEN`, `AUCTION_NOT_PUBLIC`, `STALE_REVIEW`, `AUCTION_CLOSED`, `BID_BELOW_MINIMUM`, `SELF_BID_NOT_ALLOWED`, `INVALID_IMAGE`.

## 2. Historias comprometidas

### HU-05 — Consultar mis solicitudes
**Historia:** Como usuario, quiero consultar el estado de mis solicitudes.  
**Prioridad:** Debe · **RF:** RF-SUB-02  
**Tareas vinculadas:** T2-01, T2-03, T2-09, T2-18.

**Criterios de aceptación**
- [ ] Dado un USER, cuando consulta Mis subastas, entonces solo ve publicaciones propias con estado.
- [ ] Dado una publicación rechazada, cuando consulto la ficha propia, entonces veo causa.
- [ ] Dado otro USER, cuando solicita el mismo ID privado, entonces la API deniega el acceso.

### HU-06 — Editar y reenviar rechazada
**Historia:** Como usuario, quiero corregir una solicitud rechazada y reenviarla.  
**Prioridad:** Debe · **RF:** RF-SUB-03, RF-SUB-04  
**Tareas vinculadas:** T2-01, T2-04, T2-10, T2-16.

**Criterios de aceptación**
- [ ] Dado una rechazada, cuando edito contenido y reenvío, entonces queda PENDIENTE_REVISION en nueva ronda.
- [ ] Dado una rechazada sin cambios, cuando reenvío, entonces obtengo 409.
- [ ] Dado una subasta ACTIVA, cuando intento editar, entonces el backend rechaza el cambio.

### HU-07 — Cola de revisión ADMIN
**Historia:** Como administrador, quiero ver las solicitudes pendientes.  
**Prioridad:** Debe · **RF:** RF-MOD-01  
**Tareas vinculadas:** T2-01, T2-05, T2-11, T2-15.

**Criterios de aceptación**
- [ ] Dado ADMIN, cuando consulta pendientes, entonces recibe solicitudes con versión vigente.
- [ ] Dado USER, cuando solicita la bandeja, entonces obtiene 403.
- [ ] Dado que no hay pendientes, cuando abre bandeja, entonces ve listado vacío sin error.

### HU-08 — Aprobar o rechazar
**Historia:** Como administrador, quiero decidir con motivo y control de versión.  
**Prioridad:** Debe · **RF:** RF-MOD-02, RF-MOD-03, RF-MOD-04  
**Tareas vinculadas:** T2-01, T2-06, T2-12, T2-15, T2-16, T2-17.

**Criterios de aceptación**
- [ ] Dado ADMIN y solicitud pendiente, cuando aprueba, entonces queda ACTIVA con starts_at y ends_at.
- [ ] Dado rechazo, cuando registra motivo válido, entonces el usuario ve RECHAZADA y la causa.
- [ ] Dado una revisión ya decidida, cuando intenta decidir de nuevo, entonces obtiene 409 sin duplicar decisión.

### HU-09 — Catálogo y detalle público
**Historia:** Como visitante, quiero consultar subastas aprobadas.  
**Prioridad:** Debe · **RF:** RF-CAT-01, RF-CAT-02  
**Tareas vinculadas:** T2-02, T2-07, T2-08, T2-13, T2-14, T2-18.

**Criterios de aceptación**
- [ ] Dado una ACTIVA, cuando entro sin sesión, entonces veo catálogo y detalle.
- [ ] Dado una RECHAZADA o PENDIENTE_REVISION, cuando consulto público, entonces no aparece.
- [ ] Dado detalle público, cuando examino respuesta, entonces no hay emails ni hashes.

## 3. Endpoints del sprint

| Método | Ruta (prefijo /api/v1) | Actor | Operación | HU | HTTP |
| --- | --- | --- | --- | --- | --- |
| GET | /auctions/mine | USER | Mis solicitudes | HU-05 | 200 / 401 |
| PATCH | /auctions/{id} | Propietario | Modificar RECHAZADA | HU-06 | 200 / 403 / 409 |
| POST | /auctions/{id}/resubmit | Propietario | Reenviar nueva ronda | HU-06 | 200 / 409 |
| GET | /admin/auctions/pending | ADMIN | Cola pendiente | HU-07 | 200 / 403 |
| POST | /admin/auctions/{id}/review | ADMIN | Aprobar o rechazar con versión | HU-08 | 200 / 403 / 409 / 422 |
| GET | /auctions | Público | Catálogo aprobado | HU-09,HU-16 | 200 |
| GET | /auctions/{id} | Público o dueño | Detalle según visibilidad | HU-09 | 200 / 404 |

## 4. Contratos JSON esenciales

**Decisión de moderación `POST /api/v1/admin/auctions/{id}/review`**

```json
{"decision":"RECHAZAR","reason":"La descripción necesita más detalles","reviewRound":1,"contentVersion":1}
```

## 5. Reglas de negocio aplicables

- **RN-03:** La revisión ADMIN es atómica: una decisión por ronda.
- **RN-04:** Solo RECHAZADA se puede editar; para reenviar debe haber cambios desde el rechazo.
- **RN-05:** Al aprobar, starts_at y ends_at se calculan desde hora de servidor.
- **RN-06:** La duración permitida es 24, 48 o 72 h. Pendiente de aprobación final de PO.

## 6. Compromisos por equipo

| Equipo | Integrantes | Entrega mínima del sprint |
| --- | --- | --- |
| Diseño | D01 | T2-01 Prototipo mis subastas y bandeja ADMIN; T2-02 Prototipo de catálogo y detalle público |
| Frontend (Vue 3) | D02, D03, D04 | T2-03 Vista Mis solicitudes con estados; T2-04 Edición y reenvío desde Vue; T2-05 Bandeja de pendientes ADMIN; T2-06 Acciones aprobar/rechazar con confirmación; T2-07 Catálogo público Vue; T2-08 Detalle público de artículo |
| Backend (Spring Boot) | D05, D06, D07 | T2-09 GET /auctions/mine por sesión; T2-10 PATCH + reenvío de rechazadas; T2-11 GET cola de revisión ADMIN; T2-12 Aprobar/rechazar con transacción y auditoría; T2-13 GET catálogo exclusivamente público; T2-14 GET detalle protegido por estado |
| Base de datos / QA | D08, D09 | T2-15 Migración moderation_decisions y versión; T2-16 Integridad rondas y estados en BD; T2-17 Semillas y pruebas de edición concurrente; T2-18 Prueba de flujo de moderación completo |
| Producto | PO-01, PO-02 | G2-01 Validar reglas de moderación; G2-02 Afinar criterios HU-05 a HU-09 |
| Scrum | SM-01 | G2-03 Revisar capacidad, defectos y objetivo S2 |

## 7. Cadena de dependencias y hito
- **HITO-S2** debe cumplirse antes de iniciar el siguiente sprint.
- Una tarea pasa a *En progreso* solo si todas sus predecesoras tienen evidencia demostrada.
- Una historia pasa a *Done* cuando frontend, backend, base de datos, pruebas y validación del PO están integrados en la rama de desarrollo.

## 8. Definition of Done del sprint
- Implementación integrada en la rama de desarrollo y revisada por Pull Request.
- Pruebas positivas y negativas ejecutadas; sin errores P0.
- Contrato API y migración revisados; documentación afectada actualizada.

## 9. Firma de aceptación

| Rol | Responsable | Fecha | Aceptado |
| --- | --- | --- | --- |
| Product Owner | PO-01 | | [ ] |
| Scrum Master | SM-01 | | [ ] |
| Backend | | | [ ] |
| Frontend | | | [ ] |
| Base de datos | | | [ ] |
