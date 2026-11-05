# Plan Scrum para cuatro semanas y 12 integrantes

**Proyecto:** SubastaWeb · **ID:** SCRUM-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

## 1. Organización y responsabilidad colectiva

**PO-01** tiene responsabilidad final sobre priorización, decisiones de producto y aceptación; **PO-02** realiza análisis, criterios y soporte de validación, sin constituirse en un segundo decisor formal. **SM-01** facilita Scrum y gestiona impedimentos; los desarrolladores D01-D09 forman un solo equipo multidisciplinario, no cuatro departamentos separados.

| Persona | Especialidad | Responsabilidad principal | Colaboración |
| --- | --- | --- | --- |
| PO-01 | Producto | Objetivos, backlog, alcance, aprobación | Revisión de incrementos |
| PO-02 | Producto | Análisis, criterios BDD, validación UX | Escenarios de prueba |
| SM-01 | Scrum | Planificación, Daily, Review, Retro y bloqueos | Seguimiento de dependencias |
| D01 | Diseño | Prototipos y responsive | Pruebas de accesibilidad |
| D02 | Frontend | Autenticación y resultados Vue | Integración API |
| D03 | Frontend | Moderación, historial y pujas Vue | Pruebas UI |
| D04 | Frontend | Publicación, catálogo, reloj Vue | Accesos y UX |
| D05 | Backend | Spring Security, pujas transaccionales | Pruebas unitarias |
| D06 | Backend | Subastas, moderación e historial | Contrato OpenAPI |
| D07 | Backend | Catálogo, cierre, tiempo de servidor | Integración |
| D08 | BD | Esquema, migraciones, índices | Concurrencia |
| D09 | BD | Semillas, QA SQL, integración y release | Pruebas E2E |

## 2. Capacidad propuesta (pendiente de confirmación)

| Métrica | Por sprint | Total 4 sprints |
| --- | --- | --- |
| Disponibilidad bruta: 9 x 12 h | 108 h | 432 h |
| Tareas con dueño: 9 x 2 x 4 h | 72 h | 288 h |
| Reserva para revisión, pruebas, integración y eventos | 36 h | 144 h |
| Esfuerzo PO/SM propuesto, no sumado a desarrollo | 6 h | 24 h |

**Riesgo de factibilidad:** Esta capacidad es una hipótesis. Si el equipo dispone de menos de 12 h brutas por persona, PO-01 deberá reducir alcance y prioridad Debería antes de comprometer las historias. No declarar viable por mera distribución nominal.

## 3. Objetivo, historias y criterio de cierre de cada sprint

| Sprint | Objetivo | HU Debe | HU condicional | Criterio de salida |
| --- | --- | --- | --- | --- |
| 1 | Registro, acceso y solicitud privada | HU-01, HU-02, HU-03, HU-04 | Ninguna | Usuario puede registrarse, iniciar sesión y enviar una solicitud que NO aparece públicamente. |
| 2 | Moderación administrativa y publicación | HU-05, HU-06, HU-07, HU-08, HU-09 | Ninguna | Administrador aprueba/rechaza; el usuario corrige; solamente lo aprobado aparece en catálogo. |
| 3 | Motor de ofertas y cierre automático | HU-10, HU-11, HU-12, HU-13 | Ninguna | Pujas con concurrencia resuelta y cierre idempotente al vencimiento, con ganador persistido. |
| 4 | Resultados y estabilización | HU-14 | HU-15, HU-16, HU-17, HU-18 | Recorrido completo demostrado, resultado correcto, pruebas P0 y documentación reproducible. |
## 4. Eventos Scrum propuestos (sprint semanal)

| Evento | Momento | Duración sugerida | Salida |
| --- | --- | --- | --- |
| Planning | Lunes | 45 min | Sprint Goal, historias y capacidad |
| Daily | Cada día | 10 min | Bloqueos visibles |
| Refinamiento | Miércoles | 20 min | Próximas HU preparadas |
| Review | Viernes | 30 min | Incremento demo validado por PO-01 |
| Retrospectiva | Viernes | 25 min | Una acción de mejora medible |
## 5. Definition of Ready / Definition of Done

**Ready sugerida:** historia con actor, valor, reglas, criterios testables, datos mínimos, dependencias y estimación revisada por Developers. No es una regla obligatoria de Scrum.

**Done acordada:** implementación integrada en rama principal de desarrollo; revisión de código; pruebas positivas y negativas; contrato API y migración revisados; no hay errores P0; documentación afectada actualizada; despliegue de demostración y validación del PO.

## 6. Riesgos y mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
| --- | --- | --- | --- |
| Java/Spring Boot desconocido | Media | Alta | Taller en Sprint 1 y contratos pequeños |
| Pujas simultáneas | Alta | Alta | Bloqueo de fila de subasta + 20 solicitudes paralelas |
| Diseño/backend no sincronizados | Media | Alta | DTO y prototipo acordados antes de código |
| Falta de tiempo | Alta | Alta | Prioridad P0, extras Debería cancelables |
| Infraestructura no disponible | Media | Media | Entorno local y backup de demo |
| Integración al final | Media | Alta | Día 4-5 integración en cada sprint |
| Manejo de datos personales | Media | Alta | Evitar información sensible en API y logs |
## 7. Seguimiento

Tablero: Pendiente / Preparada / En progreso / Bloqueada / En revisión / Done. Cada tarea muestra predecesoras por ID y evidencia concreta; las funciones de producto se registran como G1-01 etc. Las historias opcionales HU-15..HU-18 no se comprometen si los tests P0 requieren la reserva.

**Documento de ejecución obligatorio:** 04_Historias_y_Tareas/01_PLAN_VIGENTE_v4_Tareas_Detalladas_y_Dependencias.docx.
