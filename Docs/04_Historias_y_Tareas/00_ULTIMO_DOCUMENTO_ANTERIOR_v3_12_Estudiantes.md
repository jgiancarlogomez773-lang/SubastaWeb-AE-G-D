# SubastaWeb — Plan de tareas, requisitos y dependencias

**Versión:** 3.0 · **Equipo:** 12 estudiantes (PO-01, PO-02, SM-01, D01–D09) · **Stack:** Vue 3 + Spring Boot + PostgreSQL

## Alcance de la planificación

18 historias (HU-01…HU-18), 72 tareas técnicas y 12 actividades PO/SM. Dos tareas de 4 h por desarrollador y por sprint, con 4 h adicionales por persona reservadas; disponibilidad de 12 h semanales por developer es un supuesto pendiente de confirmación.

## Roles

| Área | Responsable(s) |
|---|---|
| Producto | PO-01 y PO-02 |
| Scrum | SM-01 |
| Diseño | D01 |
| Frontend Vue | D02–D04 |
| Backend Spring Boot | D05–D07 |
| PostgreSQL | D08–D09 |

## Historias de usuario

| Sprint | ID | Historia | Prioridad | RF |
|---|---|---|---|---|
| 1 | HU-01 | Registro de usuario | Debe | RF-AUT-01 |
| 1 | HU-02 | Inicio y cierre de sesión | Debe | RF-AUT-02, RF-AUT-03 |
| 1 | HU-03 | Permisos por rol y propiedad | Debe | RF-SEG-01 |
| 1 | HU-04 | Solicitud de publicación | Debe | RF-SUB-01 |
| 2 | HU-05 | Consultar mis solicitudes | Debe | RF-SUB-02 |
| 2 | HU-06 | Editar y reenviar rechazada | Debe | RF-SUB-03, RF-SUB-04 |
| 2 | HU-07 | Cola de revisión ADMIN | Debe | RF-MOD-01 |
| 2 | HU-08 | Aprobar o rechazar | Debe | RF-MOD-02, RF-MOD-03, RF-MOD-04 |
| 2 | HU-09 | Catálogo y detalle público | Debe | RF-CAT-01, RF-CAT-02 |
| 3 | HU-10 | Registrar puja | Debe | RF-PUJ-01, RF-PUJ-02, RF-PUJ-03 |
| 3 | HU-11 | Precio actual e historial | Debe | RF-PUJ-04 |
| 3 | HU-12 | Temporizador | Debe | RF-TMP-01 |
| 3 | HU-13 | Cierre y ganador interno | Debe | RF-CIE-01, RF-CIE-02 |
| 4 | HU-14 | Resultados finales | Debe | RF-RES-01 |
| 4 | HU-15 | Mis ofertas | Debería | RF-CLI-01 |
| 4 | HU-16 | Filtros de catálogo | Debería | RF-CAT-03 |
| 4 | HU-17 | Historial de moderación | Debería | RF-MOD-05 |
| 4 | HU-18 | Administración de usuarios | Debería | RF-ADM-01 |

## Sprint 1 — Fundamentos + solicitud privada

**Objetivo:** El visitante se registra, inicia sesión y solicita publicar un artículo que NO es público.

**Gestión de producto y Scrum**

| Código | Área | Responsable | RF / HU | Requisitos previos | Para comenzar | Criterio de finalización |
|---|---|---|---|---|---|---|
| G1-01 | Producto | PO-01 | RF-AUT-01 / RF-SUB-01 | Inicio | Confirmar alcance de HU-01 a HU-04 con interesados. | Acta de decisiones: rol USER, campos mínimos, precio, duración, imagen y rechazo de duplicados. |
| G1-02 | Producto | PO-02 | HU-01…HU-04 | G1-01 | Reglas de PO-01 disponibles. | Gherkin HU-01…HU-04, textos de errores y contratos mínimos revisados. |
| G1-03 | Scrum | SM-01 | RNF-MAN-01 | G1-01 | Equipo y repositorio designados. | Sprint Goal, tablero, PR obligatoria y Definition of Done visibles al equipo. |

**Tareas técnicas**

### T1-01 — Prototipos registro, login y nueva subasta

- **Área / Responsable:** Diseño / D01. **Historia:** HU-01, HU-02, HU-04. **Requisito:** RF-AUT-01, RF-AUT-02, RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G1-01, G1-02.
- **Para comenzar:** Campos, roles y restricciones acordados por producto.
- **Acción:** Dibujar tres flujos de pantalla móvil/escritorio, accesos y estados vacíos.
- **Finalizada cuando:** PO-02 valida campos, navegación y mensajes; enlace a prototipo publicado.

### T1-02 — Sistema visual y estados de validación

- **Área / Responsable:** Diseño / D01. **Historia:** HU-03, HU-04. **Requisito:** RF-SEG-01, RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T1-01, G1-03.
- **Para comenzar:** Wireframes y Definition of Done aprobados.
- **Acción:** Definir componentes, navegación USER/ADMIN y diseño de errores 401/403/422.
- **Finalizada cuando:** Guía breve de colores, formularios, accesibilidad y estados aprobada por frontend.

### T1-03 — Iniciar Vue 3 + Vite y navegación

- **Área / Responsable:** Frontend / D02. **Historia:** HU-01, HU-02. **Requisito:** RF-AUT-01, RF-AUT-02.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T1-01, G1-03.
- **Para comenzar:** Repo configurado y prototipos disponibles.
- **Acción:** Crear proyecto Vue, Vue Router, rutas públicas y layout reutilizable.
- **Finalizada cuando:** npm install/build ejecuta sin errores; registro y login tienen rutas navegables.

### T1-04 — Integrar registro con Spring Boot

- **Área / Responsable:** Frontend / D02. **Historia:** HU-01. **Requisito:** RF-AUT-01.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T1-03, T1-09.
- **Para comenzar:** POST /auth/register comprobado y formulario Vue creado.
- **Acción:** Validar campos, consumir API, mostrar 409/422 y bloquear doble envío.
- **Finalizada cuando:** Registro real persiste USER y duplicado se informa sin crear cuenta.

### T1-05 — Pantalla login, logout y guardas Vue

- **Área / Responsable:** Frontend / D03. **Historia:** HU-02, HU-03. **Requisito:** RF-AUT-02, RF-SEG-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T1-02, T1-03.
- **Para comenzar:** Componentes visuales y rutas base terminadas.
- **Acción:** Desarrollar login con estados cargando/error y guard de ruta sobre sesión.
- **Finalizada cuando:** Flujo con mock respeta acceso público/privado y conserva criterio de navegación.

### T1-06 — Conectar sesión real y verificar roles

- **Área / Responsable:** Frontend / D03. **Historia:** HU-02, HU-03. **Requisito:** RF-AUT-02, RF-AUT-03, RF-SEG-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T1-05, T1-10, T1-14.
- **Para comenzar:** API login/logout/me y restricciones disponibles.
- **Acción:** Conectar cookie de sesión con proxy /api y probar salida de sesión.
- **Finalizada cuando:** USER entra a sección privada; logout invalida acceso; 401/403 se presentan correctamente.

### T1-07 — Formulario de solicitud e imagen

- **Área / Responsable:** Frontend / D04. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T1-02, T1-11.
- **Para comenzar:** Campos y contrato POST /auctions documentados.
- **Acción:** Crear formulario con categorías, monto, duración y previsualización JPG/PNG/WebP.
- **Finalizada cuando:** Valida requerido, tamaño <= 3 MB y duración sin enviar datos falsos.

### T1-08 — Conectar solicitud a API y confirmación

- **Área / Responsable:** Frontend / D04. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T1-07, T1-12, T1-13.
- **Para comenzar:** POST subastas, carga de imagen y categorías disponibles.
- **Acción:** Enviar multipart, presentar estado pendiente y limpiar formulario tras éxito.
- **Finalizada cuando:** Solicitud aparece como PENDIENTE_REVISION y NO se muestra al público.

### T1-09 — POST /auth/register + hash contraseña

- **Área / Responsable:** Backend / D05. **Historia:** HU-01. **Requisito:** RF-AUT-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T1-15, T1-11, G1-01.
- **Para comenzar:** Migración users aplicada y reglas de registro aprobadas.
- **Acción:** Crear DTO validado, unicidad insensible a mayúsculas, hash y respuesta 201/409/422.
- **Finalizada cuando:** Pruebas alta válida, correo duplicado y rol ADMIN no autoprovisionable.

### T1-10 — Login, logout y usuario actual con Spring Security

- **Área / Responsable:** Backend / D05. **Historia:** HU-02. **Requisito:** RF-AUT-02, RF-AUT-03.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T1-09, T1-17.
- **Para comenzar:** Usuario de prueba y configuración inicial de seguridad disponibles.
- **Acción:** Implementar sesión con cookie HttpOnly y endpoints de autenticación.
- **Finalizada cuando:** 200 en login válido, 401 inválido, logout invalida sesión; mecanismo CSRF probado.

### T1-11 — Inicializar Spring Boot y contrato de solicitud

- **Área / Responsable:** Backend / D06. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** G1-01, T1-16.
- **Para comenzar:** Esquema auctions y reglas de subastas disponibles.
- **Acción:** Crear proyecto Java/Spring Boot con Maven, dependencias Web, Security, Data JPA y Validation; publicar DTO y contrato POST de subasta.
- **Finalizada cuando:** Build Maven ejecuta y contratos de API 201/401/422 están acordados con frontend.

### T1-12 — Guardar solicitud privada con imagen

- **Área / Responsable:** Backend / D06. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T1-11, T1-17.
- **Para comenzar:** DTO y repositorio auctions operativos; política de imágenes definida.
- **Acción:** Implementar POST /auctions, validar tipo/tamaño y guardar nombre seguro de archivo.
- **Finalizada cuando:** Transacción crea solo PENDIENTE_REVISION; imagen inválida no deja archivo huérfano.

### T1-13 — GET de categorías disponibles

- **Área / Responsable:** Backend / D07. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T1-15, T1-11.
- **Para comenzar:** Categorías sembradas y contrato publicado.
- **Acción:** Exponer GET /categories y manejar errores globales en Spring Web.
- **Finalizada cuando:** Lista de categorías real, JSON consistente y prueba de caso vacío.

### T1-14 — Autorización USER/ADMIN y propiedad

- **Área / Responsable:** Backend / D07. **Historia:** HU-03. **Requisito:** RF-SEG-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T1-10, T1-12, G1-03.
- **Para comenzar:** Sesiones, solicitudes y matriz de permisos finalizadas.
- **Acción:** Configurar reglas Spring Security en rutas y validar propietario en operaciones.
- **Finalizada cuando:** Pruebas negativas 401/403/404 no exponen datos privados.

### T1-15 — Migración users y categories

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-01, HU-04. **Requisito:** RF-AUT-01, RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G1-01.
- **Para comenzar:** Campos de registro y categorías definidos.
- **Acción:** Crear SQL versionado, PK, UNIQUE lower(email), FK y tablas mínimas.
- **Finalizada cuando:** Migración en PostgreSQL limpio y rollback documentado.

### T1-16 — Migración auctions + estados iniciales

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-04. **Requisito:** RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T1-15.
- **Para comenzar:** users/categories creadas; contrato de solicitud conocido.
- **Acción:** Definir auctions con owner_id, category_id, precio, duración, estado y version.
- **Finalizada cuando:** FK, CHECK y valores válidos; estado predeterminado PENDIENTE_REVISION.

### T1-17 — Semillas y conexión PostgreSQL/Spring

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-01, HU-04. **Requisito:** RF-AUT-01, RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T1-15, T1-16.
- **Para comenzar:** Esquema inicial aplicado.
- **Acción:** Configurar perfil local, seed categorías y ADMIN exclusivo por script/entorno.
- **Finalizada cuando:** Spring se conecta a Postgres; un USER y un ADMIN de pruebas son reproducibles.

### T1-18 — Prueba integrada y respaldo de datos del Sprint 1

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-01…HU-04. **Requisito:** RF-AUT-01, RF-SEG-01, RF-SUB-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T1-04, T1-06, T1-08, T1-14, T1-17.
- **Para comenzar:** Frontend y backend conectados con esquema y cuentas de prueba.
- **Acción:** Ejecutar prueba registro→login→publicación pendiente; verificar integridad y migraciones.
- **Finalizada cuando:** Evidencia de extremo a extremo y reporte de defectos bloqueantes corregidos.

**Compuerta de salida:** HITO-S1: registro y login reales; solicitud validada en PostgreSQL con estado PENDIENTE_REVISION; permisos 401/403 verificados; prueba extremo a extremo evidenciada.


## Sprint 2 — Moderación y catálogo

**Objetivo:** El ADMIN rechaza/aprueba, el usuario corrige y la subasta aprobada aparece públicamente.

**Gestión de producto y Scrum**

| Código | Área | Responsable | RF / HU | Requisitos previos | Para comenzar | Criterio de finalización |
|---|---|---|---|---|---|---|
| G2-01 | Producto | PO-01 | RF-SUB-04 / RF-MOD-02 | HITO-S1 | Sprint 1 aceptado y solicitudes pendientes utilizables. | Reglas de rechazo, edición, reenvío, fecha de aprobación y privacidad aprobadas. |
| G2-02 | Producto | PO-02 | HU-05…HU-09 | G2-01 | Reglas de moderación confirmadas. | Criterios Gherkin y textos ADMIN/USER entregados a diseño y desarrollo. |
| G2-03 | Scrum | SM-01 | RNF-MAN-01 | HITO-S1 | Evidencia de Sprint Review S1 revisada. | Bloqueos abiertos asignados; tablero S2 y compromisos reajustados. |

**Tareas técnicas**

### T2-01 — Prototipo mis subastas y bandeja ADMIN

- **Área / Responsable:** Diseño / D01. **Historia:** HU-05, HU-06, HU-07, HU-08. **Requisito:** RF-SUB-02, RF-SUB-03, RF-MOD-01, RF-MOD-02.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G2-01, G2-02.
- **Para comenzar:** Estados y acciones de moderación aprobados por PO.
- **Acción:** Diseñar lista propia, vista de revisión, aprobar/rechazar y motivo.
- **Finalizada cuando:** Prototipos cubren pendiente, rechazada, aprobada, error y permisos.

### T2-02 — Prototipo de catálogo y detalle público

- **Área / Responsable:** Diseño / D01. **Historia:** HU-09. **Requisito:** RF-CAT-01, RF-CAT-02.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T2-01.
- **Para comenzar:** Estados de aprobación y criterios de privacidad conocidos.
- **Acción:** Diseñar tarjetas, estado, precio, detalle y mensajes si no hay resultados.
- **Finalizada cuando:** Se ven solo subastas aprobadas y se define información que NO es pública.

### T2-03 — Vista Mis solicitudes con estados

- **Área / Responsable:** Frontend / D02. **Historia:** HU-05. **Requisito:** RF-SUB-02.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T2-01, T2-09.
- **Para comenzar:** GET /auctions/mine comprobado y wireframe publicado.
- **Acción:** Listar solicitudes propias con estado y motivo de rechazo cuando aplica.
- **Finalizada cuando:** USER solo ve sus subastas y estado; visitante recibe pantalla de acceso.

### T2-04 — Edición y reenvío desde Vue

- **Área / Responsable:** Frontend / D02. **Historia:** HU-06. **Requisito:** RF-SUB-03, RF-SUB-04.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T2-03, T2-10.
- **Para comenzar:** Endpoint editar/reenviar y lista propia estables.
- **Acción:** Formular edición de rechazadas y botón reenviar.
- **Finalizada cuando:** Solo estados permitidos habilitan edición; ronda cambia tras reenvío.

### T2-05 — Bandeja de pendientes ADMIN

- **Área / Responsable:** Frontend / D03. **Historia:** HU-07. **Requisito:** RF-MOD-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T2-01, T2-11.
- **Para comenzar:** API /admin/auctions/pending comprobada.
- **Acción:** Construir bandeja con vista preliminar, datos y estado de revisión.
- **Finalizada cuando:** Solo ADMIN navega; registros no se mezclan con catálogo público.

### T2-06 — Acciones aprobar/rechazar con confirmación

- **Área / Responsable:** Frontend / D03. **Historia:** HU-08. **Requisito:** RF-MOD-02, RF-MOD-03, RF-MOD-04.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T2-05, T2-12.
- **Para comenzar:** Backend rechazar/aprobar valida versión y motivo.
- **Acción:** Formulario de causa, confirmaciones y tratamiento 409 de revisión obsoleta.
- **Finalizada cuando:** Una decisión válida actualiza fila; doble decisión devuelve error visible.

### T2-07 — Catálogo público Vue

- **Área / Responsable:** Frontend / D04. **Historia:** HU-09. **Requisito:** RF-CAT-01.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T2-02, T2-13.
- **Para comenzar:** GET catálogo activo y diseño de tarjetas disponibles.
- **Acción:** Construir catálogo paginado simple con estados vacíos.
- **Finalizada cuando:** Visitante ve solo ACTIVA/FINALIZADA, nunca pendientes o rechazadas.

### T2-08 — Detalle público de artículo

- **Área / Responsable:** Frontend / D04. **Historia:** HU-09. **Requisito:** RF-CAT-02.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T2-07, T2-14.
- **Para comenzar:** Consulta detalle protegida por visibilidad y catálogo renderizado.
- **Acción:** Construir vista detalle con imagen, precio, descripción y fechas.
- **Finalizada cuando:** No expone correo vendedor ni solicitudes no aprobadas.

### T2-09 — GET /auctions/mine por sesión

- **Área / Responsable:** Backend / D05. **Historia:** HU-05. **Requisito:** RF-SUB-02.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T1-12, T1-14.
- **Para comenzar:** Identidad y propiedad ya verificadas en Spring.
- **Acción:** Consultar solicitudes propias y devolver estado y motivo cuando proceda.
- **Finalizada cuando:** Tests: dueño ve su dato; otro USER no puede accederlo.

### T2-10 — PATCH + reenvío de rechazadas

- **Área / Responsable:** Backend / D05. **Historia:** HU-06. **Requisito:** RF-SUB-03, RF-SUB-04.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T2-09, T2-16.
- **Para comenzar:** Versión y ronda modeladas en PostgreSQL.
- **Acción:** Validar edición por dueño, incrementar versión, reenviar con cambios.
- **Finalizada cuando:** ACTIVA no se modifica; reenvío sin cambios o versión desfasada da 409.

### T2-11 — GET cola de revisión ADMIN

- **Área / Responsable:** Backend / D06. **Historia:** HU-07. **Requisito:** RF-MOD-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T1-14, T2-15.
- **Para comenzar:** Auditoría y permisos de rol disponibles.
- **Acción:** Exponer pendientes con filtros simples y versión actual.
- **Finalizada cuando:** Solo ADMIN recibe registros pendientes; USER obtiene 403.

### T2-12 — Aprobar/rechazar con transacción y auditoría

- **Área / Responsable:** Backend / D06. **Historia:** HU-08. **Requisito:** RF-MOD-02, RF-MOD-03, RF-MOD-04.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T2-11, T2-16, T2-17.
- **Para comenzar:** Modelo de rondas y tests de versionado listos.
- **Acción:** Guardar moderación y estado de subasta en transacción; iniciar reloj al aprobar.
- **Finalizada cuando:** Una decisión por versión; rechazo con causa; doble decisión 409.

### T2-13 — GET catálogo exclusivamente público

- **Área / Responsable:** Backend / D07. **Historia:** HU-09. **Requisito:** RF-CAT-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T2-15.
- **Para comenzar:** Estados públicos y datos de ejemplo creados.
- **Acción:** Implementar consulta paginada filtrando ACTIVA y FINALIZADA.
- **Finalizada cuando:** Prueba automatizada confirma que pendientes/rechazadas nunca aparecen.

### T2-14 — GET detalle protegido por estado

- **Área / Responsable:** Backend / D07. **Historia:** HU-09. **Requisito:** RF-CAT-02.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T2-13.
- **Para comenzar:** Catálogo público filtrado disponible.
- **Acción:** Exponer GET /auctions/{id} con DTO público sin PII.
- **Finalizada cuando:** Detalle de privada retorna 404; dato de vendedor sensible no aparece.

### T2-15 — Migración moderation_decisions y versión

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-07, HU-08. **Requisito:** RF-MOD-01, RF-MOD-02.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G2-01, T1-16.
- **Para comenzar:** S1 cuenta con auctions versionada y reglas S2 aceptadas.
- **Acción:** Crear moderations: reviewer, auction, ronda, versión, decisión, motivo y timestamp.
- **Finalizada cuando:** FK/índices y auditoría append-only verificables en SQL.

### T2-16 — Integridad rondas y estados en BD

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-06, HU-08. **Requisito:** RF-SUB-04, RF-MOD-04.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T2-15.
- **Para comenzar:** Tabla auditoría creada.
- **Acción:** Agregar campos de ronda/versión e índices para decisiones vigentes.
- **Finalizada cuando:** No se aceptan versiones inválidas; migración repetible en entorno de pruebas.

### T2-17 — Semillas y pruebas de edición concurrente

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-08. **Requisito:** RF-MOD-02, RF-MOD-04.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T2-15, T2-16.
- **Para comenzar:** Modelo de auditoría/rondas estable.
- **Acción:** Crear solicitudes de prueba para rechazo, reenvío y aprobación conflictiva.
- **Finalizada cuando:** Scripts reproducibles demuestran rechazo de doble decisión.

### T2-18 — Prueba de flujo de moderación completo

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-05…HU-09. **Requisito:** RF-MOD-02, RF-CAT-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T2-04, T2-06, T2-08, T2-12, T2-17.
- **Para comenzar:** Vistas y endpoints S2 integrados.
- **Acción:** Probar rechazar→editar→reenviar→aprobar→catálogo; inspeccionar registros.
- **Finalizada cuando:** Evidencia HU05-HU09 y cero filtraciones de pendientes al catálogo.

**Compuerta de salida:** HITO-S2: flujo rechazo→edición→reenvío→aprobación→catálogo probado; versión/auditoría consistentes; privadas invisibles para visitante.


## Sprint 3 — Ofertas y cierre

**Objetivo:** Dos compradores compiten sin inconsistencias y el servidor cierra al vencer el plazo.

**Gestión de producto y Scrum**

| Código | Área | Responsable | RF / HU | Requisitos previos | Para comenzar | Criterio de finalización |
|---|---|---|---|---|---|---|
| G3-01 | Producto | PO-01 | RF-PUJ-01 / RF-CIE-01 | HITO-S2 | Catálogo público y aprobación S2 aceptados. | Documento reglas: primera oferta, incremento COP, no auto-oferta, cierre UTC, sin ganador. |
| G3-02 | Producto | PO-02 | HU-10…HU-13 | G3-01 | Reglas de negocio de ofertas confirmadas. | Casos de aceptación incluyendo simultaneidad, tardía y doble ejecución del cierre. |
| G3-03 | Scrum | SM-01 | RNF-MAN-01 | HITO-S2 | Registro de defectos S2 y capacidad de 9 dev confirmados. | Secuencia prioriza bloqueo transaccional y automatización de cierre. |

**Tareas técnicas**

### T3-01 — Prototipos puja, precio e historial

- **Área / Responsable:** Diseño / D01. **Historia:** HU-10, HU-11, HU-12. **Requisito:** RF-PUJ-01, RF-PUJ-04, RF-TMP-01.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G3-01, G3-02.
- **Para comenzar:** Reglas de monto, moneda y errores de puja aprobadas.
- **Acción:** Diseñar input COP, incrementos permitidos, líder, historial y estados vacíos.
- **Finalizada cuando:** PO-02 aprueba mensajes para 409, puja propia y tiempo vencido.

### T3-02 — Estados visuales del temporizador y cierre

- **Área / Responsable:** Diseño / D01. **Historia:** HU-12, HU-13. **Requisito:** RF-TMP-01, RF-CIE-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T3-01.
- **Para comenzar:** Prototipo de pujas disponible.
- **Acción:** Diseñar conteo regresivo, indicador finalizada y estado sin ofertas.
- **Finalizada cuando:** Diseño cubre vencimiento, carga, error de red y móvil.

### T3-03 — Formulario de pujas con estados

- **Área / Responsable:** Frontend / D02. **Historia:** HU-10. **Requisito:** RF-PUJ-01, RF-PUJ-02.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T3-01, T3-09.
- **Para comenzar:** Contrato de endpoint y reglas de pujas públicas.
- **Acción:** Implementar validaciones locales y bloqueos de botón por estado/propiedad.
- **Finalizada cuando:** Se muestran monto mínimo e indicación de cierre sin sustituir validación backend.

### T3-04 — Conectar POST pujas + conflictos 409

- **Área / Responsable:** Frontend / D02. **Historia:** HU-10. **Requisito:** RF-PUJ-01, RF-PUJ-03.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T3-03, T3-10.
- **Para comenzar:** API transaccional disponible y componente Vue completado.
- **Acción:** Enviar oferta e interpretar errores servidor con relectura de líder.
- **Finalizada cuando:** Oferta válida aparece; oferta perdedora concurrente recibe mensaje sin duplicado.

### T3-05 — Precio actual e historial público

- **Área / Responsable:** Frontend / D03. **Historia:** HU-11. **Requisito:** RF-PUJ-04.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T3-01, T3-12.
- **Para comenzar:** API historial de ofertas disponible.
- **Acción:** Mostrar líder y lista con alias anónimo/importe/fecha.
- **Finalizada cuando:** Sin correos ni identidad privada; primera puja actualiza vista.

### T3-06 — Refrescar ofertas tras puja

- **Área / Responsable:** Frontend / D03. **Historia:** HU-11. **Requisito:** RF-PUJ-04.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T3-04, T3-05.
- **Para comenzar:** Oferta integrada e historial mostrado.
- **Acción:** Actualizar líder después de enviar y con consulta periódica moderada.
- **Finalizada cuando:** Dos navegadores ven precio convergente tras recargar/esperar sondeo.

### T3-07 — Cuenta regresiva basada en hora servidor

- **Área / Responsable:** Frontend / D04. **Historia:** HU-12. **Requisito:** RF-TMP-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T3-02, T3-13.
- **Para comenzar:** ends_at y server_time disponibles en detalle/API.
- **Acción:** Mostrar tiempo restante sin fiarse del reloj local, redondeo correcto.
- **Finalizada cuando:** Contador llega a cero; refresco mantiene consistencia razonable.

### T3-08 — Estado finalizada y bloqueo visual

- **Área / Responsable:** Frontend / D04. **Historia:** HU-12, HU-13. **Requisito:** RF-TMP-01, RF-CIE-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T3-07, T3-14.
- **Para comenzar:** Reloj y job cierre funcionando.
- **Acción:** Retirar oferta en UI tras vencer; cargar estado final.
- **Finalizada cuando:** El botón queda deshabilitado al vencer; backend también rechaza la puja.

### T3-09 — Validar oferta ascendente y reglas

- **Área / Responsable:** Backend / D05. **Historia:** HU-10. **Requisito:** RF-PUJ-01, RF-PUJ-02.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T3-15, G3-01.
- **Para comenzar:** Tabla bids creada y política de incremento aprobada.
- **Acción:** Implementar DTO POST /auctions/{id}/bids con rango, propietario y fecha.
- **Finalizada cuando:** Primera oferta >= inicial; siguiente respeta mínimo; inválidas no persisten.

### T3-10 — Transacción de pujas simultáneas

- **Área / Responsable:** Backend / D05. **Historia:** HU-10. **Requisito:** RF-PUJ-03.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T3-09, T3-16, T3-17.
- **Para comenzar:** Validaciones y estrategia de bloqueo definidas en BD.
- **Acción:** Bloquear auction row / serializar dentro de transacción, recalcular líder, guardar o 409.
- **Finalizada cuando:** Test concurrente no permite dos ofertas incompatibles ni pérdida de ganador.

### T3-11 — GET precio líder para un artículo

- **Área / Responsable:** Backend / D06. **Historia:** HU-11. **Requisito:** RF-PUJ-04.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T3-15.
- **Para comenzar:** Bids existe y DTO público de subasta acordado.
- **Acción:** Crear servicio para precio base si no hay pujas y líder si existen.
- **Finalizada cuando:** Consulta devuelve monto actual correcto sin datos personales.

### T3-12 — GET historial paginado sin datos privados

- **Área / Responsable:** Backend / D06. **Historia:** HU-11. **Requisito:** RF-PUJ-04.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T3-11.
- **Para comenzar:** Consulta líder implementada.
- **Acción:** Exponer historial reciente con alias y fecha, DTO sin email.
- **Finalizada cuando:** Pruebas confirman orden, paginación y ausencia de PII.

### T3-13 — Fecha final y hora oficial en API

- **Área / Responsable:** Backend / D07. **Historia:** HU-12. **Requisito:** RF-TMP-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T2-12.
- **Para comenzar:** Aprobación define inicio y fin desde servidor.
- **Acción:** Incluir starts_at, ends_at y server_time en detalle o endpoint de reloj.
- **Finalizada cuando:** Cuenta regresiva UI puede sincronizarse sin depender del reloj del cliente.

### T3-14 — Job cierre idempotente + ganador

- **Área / Responsable:** Backend / D07. **Historia:** HU-13. **Requisito:** RF-CIE-01, RF-CIE-02.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T3-13, T3-15, T3-16.
- **Para comenzar:** Bids, índices y fechas de servidor disponibles.
- **Acción:** @Scheduled cierra ACTIVA vencida, guarda winning_bid_id o NULL y closed_at.
- **Finalizada cuando:** Dos ejecuciones consecutivas producen mismo resultado; no se aceptan tardías.

### T3-15 — Migración bids y referencia de ganador

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-10, HU-13. **Requisito:** RF-PUJ-01, RF-CIE-02.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G3-01, T2-16.
- **Para comenzar:** Esquema de auctions final y campos de resultado definidos.
- **Acción:** Crear bids, FK a users/auctions y winning_bid_id nullable asociado.
- **Finalizada cuando:** Integridad de bids y winner; migración limpia reproducible.

### T3-16 — Índices de ofertas y estrategia de bloqueo

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-10. **Requisito:** RF-PUJ-03.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T3-15.
- **Para comenzar:** Migración bids aplicada.
- **Acción:** Índices (auction_id, amount, created_at) y aislamiento documentado.
- **Finalizada cuando:** Explicar transacción serializadora; prueba de consulta usa índices acordados.

### T3-17 — Fixtures y prueba de concurrencia sobre PostgreSQL

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-10. **Requisito:** RF-PUJ-03.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T3-16.
- **Para comenzar:** Índices y transacción diseñada.
- **Acción:** Cargar 2 USER, varias ofertas y script que ejecute >=20 solicitudes paralelas.
- **Finalizada cuando:** Registro de pujas aceptadas/rechazadas auditable sin importes contradictorios.

### T3-18 — Pruebas integradas de pujas y cierre

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-10…HU-13. **Requisito:** RF-PUJ-03, RF-CIE-01, RF-CIE-02.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T3-04, T3-08, T3-10, T3-14, T3-17.
- **Para comenzar:** Frontend, motor transaccional y job integrados.
- **Acción:** Ejecutar concurrencia, vencimiento, auto-puja y doble cierre; documentar resultados.
- **Finalizada cuando:** Pasa matriz de escenarios y quedan evidencias SQL/API sin fallos P0.

**Compuerta de salida:** HITO-S3: pujas simultáneas probadas en PostgreSQL real; propietario/tardía bloqueadas; job idempotente registra ganador o NULL.


## Sprint 4 — Resultados, estabilización y entrega

**Objetivo:** El resultado final se ve en Vue; se prueba y despliega un MVP reproducible.

**Gestión de producto y Scrum**

| Código | Área | Responsable | RF / HU | Requisitos previos | Para comenzar | Criterio de finalización |
|---|---|---|---|---|---|---|
| G4-01 | Producto | PO-01 | RF-RES-01 / HU-15…HU-18 | HITO-S3 | Motor de puja y cierre S3 aceptados. | HU-14 P0 bloqueante y HU-15…18 condicionadas; criterio de liberación firmado. |
| G4-02 | Producto | PO-02 | HU-14 | G4-01 | Prioridades P0/P1 claras. | Guion demostración: publicación→rechazo→reenvío→aprobación→oferta→resultado. |
| G4-03 | Scrum | SM-01 | RNF-OPA-01 | HITO-S3 | Defectos y riesgos S3 conocidos. | Plan daily de bloqueos, ventana de congelación y lista de verificaciones. |

**Tareas técnicas**

### T4-01 — Diseño pantalla resultado y estado sin ofertas

- **Área / Responsable:** Diseño / D01. **Historia:** HU-14. **Requisito:** RF-RES-01.
- **Prioridad / Día / Horas:** Debe / 1 / 4 h.
- **Predecesoras:** G4-01, G4-02.
- **Para comenzar:** PO define qué información del ganador puede ser pública.
- **Acción:** Diseñar resultado final y mensaje sin pujas; proteger privacidad.
- **Finalizada cuando:** Diseño validado con alias público, monto o estado sin adjudicación.

### T4-02 — Revisión accesibilidad y consistencia del MVP

- **Área / Responsable:** Diseño / D01. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-USA-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T4-01.
- **Para comenzar:** Pantallas reales y pantalla resultado disponibles.
- **Acción:** Realizar checklist teclado, textos, error, responsive 360 px y accesibilidad visual.
- **Finalizada cuando:** Registrar correcciones y validar que navegación principal es usable.

### T4-03 — Vista Vue ganador / sin ganador

- **Área / Responsable:** Frontend / D02. **Historia:** HU-14. **Requisito:** RF-RES-01.
- **Prioridad / Día / Horas:** Debe / 3 / 4 h.
- **Predecesoras:** T4-01, T4-09.
- **Para comenzar:** API de resultados y diseño final listos.
- **Acción:** Desarrollar vista en detalle de subasta finalizada.
- **Finalizada cuando:** Ganador muestra alias/importe; si no hubo pujas indica Sin ofertas.

### T4-04 — Integración y revisión visual del recorrido P0

- **Área / Responsable:** Frontend / D02. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-USA-01.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T4-03, T4-02.
- **Para comenzar:** Pantalla resultados y checklist UX disponibles.
- **Acción:** Verificar todos los pasos del recorrido en móvil y desktop y corregir UI P0.
- **Finalizada cuando:** Flujo de demo completo usable sin datos sensibles ni bloqueos.

### T4-05 — Vista Mis ofertas (condicional)

- **Área / Responsable:** Frontend / D03. **Historia:** HU-15. **Requisito:** RF-CLI-01.
- **Prioridad / Día / Horas:** Debería / 3 / 4 h.
- **Predecesoras:** T3-12, T4-11.
- **Para comenzar:** API personal publicada; PO aprueba trabajo P1 tras priorizar P0.
- **Acción:** Mostrar únicamente ofertas propias por fecha.
- **Finalizada cuando:** Ninguna oferta ajena visible; si P0 falla, se reemplaza por corrección P0.

### T4-06 — Filtros de catálogo Vue (condicional)

- **Área / Responsable:** Frontend / D03. **Historia:** HU-16. **Requisito:** RF-CAT-03.
- **Prioridad / Día / Horas:** Debería / 4 / 4 h.
- **Predecesoras:** T2-07, T4-12.
- **Para comenzar:** Backend filtros y catálogo P0 operativos; capacidad P1 autorizada.
- **Acción:** Añadir filtro categoría/estado y búsqueda simple.
- **Finalizada cuando:** Filtra solo visibles; sustituible por regresión si hay bloqueos críticos.

### T4-07 — Historial de moderación UI (condicional)

- **Área / Responsable:** Frontend / D04. **Historia:** HU-17. **Requisito:** RF-MOD-05.
- **Prioridad / Día / Horas:** Debería / 4 / 4 h.
- **Predecesoras:** T2-06, T4-13.
- **Para comenzar:** API auditoría ADMIN disponible y capacidad P1 aprobada.
- **Acción:** Mostrar decisiones de una subasta solo a ADMIN.
- **Finalizada cuando:** Rondas y causas visibles sin opción de edición; sustituible por QA P0.

### T4-08 — Listado de usuarios ADMIN (condicional)

- **Área / Responsable:** Frontend / D04. **Historia:** HU-18. **Requisito:** RF-ADM-01.
- **Prioridad / Día / Horas:** Debería / 5 / 4 h.
- **Predecesoras:** T4-14, T1-14.
- **Para comenzar:** API usuarios protegida y sprint sin defectos P0.
- **Acción:** Crear pantalla paginada de usuarios sin hashes.
- **Finalizada cuando:** 403 USER y datos limitados; sustituible por documentación/QA final.

### T4-09 — API resultado final y anonimización

- **Área / Responsable:** Backend / D05. **Historia:** HU-14. **Requisito:** RF-RES-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T3-14.
- **Para comenzar:** Job cierre S3 escribe ganador o NULL.
- **Acción:** Exponer GET /auctions/{id}/result con estado, alias y puja ganadora.
- **Finalizada cuando:** Retorna Sin ofertas si no hay bids; no retorna email/hash.

### T4-10 — Endurecimiento de API y errores P0

- **Área / Responsable:** Backend / D05. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-SEG-02.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T4-09, T2-12.
- **Para comenzar:** Endpoints P0 disponibles; PO aprobó semántica de errores.
- **Acción:** Revisar autorización/errores y completar contrato API REST.
- **Finalizada cuando:** Tests 401/403/404/409/422 pasan en rutas protegidas.

### T4-11 — GET /bids/mine (condicional)

- **Área / Responsable:** Backend / D06. **Historia:** HU-15. **Requisito:** RF-CLI-01.
- **Prioridad / Día / Horas:** Debería / 2 / 4 h.
- **Predecesoras:** T3-12, T1-14, G4-01.
- **Para comenzar:** Ofertas persistidas y regla de propiedad disponible.
- **Acción:** Endpoint filtra por usuario de sesión sin ID de terceros.
- **Finalizada cuando:** Prueba no revela ofertas ajenas; reemplazable por bugs P0 si se pospone.

### T4-12 — GET catálogo con filtros (condicional)

- **Área / Responsable:** Backend / D06. **Historia:** HU-16. **Requisito:** RF-CAT-03.
- **Prioridad / Día / Horas:** Debería / 3 / 4 h.
- **Predecesoras:** T2-13, G4-01.
- **Para comenzar:** Catálogo P0 y filtros negociados por PO.
- **Acción:** Añadir búsqueda por título, categoría y estado sin ver privados.
- **Finalizada cuando:** Resultados paginados y jamás exponen pendientes/rechazadas.

### T4-13 — GET historial ADMIN (condicional)

- **Área / Responsable:** Backend / D07. **Historia:** HU-17. **Requisito:** RF-MOD-05.
- **Prioridad / Día / Horas:** Debería / 3 / 4 h.
- **Predecesoras:** T2-12, G4-01.
- **Para comenzar:** Auditoría de moderación persistida en S2.
- **Acción:** Exponer decisiones por subasta y proteger rol ADMIN.
- **Finalizada cuando:** Historia ordenada y 403 USER; si bloqueos P0, sustituir por cierre/QA.

### T4-14 — GET usuarios ADMIN (condicional)

- **Área / Responsable:** Backend / D07. **Historia:** HU-18. **Requisito:** RF-ADM-01.
- **Prioridad / Día / Horas:** Debería / 4 / 4 h.
- **Predecesoras:** T1-14, G4-01.
- **Para comenzar:** Roles Spring Security disponibles.
- **Acción:** Listar usuarios paginados sin credenciales ni hashes.
- **Finalizada cuando:** 403 a USER, respuesta limitada; sustituible por soporte P0.

### T4-15 — Semillas de demo y respaldo de release

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-OPA-01.
- **Prioridad / Día / Horas:** Debe / 2 / 4 h.
- **Predecesoras:** T3-15, G4-03.
- **Para comenzar:** Modelo S3 y checklist de release disponibles.
- **Acción:** Preparar migración desde vacío, cuentas de prueba separadas y backup/restore.
- **Finalizada cuando:** Una nueva BD se inicializa y contiene casos sin ofertas y con ganador.

### T4-16 — Auditar integridad winner y fechas

- **Área / Responsable:** Base de datos / D08. **Historia:** HU-14. **Requisito:** RF-CIE-02, RNF-FIA-02.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T4-15, T4-09.
- **Para comenzar:** API resultado y BD de ensayo disponibles.
- **Acción:** Verificar winner pertenece a subasta, no hay ofertas post ends_at y job es idempotente.
- **Finalizada cuando:** Checklist SQL aprobado con cero inconsistencias P0.

### T4-17 — E2E y regresión de cierre, pujas y seguridad

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-FIA-01, RNF-FIA-02.
- **Prioridad / Día / Horas:** Debe / 4 / 4 h.
- **Predecesoras:** T4-09, T4-03, T4-15, T3-18.
- **Para comenzar:** MVP completo y datos de prueba listos.
- **Acción:** Ejecutar matriz funcional, simultaneidad, doble cierre y acceso por rol.
- **Finalizada cuando:** Sin bugs P0 y evidencias de cada RF Debe con responsable.

### T4-18 — Entorno demostración, README y smoke test

- **Área / Responsable:** Base de datos / D09. **Historia:** HU-14. **Requisito:** RF-RES-01, RNF-OPA-01.
- **Prioridad / Día / Horas:** Debe / 5 / 4 h.
- **Predecesoras:** T4-17, T4-16, T4-04.
- **Para comenzar:** Regresión e integridad P0 aprobadas.
- **Acción:** Levantar frontend, backend y BD con instrucciones; ejecutar demo de extremo a extremo.
- **Finalizada cuando:** Otro estudiante reproduce instalación y el PO acepta incremento final.

**Compuerta de salida:** CIERRE: ruta completa demostrable sin defectos P0; README, evidencias y acta de PO. Extras HU-15…18 no bloquean la aceptación.

## Consideraciones finales

Vue 3 interpreta la referencia “Veo”; confirmar nombre en la primera reunión. No se comprometen HU-15…HU-18 hasta que HU-01…HU-14 estén integradas. No se incorporaron desarrolladores adicionales. El SRS/PRD/TRD antiguo debe versionarse antes de aprobar este plan como línea base.