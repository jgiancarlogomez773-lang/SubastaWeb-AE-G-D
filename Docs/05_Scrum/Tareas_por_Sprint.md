# Tareas organizadas por sprint

**Proyecto:** SubastaWeb · **Fuente:** PLAN-SW-004 v4.0 · **Total:** 84 tareas (72 técnicas `T` + 12 de gestión `G`)

Cada sprint se entrega el **domingo** (día 5: integración, QA, Review y Retrospectiva). El estado de cada tarea se sigue en el tablero del proyecto y en los issues; este documento no lo refleja.

## Resumen

| Sprint | Objetivo | Historias | Inicio | Entrega | Tareas |
| --- | --- | --- | --- | --- | --- |
| 1 | Registro, acceso y solicitud privada | HU-01 a HU-04 | mié 18/11 | **dom 22/11** | 21 |
| 2 | Moderación administrativa y publicación | HU-05 a HU-09 | mar 24/11 | **dom 29/11** | 21 |
| 3 | Motor de ofertas y cierre automático | HU-10 a HU-13 | mar 01/12 | **dom 06/12** | 21 |
| 4 | Resultados y estabilización | HU-14 (obligatoria); HU-15 a HU-18 condicionales | mar 08/12 | **dom 13/12** | 21 |

## Carga por persona (tareas en todo el proyecto)

| Persona (GitHub) | Código | Tareas |
| --- | --- | --- |
| jgiancarlogomez773-lang | PO-01 | 4 |
| Joseber32 | PO-02 | 4 |
| eljefewichi23-sketch | SM-01 | 4 |
| MariA-00001 | D01 | 8 |
| 1509-d | D02 | 8 |
| 1509-d | D03 | 8 |
| 1509-d | D04 | 8 |
| yeisonglvz | D05 | 8 |
| DanBan1919 | D06 | 8 |
| yuleyker | D07 | 8 |
| Tolo-33 | D08 | 8 |
| Tolo-33 | D09 | 8 |

## Sprint 1 — Registro, acceso y solicitud privada

**Fechas:** mié 18/11 al dom 22/11/2026 · **Hito de cierre:** HITO-S1 · **Historias:** HU-01 a HU-04

| Código | Tarea | Responsable | Día | Horas | Historia | Predecesoras |
| --- | --- | --- | --- | --- | --- | --- |
| G1-01 | Definir reglas de negocio base | PO-01 (`jgiancarlogomez773-lang`) | mié 18/11 | 2 h | — | Inicio del proyecto (sin requisito previo) |
| G1-02 | Revisar criterios de aceptación y datos | PO-02 (`Joseber32`) | mié 18/11 | 2 h | — | G1-01 |
| G1-03 | Acordar objetivo y calidad del sprint | SM-01 (`eljefewichi23-sketch`) | mié 18/11 | 2 h | — | G1-01 |
| T1-01 | Prototipos registro, login y nueva subasta | D01 (`MariA-00001`) | mié 18/11 | 4 h | HU-01, HU-02, HU-04 | G1-01, G1-02 |
| T1-15 | Migración users y categories | D08 (`Tolo-33`) | mié 18/11 | 4 h | HU-01, HU-04 | G1-01 |
| T1-02 | Sistema visual y estados de validación | D01 (`MariA-00001`) | jue 19/11 | 4 h | HU-03, HU-04 | T1-01, G1-03 |
| T1-03 | Iniciar Vue 3 + Vite y navegación | D02 (`1509-d`) | jue 19/11 | 4 h | HU-01, HU-02 | T1-01, G1-03 |
| T1-11 | Inicializar Spring Boot y contrato de solicitud | D06 (`DanBan1919`) | jue 19/11 | 4 h | HU-04 | G1-01, T1-16 |
| T1-16 | Migración auctions + estados iniciales | D08 (`Tolo-33`) | jue 19/11 | 4 h | HU-04 | T1-15 |
| T1-05 | Pantalla login, logout y guardas Vue | D03 (`1509-d`) | vie 20/11 | 4 h | HU-02, HU-03 | T1-02, T1-03 |
| T1-07 | Formulario de solicitud e imagen | D04 (`1509-d`) | vie 20/11 | 4 h | HU-04 | T1-02, T1-11 |
| T1-09 | POST /auth/register + hash contraseña | D05 (`yeisonglvz`) | vie 20/11 | 4 h | HU-01 | T1-15, T1-11, G1-01 |
| T1-13 | GET de categorías disponibles | D07 (`yuleyker`) | vie 20/11 | 4 h | HU-04 | T1-15, T1-11 |
| T1-17 | Semillas y conexión PostgreSQL/Spring | D09 (`Tolo-33`) | vie 20/11 | 4 h | HU-01, HU-04 | T1-15, T1-16 |
| T1-04 | Integrar registro con Spring Boot | D02 (`1509-d`) | sáb 21/11 | 4 h | HU-01 | T1-03, T1-09 |
| T1-10 | Login, logout y usuario actual con Spring Security | D05 (`yeisonglvz`) | sáb 21/11 | 4 h | HU-02 | T1-09, T1-17 |
| T1-12 | Guardar solicitud privada con imagen | D06 (`DanBan1919`) | sáb 21/11 | 4 h | HU-04 | T1-11, T1-17 |
| T1-06 | Conectar sesión real y verificar roles | D03 (`1509-d`) | dom 22/11 | 4 h | HU-02, HU-03 | T1-05, T1-10, T1-14 |
| T1-08 | Conectar solicitud a API y confirmación | D04 (`1509-d`) | dom 22/11 | 4 h | HU-04 | T1-07, T1-12, T1-13 |
| T1-14 | Autorización USER/ADMIN y propiedad | D07 (`yuleyker`) | dom 22/11 | 4 h | HU-03 | T1-10, T1-12, G1-03 |
| T1-18 | Prueba integrada y respaldo de datos del Sprint 1 | D09 (`Tolo-33`) | dom 22/11 | 4 h | HU-01…HU-04 | T1-04, T1-06, T1-08, T1-14, T1-17 |

## Sprint 2 — Moderación administrativa y publicación

**Fechas:** mar 24/11 al dom 29/11/2026 · **Hito de cierre:** HITO-S2 · **Historias:** HU-05 a HU-09

| Código | Tarea | Responsable | Día | Horas | Historia | Predecesoras |
| --- | --- | --- | --- | --- | --- | --- |
| G2-01 | Validar reglas de moderación | PO-01 (`jgiancarlogomez773-lang`) | mar 24/11 | 2 h | — | HITO-S1 |
| G2-02 | Afinar criterios HU-05 a HU-09 | PO-02 (`Joseber32`) | mar 24/11 | 2 h | — | G2-01 |
| G2-03 | Revisar capacidad, defectos y objetivo S2 | SM-01 (`eljefewichi23-sketch`) | mar 24/11 | 2 h | — | HITO-S1 |
| T2-01 | Prototipo mis subastas y bandeja ADMIN | D01 (`MariA-00001`) | mar 24/11 | 4 h | HU-05, HU-06, HU-07, HU-08 | G2-01, G2-02 |
| T2-15 | Migración moderation_decisions y versión | D08 (`Tolo-33`) | mar 24/11 | 4 h | HU-07, HU-08 | G2-01, T1-16 |
| T2-02 | Prototipo de catálogo y detalle público | D01 (`MariA-00001`) | mié 25/11 | 4 h | HU-09 | T2-01 |
| T2-09 | GET /auctions/mine por sesión | D05 (`yeisonglvz`) | mié 25/11 | 4 h | HU-05 | T1-12, T1-14 |
| T2-11 | GET cola de revisión ADMIN | D06 (`DanBan1919`) | mié 25/11 | 4 h | HU-07 | T1-14, T2-15 |
| T2-16 | Integridad rondas y estados en BD | D08 (`Tolo-33`) | mié 25/11 | 4 h | HU-06, HU-08 | T2-15 |
| T2-03 | Vista Mis solicitudes con estados | D02 (`1509-d`) | jue 26/11 | 4 h | HU-05 | T2-01, T2-09 |
| T2-05 | Bandeja de pendientes ADMIN | D03 (`1509-d`) | jue 26/11 | 4 h | HU-07 | T2-01, T2-11 |
| T2-13 | GET catálogo exclusivamente público | D07 (`yuleyker`) | jue 26/11 | 4 h | HU-09 | T2-15 |
| T2-17 | Semillas y pruebas de edición concurrente | D09 (`Tolo-33`) | jue 26/11 | 4 h | HU-08 | T2-15, T2-16 |
| T2-07 | Catálogo público Vue | D04 (`1509-d`) | vie 27/11 | 4 h | HU-09 | T2-02, T2-13 |
| T2-10 | PATCH + reenvío de rechazadas | D05 (`yeisonglvz`) | vie 27/11 | 4 h | HU-06 | T2-09, T2-16 |
| T2-12 | Aprobar/rechazar con transacción y auditoría | D06 (`DanBan1919`) | vie 27/11 | 4 h | HU-08 | T2-11, T2-16, T2-17 |
| T2-14 | GET detalle protegido por estado | D07 (`yuleyker`) | vie 27/11 | 4 h | HU-09 | T2-13 |
| T2-04 | Edición y reenvío desde Vue | D02 (`1509-d`) | dom 29/11 | 4 h | HU-06 | T2-03, T2-10 |
| T2-06 | Acciones aprobar/rechazar con confirmación | D03 (`1509-d`) | dom 29/11 | 4 h | HU-08 | T2-05, T2-12 |
| T2-08 | Detalle público de artículo | D04 (`1509-d`) | dom 29/11 | 4 h | HU-09 | T2-07, T2-14 |
| T2-18 | Prueba de flujo de moderación completo | D09 (`Tolo-33`) | dom 29/11 | 4 h | HU-05…HU-09 | T2-04, T2-06, T2-08, T2-12, T2-17 |

## Sprint 3 — Motor de ofertas y cierre automático

**Fechas:** mar 01/12 al dom 06/12/2026 · **Hito de cierre:** HITO-S3 · **Historias:** HU-10 a HU-13

| Código | Tarea | Responsable | Día | Horas | Historia | Predecesoras |
| --- | --- | --- | --- | --- | --- | --- |
| G3-01 | Confirmar reglas de puja y cierre | PO-01 (`jgiancarlogomez773-lang`) | mar 01/12 | 2 h | — | HITO-S2 |
| G3-02 | Diseñar escenarios negativos de pujas | PO-02 (`Joseber32`) | mar 01/12 | 2 h | — | G3-01 |
| G3-03 | Activar Sprint 3 y revisar riesgos | SM-01 (`eljefewichi23-sketch`) | mar 01/12 | 2 h | — | HITO-S2 |
| T3-01 | Prototipos puja, precio e historial | D01 (`MariA-00001`) | mar 01/12 | 4 h | HU-10, HU-11, HU-12 | G3-01, G3-02 |
| T3-15 | Migración bids y referencia de ganador | D08 (`Tolo-33`) | mar 01/12 | 4 h | HU-10, HU-13 | G3-01, T2-16 |
| T3-02 | Estados visuales del temporizador y cierre | D01 (`MariA-00001`) | mié 02/12 | 4 h | HU-12, HU-13 | T3-01 |
| T3-11 | GET precio líder para un artículo | D06 (`DanBan1919`) | mié 02/12 | 4 h | HU-11 | T3-15 |
| T3-13 | Fecha final y hora oficial en API | D07 (`yuleyker`) | mié 02/12 | 4 h | HU-12 | T2-12 |
| T3-16 | Índices de ofertas y estrategia de bloqueo | D08 (`Tolo-33`) | mié 02/12 | 4 h | HU-10 | T3-15 |
| T3-03 | Formulario de pujas con estados | D02 (`1509-d`) | jue 03/12 | 4 h | HU-10 | T3-01, T3-09 |
| T3-07 | Cuenta regresiva basada en hora servidor | D04 (`1509-d`) | jue 03/12 | 4 h | HU-12 | T3-02, T3-13 |
| T3-09 | Validar oferta ascendente y reglas | D05 (`yeisonglvz`) | jue 03/12 | 4 h | HU-10 | T3-15, G3-01 |
| T3-12 | GET historial paginado sin datos privados | D06 (`DanBan1919`) | jue 03/12 | 4 h | HU-11 | T3-11 |
| T3-17 | Fixtures y prueba de concurrencia sobre PostgreSQL | D09 (`Tolo-33`) | jue 03/12 | 4 h | HU-10 | T3-16 |
| T3-05 | Precio actual e historial público | D03 (`1509-d`) | vie 04/12 | 4 h | HU-11 | T3-01, T3-12 |
| T3-10 | Transacción de pujas simultáneas | D05 (`yeisonglvz`) | vie 04/12 | 4 h | HU-10 | T3-09, T3-16, T3-17 |
| T3-14 | Job cierre idempotente + ganador | D07 (`yuleyker`) | vie 04/12 | 4 h | HU-13 | T3-13, T3-15, T3-16 |
| T3-04 | Conectar POST pujas + conflictos 409 | D02 (`1509-d`) | dom 06/12 | 4 h | HU-10 | T3-03, T3-10 |
| T3-06 | Refrescar ofertas tras puja | D03 (`1509-d`) | dom 06/12 | 4 h | HU-11 | T3-04, T3-05 |
| T3-08 | Estado finalizada y bloqueo visual | D04 (`1509-d`) | dom 06/12 | 4 h | HU-12, HU-13 | T3-07, T3-14 |
| T3-18 | Pruebas integradas de pujas y cierre | D09 (`Tolo-33`) | dom 06/12 | 4 h | HU-10…HU-13 | T3-04, T3-08, T3-10, T3-14, T3-17 |

## Sprint 4 — Resultados y estabilización

**Fechas:** mar 08/12 al dom 13/12/2026 · **Hito de cierre:** HITO-S4 · **Historias:** HU-14 (obligatoria); HU-15 a HU-18 condicionales

| Código | Tarea | Responsable | Día | Horas | Historia | Predecesoras |
| --- | --- | --- | --- | --- | --- | --- |
| G4-01 | Priorizar entrega final y decidir extras | PO-01 (`jgiancarlogomez773-lang`) | mar 08/12 | 2 h | — | HITO-S3 |
| G4-02 | Planificar demo y validación final | PO-02 (`Joseber32`) | mar 08/12 | 2 h | — | G4-01 |
| G4-03 | Gestionar estabilización y despliegue | SM-01 (`eljefewichi23-sketch`) | mar 08/12 | 2 h | — | HITO-S3 |
| T4-01 | Diseño pantalla resultado y estado sin ofertas | D01 (`MariA-00001`) | mar 08/12 | 4 h | HU-14 | G4-01, G4-02 |
| T4-09 | API resultado final y anonimización | D05 (`yeisonglvz`) | mié 09/12 | 4 h | HU-14 | T3-14 |
| T4-11 | GET /bids/mine (condicional) | D06 (`DanBan1919`) | mié 09/12 | 4 h | HU-15 | T3-12, T1-14, G4-01 |
| T4-15 | Semillas de demo y respaldo de release | D08 (`Tolo-33`) | mié 09/12 | 4 h | HU-14 | T3-15, G4-03 |
| T4-02 | Revisión accesibilidad y consistencia del MVP | D01 (`MariA-00001`) | jue 10/12 | 4 h | HU-14 | T4-01 |
| T4-03 | Vista Vue ganador / sin ganador | D02 (`1509-d`) | jue 10/12 | 4 h | HU-14 | T4-01, T4-09 |
| T4-05 | Vista Mis ofertas (condicional) | D03 (`1509-d`) | jue 10/12 | 4 h | HU-15 | T3-12, T4-11 |
| T4-12 | GET catálogo con filtros (condicional) | D06 (`DanBan1919`) | jue 10/12 | 4 h | HU-16 | T2-13, G4-01 |
| T4-13 | GET historial ADMIN (condicional) | D07 (`yuleyker`) | jue 10/12 | 4 h | HU-17 | T2-12, G4-01 |
| T4-04 | Integración y revisión visual del recorrido P0 | D02 (`1509-d`) | vie 11/12 | 4 h | HU-14 | T4-03, T4-02 |
| T4-06 | Filtros de catálogo Vue (condicional) | D03 (`1509-d`) | vie 11/12 | 4 h | HU-16 | T2-07, T4-12 |
| T4-07 | Historial de moderación UI (condicional) | D04 (`1509-d`) | vie 11/12 | 4 h | HU-17 | T2-06, T4-13 |
| T4-10 | Endurecimiento de API y errores P0 | D05 (`yeisonglvz`) | vie 11/12 | 4 h | HU-14 | T4-09, T2-12 |
| T4-14 | GET usuarios ADMIN (condicional) | D07 (`yuleyker`) | vie 11/12 | 4 h | HU-18 | T1-14, G4-01 |
| T4-16 | Auditar integridad winner y fechas | D08 (`Tolo-33`) | vie 11/12 | 4 h | HU-14 | T4-15, T4-09 |
| T4-17 | E2E y regresión de cierre, pujas y seguridad | D09 (`Tolo-33`) | vie 11/12 | 4 h | HU-14 | T4-09, T4-03, T4-15, T3-18 |
| T4-08 | Listado de usuarios ADMIN (condicional) | D04 (`1509-d`) | dom 13/12 | 4 h | HU-18 | T4-14, T1-14 |
| T4-18 | Entorno demostración, README y smoke test | D09 (`Tolo-33`) | dom 13/12 | 4 h | HU-14 | T4-17, T4-16, T4-04 |

## Reglas de avance

- Una tarea pasa a *En progreso* solo cuando todas sus predecesoras tienen evidencia demostrada.
- Una historia pasa a *Done* cuando frontend, backend, base de datos, pruebas y validación del PO están integrados.
- Las tareas condicionales del Sprint 4 solo se hacen si PO-01 confirma que no quedan defectos bloqueantes del MVP.
