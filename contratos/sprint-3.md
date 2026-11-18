# Contrato del Sprint 3 — Motor de ofertas y cierre automático

**Proyecto:** SubastaWeb · **Documentos base:** SRS-SW-004 v4.0 y PLAN-SW-004 v4.0 · **Hito de cierre:** HITO-S3
**Fechas:** inicio mar 01/12/2026 · entrega lun 07/12/2026

> Este contrato fija lo que el equipo se compromete a entregar y lo que Frontend, Backend y Base de datos deben respetar entre sí. Cualquier cambio de alcance lo decide PO-01 y se registra en este archivo mediante Pull Request.

## 1. Convenciones comunes de la API
- Prefijo de rutas: `/api/v1`. Fechas en ISO 8601 UTC. Montos en COP, enteros sin decimales.
- Errores con `code`, `message`, `requestId` y `details`, sin revelar secretos.
- Códigos HTTP: 401 sesión no válida · 403 rol/propiedad · 404 no disponible o privado · 409 conflicto/cerrada/versión antigua · 422 validación.
- Códigos estables: `AUTH_REQUIRED`, `FORBIDDEN`, `AUCTION_NOT_PUBLIC`, `STALE_REVIEW`, `AUCTION_CLOSED`, `BID_BELOW_MINIMUM`, `SELF_BID_NOT_ALLOWED`, `INVALID_IMAGE`.

## 2. Historias comprometidas

### HU-10 — Registrar puja
**Historia:** Como comprador, quiero ofertar de forma válida y segura.  
**Prioridad:** Debe · **RF:** RF-PUJ-01, RF-PUJ-02, RF-PUJ-03  
**Tareas vinculadas:** T3-01, T3-03, T3-04, T3-09, T3-10, T3-15, T3-16, T3-17, T3-18.

**Criterios de aceptación**
- [ ] Dado ACTIVA ajena, cuando oferto mínimo válido, entonces se registra 201.
- [ ] Dado el vendedor, cuando intenta ofertar, entonces se rechaza.
- [ ] Dado dos ofertas simultáneas por debajo del nuevo mínimo, cuando compiten, entonces solo persiste la válida.

### HU-11 — Precio actual e historial
**Historia:** Como visitante, quiero observar precio líder e historial público.  
**Prioridad:** Debe · **RF:** RF-PUJ-04  
**Tareas vinculadas:** T3-01, T3-05, T3-06, T3-11, T3-12.

**Criterios de aceptación**
- [ ] Dado subasta sin ofertas, cuando consulto precio actual, entonces se muestra precio inicial.
- [ ] Dado ofertas aceptadas, cuando consulto historial, entonces veo precio líder y orden.
- [ ] Dado visitante, cuando consulta pujas, entonces no recibe datos personales.

### HU-12 — Temporizador
**Historia:** Como participante, quiero saber cuánto tiempo queda.  
**Prioridad:** Debe · **RF:** RF-TMP-01  
**Tareas vinculadas:** T3-01, T3-02, T3-07, T3-08, T3-13.

**Criterios de aceptación**
- [ ] Dado ACTIVA, cuando carga detalle, entonces se muestra contador basado en ends_at y server_time.
- [ ] Dado vencimiento, cuando transcurre el tiempo, entonces el contador llega a cero.
- [ ] Dado reloj local adulterado, cuando intento pujar después de ends_at, entonces el servidor rechaza.

### HU-13 — Cierre y ganador interno
**Historia:** Como sistema, quiero finalizar vencidas y guardar ganador o nulo.  
**Prioridad:** Debe · **RF:** RF-CIE-01, RF-CIE-02  
**Tareas vinculadas:** T3-02, T3-08, T3-14, T3-15, T3-18.

**Criterios de aceptación**
- [ ] Dado una ACTIVA vencida, cuando corre el trabajo de cierre, entonces pasa a FINALIZADA.
- [ ] Dado 0 pujas, cuando cierra, entonces winner_bid_id queda NULL.
- [ ] Dado doble ejecución del trabajo, cuando vuelve a cerrar, entonces resultado permanece inalterado.

## 3. Endpoints del sprint

| Método | Ruta (prefijo /api/v1) | Actor | Operación | HU | HTTP |
| --- | --- | --- | --- | --- | --- |
| POST | /auctions/{id}/bids | USER no vendedor | Crear puja válida | HU-10 | 201 / 401 / 403 / 409 / 422 |
| GET | /auctions/{id}/bids | Público | Historial público resumido | HU-11 | 200 / 404 |

## 4. Contratos JSON esenciales

**Puja `POST /api/v1/auctions/{id}/bids`**

```json
{"amount":21000}
```

**Detalle público (salida parcial) `GET /api/v1/auctions/{id}`**

```json
{"id":42,"status":"ACTIVA","startingPrice":10000,"currentPrice":20000,
 "minNextBid":21000,"endsAt":"2026-11-02T17:00:00Z","serverTime":"2026-11-01T16:00:00Z"}
```

## 5. Reglas de negocio aplicables

- **RN-08:** Primer bid >= precio inicial; siguientes >= mayor bid + COP 1.000.
- **RN-09:** El propietario no puede ofertar por su propia publicación.
- **RN-10:** No se admiten bids después de ends_at, incluso si el worker no ha cerrado.
- **RN-11:** El control de monto y el insert deben ejecutarse bajo bloqueo de la fila auction.
- **RN-12:** Cada puja válida es inmutable; no se eliminan bids en el MVP.
- **RN-13:** Al cerrar, gana la mayor oferta aceptada; si no hubo pujas, NULL.
- **RN-14:** El cierre es idempotente, aunque el job se ejecute varias veces.

## 6. Compromisos por equipo

| Equipo | Integrantes | Entrega mínima del sprint |
| --- | --- | --- |
| Diseño | D01 | T3-01 Prototipos puja, precio e historial; T3-02 Estados visuales del temporizador y cierre |
| Frontend (Vue 3) | D02, D03, D04 | T3-03 Formulario de pujas con estados; T3-04 Conectar POST pujas + conflictos 409; T3-05 Precio actual e historial público; T3-06 Refrescar ofertas tras puja; T3-07 Cuenta regresiva basada en hora servidor; T3-08 Estado finalizada y bloqueo visual |
| Backend (Spring Boot) | D05, D06, D07 | T3-09 Validar oferta ascendente y reglas; T3-10 Transacción de pujas simultáneas; T3-11 GET precio líder para un artículo; T3-12 GET historial paginado sin datos privados; T3-13 Fecha final y hora oficial en API; T3-14 Job cierre idempotente + ganador |
| Base de datos / QA | D08, D09 | T3-15 Migración bids y referencia de ganador; T3-16 Índices de ofertas y estrategia de bloqueo; T3-17 Fixtures y prueba de concurrencia sobre PostgreSQL; T3-18 Pruebas integradas de pujas y cierre |
| Producto | PO-01, PO-02 | G3-01 Confirmar reglas de puja y cierre; G3-02 Diseñar escenarios negativos de pujas |
| Scrum | SM-01 | G3-03 Activar Sprint 3 y revisar riesgos |

## 7. Cadena de dependencias y hito
- **HITO-S3** debe cumplirse antes de iniciar el siguiente sprint.
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
