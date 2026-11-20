# Contrato del Sprint 4 — Resultados y estabilización

**Proyecto:** SubastaWeb · **Documentos base:** SRS-SW-004 v4.0 y PLAN-SW-004 v4.0 · **Hito de cierre:** HITO-S4
**Fechas:** inicio mar 08/12/2026 · entrega dom 13/12/2026

> Este contrato fija lo que el equipo se compromete a entregar y lo que Frontend, Backend y Base de datos deben respetar entre sí. Cualquier cambio de alcance lo decide PO-01 y se registra en este archivo mediante Pull Request.

## 1. Convenciones comunes de la API
- Prefijo de rutas: `/api/v1`. Fechas en ISO 8601 UTC. Montos en COP, enteros sin decimales.
- Errores con `code`, `message`, `requestId` y `details`, sin revelar secretos.
- Códigos HTTP: 401 sesión no válida · 403 rol/propiedad · 404 no disponible o privado · 409 conflicto/cerrada/versión antigua · 422 validación.
- Códigos estables: `AUTH_REQUIRED`, `FORBIDDEN`, `AUCTION_NOT_PUBLIC`, `STALE_REVIEW`, `AUCTION_CLOSED`, `BID_BELOW_MINIMUM`, `SELF_BID_NOT_ALLOWED`, `INVALID_IMAGE`.

## 2. Historias comprometidas

### HU-14 — Resultados finales
**Historia:** Como visitante, quiero consultar el resultado cuando termina una subasta.  
**Prioridad:** Debe · **RF:** RF-RES-01  
**Tareas vinculadas:** T4-01, T4-02, T4-03, T4-04, T4-09, T4-10, T4-15, T4-16, T4-17, T4-18.

**Criterios de aceptación**
- [ ] Dado FINALIZADA con pujas, cuando veo el resultado, entonces obtengo alias y monto ganador.
- [ ] Dado FINALIZADA sin pujas, cuando veo resultado, entonces indica Sin ofertas.
- [ ] Dado ACTIVA, cuando consulto resultado final, entonces no se declara ganador anticipadamente.

### HU-15 — Mis ofertas
**Historia:** Como usuario, quiero consultar mis ofertas registradas.  
**Prioridad:** Debería · **RF:** RF-CLI-01 · **CONDICIONAL** (solo si PO-01 confirma que no hay defectos bloqueantes del MVP)  
**Tareas vinculadas:** T4-05, T4-11.

**Criterios de aceptación**
- [ ] Dado USER, cuando consulta Mis ofertas, entonces ve exclusivamente sus pujas.
- [ ] Dado USER A, cuando suplanta ID de USER B, entonces no recibe ofertas de B.

### HU-16 — Filtros de catálogo
**Historia:** Como visitante, quiero filtrar subastas aprobadas.  
**Prioridad:** Debería · **RF:** RF-CAT-03 · **CONDICIONAL** (solo si PO-01 confirma que no hay defectos bloqueantes del MVP)  
**Tareas vinculadas:** T4-06, T4-12.

**Criterios de aceptación**
- [ ] Dado catálogo público, cuando filtro categoría o título, entonces lista resultados públicos.
- [ ] Dado filtro sin coincidencias, cuando ejecuto, entonces veo estado vacío sin privados.

### HU-17 — Historial de moderación
**Historia:** Como administrador, quiero auditar decisiones anteriores.  
**Prioridad:** Debería · **RF:** RF-MOD-05 · **CONDICIONAL** (solo si PO-01 confirma que no hay defectos bloqueantes del MVP)  
**Tareas vinculadas:** T4-07, T4-13.

**Criterios de aceptación**
- [ ] Dado ADMIN, cuando consulta una subasta reenviada, entonces ve decisiones por ronda.
- [ ] Dado USER, cuando consulta auditoría, entonces recibe 403.

### HU-18 — Administración de usuarios
**Historia:** Como administrador, quiero consultar las cuentas registradas.  
**Prioridad:** Debería · **RF:** RF-ADM-01 · **CONDICIONAL** (solo si PO-01 confirma que no hay defectos bloqueantes del MVP)  
**Tareas vinculadas:** T4-08, T4-14.

**Criterios de aceptación**
- [ ] Dado ADMIN, cuando lista usuarios, entonces ve correo, nombre y rol.
- [ ] Dado USER, cuando lista usuarios, entonces recibe 403.
- [ ] Dado cualquier respuesta, cuando la examino, entonces no incluye password_hash.

## 3. Endpoints del sprint

| Método | Ruta (prefijo /api/v1) | Actor | Operación | HU | HTTP |
| --- | --- | --- | --- | --- | --- |
| GET | /auctions/{id}/result | Público | Resultado final o aún activo | HU-14 | 200 / 404 / 409 |
| GET | /bids/mine | USER (opcional) | Mis ofertas | HU-15 | 200 / 401 |
| GET | /auctions | Público | Catálogo aprobado | HU-09,HU-16 | 200 |
| GET | /admin/auctions/{id}/reviews | ADMIN (opcional) | Historial administrativo | HU-17 | 200 / 403 |
| GET | /admin/users | ADMIN (opcional) | Listado usuarios | HU-18 | 200 / 403 |

## 4. Contratos JSON esenciales

**Error estable (formato común a toda la API)**

```json
{"code":"BID_BELOW_MINIMUM","message":"La oferta no supera el mínimo vigente","requestId":"req-123"}
```

## 5. Reglas de negocio aplicables

- **RN-13:** Al cerrar, gana la mayor oferta aceptada; si no hubo pujas, NULL.
- **RN-16:** El público consulta alias del ganador, sin correo ni teléfono.

## 6. Compromisos por equipo

| Equipo | Integrantes | Entrega mínima del sprint |
| --- | --- | --- |
| Diseño | D01 | T4-01 Diseño pantalla resultado y estado sin ofertas; T4-02 Revisión accesibilidad y consistencia del MVP |
| Frontend (Vue 3) | D02, D03, D04 | T4-03 Vista Vue ganador / sin ganador; T4-04 Integración y revisión visual del recorrido P0; T4-05 Vista Mis ofertas (condicional); T4-06 Filtros de catálogo Vue (condicional); T4-07 Historial de moderación UI (condicional); T4-08 Listado de usuarios ADMIN (condicional) |
| Backend (Spring Boot) | D05, D06, D07 | T4-09 API resultado final y anonimización; T4-10 Endurecimiento de API y errores P0; T4-11 GET /bids/mine (condicional); T4-12 GET catálogo con filtros (condicional); T4-13 GET historial ADMIN (condicional); T4-14 GET usuarios ADMIN (condicional) |
| Base de datos / QA | D08, D09 | T4-15 Semillas de demo y respaldo de release; T4-16 Auditar integridad winner y fechas; T4-17 E2E y regresión de cierre, pujas y seguridad; T4-18 Entorno demostración, README y smoke test |
| Producto | PO-01, PO-02 | G4-01 Priorizar entrega final y decidir extras; G4-02 Planificar demo y validación final |
| Scrum | SM-01 | G4-03 Gestionar estabilización y despliegue |

## 7. Cadena de dependencias y hito
- **HITO-S4** debe cumplirse antes de iniciar el siguiente sprint.
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
