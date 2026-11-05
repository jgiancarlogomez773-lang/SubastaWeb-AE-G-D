# Plan detallado de tareas, requisitos y dependencias

**Proyecto:** SubastaWeb · **ID:** PLAN-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. Documento de ejecución vigente; incluye las tareas reales del plan anterior y las amplía con controles verificables.

## 1. Lectura obligatoria antes de asignar trabajo

**Equipo:** PO-01 (único PO responsable), PO-02 (analista/validador), SM-01 (facilitador Scrum), D01 Diseño, D02-D04 Frontend Vue, D05-D07 Backend Java Spring Boot y D08-D09 Base de datos PostgreSQL. Los 9 especialistas forman un solo equipo de desarrollo con responsabilidad conjunta de calidad y pruebas.

**Interpretación de dependencias:** `T2-10 <- T2-09` significa que el entregable de T2-09 se debe demostrar y revisar antes de iniciar T2-10. `G2-01` es aprobación de producto, no una tarea de programación. `HITO-S1` es el criterio de cierre del primer sprint. Las dependencias son prerrequisitos reales, no solo un orden de lectura.

**Capacidad propuesta (por confirmar):** 9 Developers x 12 h/semana = 108 h brutas. Se comprometen 72 h de tareas concretas por sprint (dos tareas de 4 h por persona); 36 h quedan para eventos, coordinación, QA adicional, code review y contingencias. PO y Scrum Master no se contabilizan como desarrolladores. Story points no se convierten directamente a horas.

**Regla de avance:** Una tarea se mueve a "En progreso" únicamente después de demostrar la evidencia de TODAS sus predecesoras. Una historia pasa a "Done" solo cuando frontend, backend, base de datos, pruebas y validación de PO están integrados.

## 2. Matriz de responsables y carga semanal

| Integrante | Área principal | Tareas/sprint | Horas técnicas por sprint | Responsabilidad secundaria |
| --- | --- | --- | --- | --- |
| PO-01 | Producto | 1 actividad G | 2 h propuestas | Aceptación y prioridades |
| PO-02 | Producto | 1 actividad G | 2 h propuestas | Criterios Gherkin y pruebas de usuario |
| SM-01 | Scrum | 1 actividad G | 2 h propuestas | Desbloqueo y calidad del sprint |
| D01 | Diseño | 2 | 8 h | Revisión responsive y UX |
| D02 | Frontend | 2 | 8 h | Integración de APIs |
| D03 | Frontend | 2 | 8 h | Pruebas UI y accesos |
| D04 | Frontend | 2 | 8 h | Catálogo y pujas |
| D05 | Backend | 2 | 8 h | Autenticación y concurrencia |
| D06 | Backend | 2 | 8 h | Moderación e historial |
| D07 | Backend | 2 | 8 h | Cierre y resultados |
| D08 | Base de datos | 2 | 8 h | Migraciones e índices |
| D09 | Base de datos | 2 | 8 h | Fixtures, integridad y pruebas E2E |

## 3. SPRINT 1 — Registro, acceso y solicitud privada

**Sprint Goal:** Usuario puede registrarse, iniciar sesión y enviar una solicitud que NO aparece públicamente.

**Esfuerzo:** 18 tareas x 4 h = 72 h; capacidad bruta presupuesta 108 h, reserva 36 h. **Día 5:** integración, QA, Review y Retrospectiva.

###  A. Actividades habilitantes del Product Owner y Scrum Master

**G1-01 — Definir reglas de negocio base** · Producto · PO-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** Inicio del proyecto (sin requisito previo).  
- **Condición de entrada:** Confirmar alcance de HU-01 a HU-04 con interesados.  
- **Salida verificable:** Acta de decisiones: rol USER, campos mínimos, precio, duración, imagen y rechazo de duplicados.  
- **Habilita:** T1-01, T1-09, T1-11, T1-15, G1-02, G1-03.

**G1-02 — Revisar criterios de aceptación y datos** · Producto · PO-02 · Día 1 · 2 h.  
- **Prerequisito por ID:** G1-01.  
- **Condición de entrada:** Reglas de PO-01 disponibles.  
- **Salida verificable:** Gherkin HU-01…HU-04, textos de errores y contratos mínimos revisados.  
- **Habilita:** T1-01.

**G1-03 — Acordar objetivo y calidad del sprint** · Scrum · SM-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** G1-01.  
- **Condición de entrada:** Equipo y repositorio designados.  
- **Salida verificable:** Sprint Goal, tablero, PR obligatoria y Definition of Done visibles al equipo.  
- **Habilita:** T1-02, T1-03, T1-14.

### B. Índice operativo de tareas técnicas y dependencias

| Código | Área | Estudiante | Día | HU | Predecesoras obligatorias |
| --- | --- | --- | --- | --- | --- |
| T1-01 | Diseño | D01 | D1 | HU-01, HU-02, HU-04 | G1-01, G1-02 |
| T1-02 | Diseño | D01 | D2 | HU-03, HU-04 | T1-01, G1-03 |
| T1-03 | Frontend | D02 | D2 | HU-01, HU-02 | T1-01, G1-03 |
| T1-04 | Frontend | D02 | D4 | HU-01 | T1-03, T1-09 |
| T1-05 | Frontend | D03 | D3 | HU-02, HU-03 | T1-02, T1-03 |
| T1-06 | Frontend | D03 | D5 | HU-02, HU-03 | T1-05, T1-10, T1-14 |
| T1-07 | Frontend | D04 | D3 | HU-04 | T1-02, T1-11 |
| T1-08 | Frontend | D04 | D5 | HU-04 | T1-07, T1-12, T1-13 |
| T1-09 | Backend | D05 | D3 | HU-01 | T1-15, T1-11, G1-01 |
| T1-10 | Backend | D05 | D4 | HU-02 | T1-09, T1-17 |
| T1-11 | Backend | D06 | D2 | HU-04 | G1-01, T1-16 |
| T1-12 | Backend | D06 | D4 | HU-04 | T1-11, T1-17 |
| T1-13 | Backend | D07 | D3 | HU-04 | T1-15, T1-11 |
| T1-14 | Backend | D07 | D5 | HU-03 | T1-10, T1-12, G1-03 |
| T1-15 | Base de datos | D08 | D1 | HU-01, HU-04 | G1-01 |
| T1-16 | Base de datos | D08 | D2 | HU-04 | T1-15 |
| T1-17 | Base de datos | D09 | D3 | HU-01, HU-04 | T1-15, T1-16 |
| T1-18 | Base de datos | D09 | D5 | HU-01…HU-04 | T1-04, T1-06, T1-08, T1-14, T1-17 |

### C. Fichas ejecutables de todas las tareas

#### T1-01 — Prototipos registro, login y nueva subasta

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S1 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01, HU-02, HU-04 · **Requisitos RF:** RF-AUT-01, RF-AUT-02, RF-SUB-01

**Prerequisitos por ID:** G1-01, G1-02.

**Qué tiene que estar terminado para comenzar:** Campos, roles y restricciones acordados por producto.

**Acciones concretas:**

1. Verificar los entregables de G1-01, G1-02 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Dibujar tres flujos de pantalla móvil/escritorio, accesos y estados vacíos.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** PO-02 valida campos, navegación y mensajes; enlace a prototipo publicado.

**Trabajo que desbloquea:** T1-02, T1-03.

#### T1-02 — Sistema visual y estados de validación

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S1 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-03, HU-04 · **Requisitos RF:** RF-SEG-01, RF-SUB-01

**Prerequisitos por ID:** T1-01, G1-03.

**Qué tiene que estar terminado para comenzar:** Wireframes y Definition of Done aprobados.

**Acciones concretas:**

1. Verificar los entregables de T1-01, G1-03 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Definir componentes, navegación USER/ADMIN y diseño de errores 401/403/422.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Guía breve de colores, formularios, accesibilidad y estados aprobada por frontend.

**Trabajo que desbloquea:** T1-05, T1-07.

#### T1-03 — Iniciar Vue 3 + Vite y navegación

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S1 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01, HU-02 · **Requisitos RF:** RF-AUT-01, RF-AUT-02

**Prerequisitos por ID:** T1-01, G1-03.

**Qué tiene que estar terminado para comenzar:** Repo configurado y prototipos disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-01, G1-03 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear proyecto Vue, Vue Router, rutas públicas y layout reutilizable.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** npm install/build ejecuta sin errores; registro y login tienen rutas navegables.

**Trabajo que desbloquea:** T1-04, T1-05.

#### T1-04 — Integrar registro con Spring Boot

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S1 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01 · **Requisitos RF:** RF-AUT-01

**Prerequisitos por ID:** T1-03, T1-09.

**Qué tiene que estar terminado para comenzar:** POST /auth/register comprobado y formulario Vue creado.

**Acciones concretas:**

1. Verificar los entregables de T1-03, T1-09 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Validar campos, consumir API, mostrar 409/422 y bloquear doble envío.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Registro real persiste USER y duplicado se informa sin crear cuenta.

**Trabajo que desbloquea:** T1-18.

#### T1-05 — Pantalla login, logout y guardas Vue

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S1 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-02, HU-03 · **Requisitos RF:** RF-AUT-02, RF-SEG-01

**Prerequisitos por ID:** T1-02, T1-03.

**Qué tiene que estar terminado para comenzar:** Componentes visuales y rutas base terminadas.

**Acciones concretas:**

1. Verificar los entregables de T1-02, T1-03 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Desarrollar login con estados cargando/error y guard de ruta sobre sesión.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Flujo con mock respeta acceso público/privado y conserva criterio de navegación.

**Trabajo que desbloquea:** T1-06.

#### T1-06 — Conectar sesión real y verificar roles

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S1 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-02, HU-03 · **Requisitos RF:** RF-AUT-02, RF-AUT-03, RF-SEG-01

**Prerequisitos por ID:** T1-05, T1-10, T1-14.

**Qué tiene que estar terminado para comenzar:** API login/logout/me y restricciones disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-05, T1-10, T1-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Conectar cookie de sesión con proxy /api y probar salida de sesión.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** USER entra a sección privada; logout invalida acceso; 401/403 se presentan correctamente.

**Trabajo que desbloquea:** T1-18.

#### T1-07 — Formulario de solicitud e imagen

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S1 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** T1-02, T1-11.

**Qué tiene que estar terminado para comenzar:** Campos y contrato POST /auctions documentados.

**Acciones concretas:**

1. Verificar los entregables de T1-02, T1-11 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear formulario con categorías, monto, duración y previsualización JPG/PNG/WebP.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Valida requerido, tamaño <= 3 MB y duración sin enviar datos falsos.

**Trabajo que desbloquea:** T1-08.

#### T1-08 — Conectar solicitud a API y confirmación

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S1 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** T1-07, T1-12, T1-13.

**Qué tiene que estar terminado para comenzar:** POST subastas, carga de imagen y categorías disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-07, T1-12, T1-13 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Enviar multipart, presentar estado pendiente y limpiar formulario tras éxito.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Solicitud aparece como PENDIENTE_REVISION y NO se muestra al público.

**Trabajo que desbloquea:** T1-18.

#### T1-09 — POST /auth/register + hash contraseña

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S1 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01 · **Requisitos RF:** RF-AUT-01

**Prerequisitos por ID:** T1-15, T1-11, G1-01.

**Qué tiene que estar terminado para comenzar:** Migración users aplicada y reglas de registro aprobadas.

**Acciones concretas:**

1. Verificar los entregables de T1-15, T1-11, G1-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear DTO validado, unicidad insensible a mayúsculas, hash y respuesta 201/409/422.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Pruebas alta válida, correo duplicado y rol ADMIN no autoprovisionable.

**Trabajo que desbloquea:** T1-04, T1-10.

#### T1-10 — Login, logout y usuario actual con Spring Security

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S1 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-02 · **Requisitos RF:** RF-AUT-02, RF-AUT-03

**Prerequisitos por ID:** T1-09, T1-17.

**Qué tiene que estar terminado para comenzar:** Usuario de prueba y configuración inicial de seguridad disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-09, T1-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Implementar sesión con cookie HttpOnly y endpoints de autenticación.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** 200 en login válido, 401 inválido, logout invalida sesión; mecanismo CSRF probado.

**Trabajo que desbloquea:** T1-06, T1-14.

#### T1-11 — Inicializar Spring Boot y contrato de solicitud

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S1 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** G1-01, T1-16.

**Qué tiene que estar terminado para comenzar:** Esquema auctions y reglas de subastas disponibles.

**Acciones concretas:**

1. Verificar los entregables de G1-01, T1-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear proyecto Java/Spring Boot con Maven, dependencias Web, Security, Data JPA y Validation; publicar DTO y contrato POST de subasta.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Build Maven ejecuta y contratos de API 201/401/422 están acordados con frontend.

**Trabajo que desbloquea:** T1-07, T1-09, T1-12, T1-13.

#### T1-12 — Guardar solicitud privada con imagen

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S1 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** T1-11, T1-17.

**Qué tiene que estar terminado para comenzar:** DTO y repositorio auctions operativos; política de imágenes definida.

**Acciones concretas:**

1. Verificar los entregables de T1-11, T1-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Implementar POST /auctions, validar tipo/tamaño y guardar nombre seguro de archivo.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Transacción crea solo PENDIENTE_REVISION; imagen inválida no deja archivo huérfano.

**Trabajo que desbloquea:** T1-08, T1-14, T2-09.

#### T1-13 — GET de categorías disponibles

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S1 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** T1-15, T1-11.

**Qué tiene que estar terminado para comenzar:** Categorías sembradas y contrato publicado.

**Acciones concretas:**

1. Verificar los entregables de T1-15, T1-11 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer GET /categories y manejar errores globales en Spring Web.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Lista de categorías real, JSON consistente y prueba de caso vacío.

**Trabajo que desbloquea:** T1-08.

#### T1-14 — Autorización USER/ADMIN y propiedad

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S1 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-03 · **Requisitos RF:** RF-SEG-01

**Prerequisitos por ID:** T1-10, T1-12, G1-03.

**Qué tiene que estar terminado para comenzar:** Sesiones, solicitudes y matriz de permisos finalizadas.

**Acciones concretas:**

1. Verificar los entregables de T1-10, T1-12, G1-03 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Configurar reglas Spring Security en rutas y validar propietario en operaciones.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Pruebas negativas 401/403/404 no exponen datos privados.

**Trabajo que desbloquea:** T1-06, T1-18, T2-09, T2-11, T4-08, T4-11, T4-14.

#### T1-15 — Migración users y categories

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S1 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01, HU-04 · **Requisitos RF:** RF-AUT-01, RF-SUB-01

**Prerequisitos por ID:** G1-01.

**Qué tiene que estar terminado para comenzar:** Campos de registro y categorías definidos.

**Acciones concretas:**

1. Verificar los entregables de G1-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear SQL versionado, PK, UNIQUE lower(email), FK y tablas mínimas.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Migración en PostgreSQL limpio y rollback documentado.

**Trabajo que desbloquea:** T1-09, T1-13, T1-16, T1-17.

#### T1-16 — Migración auctions + estados iniciales

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S1 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-04 · **Requisitos RF:** RF-SUB-01

**Prerequisitos por ID:** T1-15.

**Qué tiene que estar terminado para comenzar:** users/categories creadas; contrato de solicitud conocido.

**Acciones concretas:**

1. Verificar los entregables de T1-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Definir auctions con owner_id, category_id, precio, duración, estado y version.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** FK, CHECK y valores válidos; estado predeterminado PENDIENTE_REVISION.

**Trabajo que desbloquea:** T1-11, T1-17, T2-15.

#### T1-17 — Semillas y conexión PostgreSQL/Spring

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S1 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01, HU-04 · **Requisitos RF:** RF-AUT-01, RF-SUB-01

**Prerequisitos por ID:** T1-15, T1-16.

**Qué tiene que estar terminado para comenzar:** Esquema inicial aplicado.

**Acciones concretas:**

1. Verificar los entregables de T1-15, T1-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Configurar perfil local, seed categorías y ADMIN exclusivo por script/entorno.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Spring se conecta a Postgres; un USER y un ADMIN de pruebas son reproducibles.

**Trabajo que desbloquea:** T1-10, T1-12, T1-18.

#### T1-18 — Prueba integrada y respaldo de datos del Sprint 1

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S1 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-01…HU-04 · **Requisitos RF:** RF-AUT-01, RF-SEG-01, RF-SUB-01

**Prerequisitos por ID:** T1-04, T1-06, T1-08, T1-14, T1-17.

**Qué tiene que estar terminado para comenzar:** Frontend y backend conectados con esquema y cuentas de prueba.

**Acciones concretas:**

1. Verificar los entregables de T1-04, T1-06, T1-08, T1-14, T1-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Ejecutar prueba registro→login→publicación pendiente; verificar integridad y migraciones.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Evidencia de extremo a extremo y reporte de defectos bloqueantes corregidos.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

### D. Compuerta de salida del Sprint 1

**HITO-S1:** Usuario puede registrarse, iniciar sesión y enviar una solicitud que NO aparece públicamente.

La evidencia incluye captura funcional, caso de prueba o informe reproducible, código integrado en rama compartida y revisión del Product Owner.


## 4. SPRINT 2 — Moderación administrativa y publicación

**Sprint Goal:** Administrador aprueba/rechaza; el usuario corrige; solamente lo aprobado aparece en catálogo.

**Esfuerzo:** 18 tareas x 4 h = 72 h; capacidad bruta presupuesta 108 h, reserva 36 h. **Día 5:** integración, QA, Review y Retrospectiva.

###  A. Actividades habilitantes del Product Owner y Scrum Master

**G2-01 — Validar reglas de moderación** · Producto · PO-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S1.  
- **Condición de entrada:** Sprint 1 aceptado y solicitudes pendientes utilizables.  
- **Salida verificable:** Reglas de rechazo, edición, reenvío, fecha de aprobación y privacidad aprobadas.  
- **Habilita:** T2-01, T2-15, G2-02.

**G2-02 — Afinar criterios HU-05 a HU-09** · Producto · PO-02 · Día 1 · 2 h.  
- **Prerequisito por ID:** G2-01.  
- **Condición de entrada:** Reglas de moderación confirmadas.  
- **Salida verificable:** Criterios Gherkin y textos ADMIN/USER entregados a diseño y desarrollo.  
- **Habilita:** T2-01.

**G2-03 — Revisar capacidad, defectos y objetivo S2** · Scrum · SM-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S1.  
- **Condición de entrada:** Evidencia de Sprint Review S1 revisada.  
- **Salida verificable:** Bloqueos abiertos asignados; tablero S2 y compromisos reajustados.  
- **Habilita:** Criterios de Sprint Review.

### B. Índice operativo de tareas técnicas y dependencias

| Código | Área | Estudiante | Día | HU | Predecesoras obligatorias |
| --- | --- | --- | --- | --- | --- |
| T2-01 | Diseño | D01 | D1 | HU-05, HU-06, HU-07, HU-08 | G2-01, G2-02 |
| T2-02 | Diseño | D01 | D2 | HU-09 | T2-01 |
| T2-03 | Frontend | D02 | D3 | HU-05 | T2-01, T2-09 |
| T2-04 | Frontend | D02 | D5 | HU-06 | T2-03, T2-10 |
| T2-05 | Frontend | D03 | D3 | HU-07 | T2-01, T2-11 |
| T2-06 | Frontend | D03 | D5 | HU-08 | T2-05, T2-12 |
| T2-07 | Frontend | D04 | D4 | HU-09 | T2-02, T2-13 |
| T2-08 | Frontend | D04 | D5 | HU-09 | T2-07, T2-14 |
| T2-09 | Backend | D05 | D2 | HU-05 | T1-12, T1-14 |
| T2-10 | Backend | D05 | D4 | HU-06 | T2-09, T2-16 |
| T2-11 | Backend | D06 | D2 | HU-07 | T1-14, T2-15 |
| T2-12 | Backend | D06 | D4 | HU-08 | T2-11, T2-16, T2-17 |
| T2-13 | Backend | D07 | D3 | HU-09 | T2-15 |
| T2-14 | Backend | D07 | D4 | HU-09 | T2-13 |
| T2-15 | Base de datos | D08 | D1 | HU-07, HU-08 | G2-01, T1-16 |
| T2-16 | Base de datos | D08 | D2 | HU-06, HU-08 | T2-15 |
| T2-17 | Base de datos | D09 | D3 | HU-08 | T2-15, T2-16 |
| T2-18 | Base de datos | D09 | D5 | HU-05…HU-09 | T2-04, T2-06, T2-08, T2-12, T2-17 |

### C. Fichas ejecutables de todas las tareas

#### T2-01 — Prototipo mis subastas y bandeja ADMIN

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S2 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-05, HU-06, HU-07, HU-08 · **Requisitos RF:** RF-SUB-02, RF-SUB-03, RF-MOD-01, RF-MOD-02

**Prerequisitos por ID:** G2-01, G2-02.

**Qué tiene que estar terminado para comenzar:** Estados y acciones de moderación aprobados por PO.

**Acciones concretas:**

1. Verificar los entregables de G2-01, G2-02 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Diseñar lista propia, vista de revisión, aprobar/rechazar y motivo.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Prototipos cubren pendiente, rechazada, aprobada, error y permisos.

**Trabajo que desbloquea:** T2-02, T2-03, T2-05.

#### T2-02 — Prototipo de catálogo y detalle público

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S2 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-09 · **Requisitos RF:** RF-CAT-01, RF-CAT-02

**Prerequisitos por ID:** T2-01.

**Qué tiene que estar terminado para comenzar:** Estados de aprobación y criterios de privacidad conocidos.

**Acciones concretas:**

1. Verificar los entregables de T2-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Diseñar tarjetas, estado, precio, detalle y mensajes si no hay resultados.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Se ven solo subastas aprobadas y se define información que NO es pública.

**Trabajo que desbloquea:** T2-07.

#### T2-03 — Vista Mis solicitudes con estados

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S2 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-05 · **Requisitos RF:** RF-SUB-02

**Prerequisitos por ID:** T2-01, T2-09.

**Qué tiene que estar terminado para comenzar:** GET /auctions/mine comprobado y wireframe publicado.

**Acciones concretas:**

1. Verificar los entregables de T2-01, T2-09 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Listar solicitudes propias con estado y motivo de rechazo cuando aplica.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** USER solo ve sus subastas y estado; visitante recibe pantalla de acceso.

**Trabajo que desbloquea:** T2-04.

#### T2-04 — Edición y reenvío desde Vue

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S2 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-06 · **Requisitos RF:** RF-SUB-03, RF-SUB-04

**Prerequisitos por ID:** T2-03, T2-10.

**Qué tiene que estar terminado para comenzar:** Endpoint editar/reenviar y lista propia estables.

**Acciones concretas:**

1. Verificar los entregables de T2-03, T2-10 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Formular edición de rechazadas y botón reenviar.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Solo estados permitidos habilitan edición; ronda cambia tras reenvío.

**Trabajo que desbloquea:** T2-18.

#### T2-05 — Bandeja de pendientes ADMIN

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S2 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-07 · **Requisitos RF:** RF-MOD-01

**Prerequisitos por ID:** T2-01, T2-11.

**Qué tiene que estar terminado para comenzar:** API /admin/auctions/pending comprobada.

**Acciones concretas:**

1. Verificar los entregables de T2-01, T2-11 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Construir bandeja con vista preliminar, datos y estado de revisión.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Solo ADMIN navega; registros no se mezclan con catálogo público.

**Trabajo que desbloquea:** T2-06.

#### T2-06 — Acciones aprobar/rechazar con confirmación

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S2 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-08 · **Requisitos RF:** RF-MOD-02, RF-MOD-03, RF-MOD-04

**Prerequisitos por ID:** T2-05, T2-12.

**Qué tiene que estar terminado para comenzar:** Backend rechazar/aprobar valida versión y motivo.

**Acciones concretas:**

1. Verificar los entregables de T2-05, T2-12 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Formulario de causa, confirmaciones y tratamiento 409 de revisión obsoleta.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Una decisión válida actualiza fila; doble decisión devuelve error visible.

**Trabajo que desbloquea:** T2-18, T4-07.

#### T2-07 — Catálogo público Vue

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S2 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-09 · **Requisitos RF:** RF-CAT-01

**Prerequisitos por ID:** T2-02, T2-13.

**Qué tiene que estar terminado para comenzar:** GET catálogo activo y diseño de tarjetas disponibles.

**Acciones concretas:**

1. Verificar los entregables de T2-02, T2-13 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Construir catálogo paginado simple con estados vacíos.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Visitante ve solo ACTIVA/FINALIZADA, nunca pendientes o rechazadas.

**Trabajo que desbloquea:** T2-08, T4-06.

#### T2-08 — Detalle público de artículo

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S2 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-09 · **Requisitos RF:** RF-CAT-02

**Prerequisitos por ID:** T2-07, T2-14.

**Qué tiene que estar terminado para comenzar:** Consulta detalle protegida por visibilidad y catálogo renderizado.

**Acciones concretas:**

1. Verificar los entregables de T2-07, T2-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Construir vista detalle con imagen, precio, descripción y fechas.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** No expone correo vendedor ni solicitudes no aprobadas.

**Trabajo que desbloquea:** T2-18.

#### T2-09 — GET /auctions/mine por sesión

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S2 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-05 · **Requisitos RF:** RF-SUB-02

**Prerequisitos por ID:** T1-12, T1-14.

**Qué tiene que estar terminado para comenzar:** Identidad y propiedad ya verificadas en Spring.

**Acciones concretas:**

1. Verificar los entregables de T1-12, T1-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Consultar solicitudes propias y devolver estado y motivo cuando proceda.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Tests: dueño ve su dato; otro USER no puede accederlo.

**Trabajo que desbloquea:** T2-03, T2-10.

#### T2-10 — PATCH + reenvío de rechazadas

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S2 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-06 · **Requisitos RF:** RF-SUB-03, RF-SUB-04

**Prerequisitos por ID:** T2-09, T2-16.

**Qué tiene que estar terminado para comenzar:** Versión y ronda modeladas en PostgreSQL.

**Acciones concretas:**

1. Verificar los entregables de T2-09, T2-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Validar edición por dueño, incrementar versión, reenviar con cambios.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** ACTIVA no se modifica; reenvío sin cambios o versión desfasada da 409.

**Trabajo que desbloquea:** T2-04.

#### T2-11 — GET cola de revisión ADMIN

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S2 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-07 · **Requisitos RF:** RF-MOD-01

**Prerequisitos por ID:** T1-14, T2-15.

**Qué tiene que estar terminado para comenzar:** Auditoría y permisos de rol disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-14, T2-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer pendientes con filtros simples y versión actual.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Solo ADMIN recibe registros pendientes; USER obtiene 403.

**Trabajo que desbloquea:** T2-05, T2-12.

#### T2-12 — Aprobar/rechazar con transacción y auditoría

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S2 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-08 · **Requisitos RF:** RF-MOD-02, RF-MOD-03, RF-MOD-04

**Prerequisitos por ID:** T2-11, T2-16, T2-17.

**Qué tiene que estar terminado para comenzar:** Modelo de rondas y tests de versionado listos.

**Acciones concretas:**

1. Verificar los entregables de T2-11, T2-16, T2-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Guardar moderación y estado de subasta en transacción; iniciar reloj al aprobar.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Una decisión por versión; rechazo con causa; doble decisión 409.

**Trabajo que desbloquea:** T2-06, T2-18, T3-13, T4-10, T4-13.

#### T2-13 — GET catálogo exclusivamente público

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S2 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-09 · **Requisitos RF:** RF-CAT-01

**Prerequisitos por ID:** T2-15.

**Qué tiene que estar terminado para comenzar:** Estados públicos y datos de ejemplo creados.

**Acciones concretas:**

1. Verificar los entregables de T2-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Implementar consulta paginada filtrando ACTIVA y FINALIZADA.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Prueba automatizada confirma que pendientes/rechazadas nunca aparecen.

**Trabajo que desbloquea:** T2-07, T2-14, T4-12.

#### T2-14 — GET detalle protegido por estado

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S2 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-09 · **Requisitos RF:** RF-CAT-02

**Prerequisitos por ID:** T2-13.

**Qué tiene que estar terminado para comenzar:** Catálogo público filtrado disponible.

**Acciones concretas:**

1. Verificar los entregables de T2-13 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer GET /auctions/{id} con DTO público sin PII.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Detalle de privada retorna 404; dato de vendedor sensible no aparece.

**Trabajo que desbloquea:** T2-08.

#### T2-15 — Migración moderation_decisions y versión

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S2 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-07, HU-08 · **Requisitos RF:** RF-MOD-01, RF-MOD-02

**Prerequisitos por ID:** G2-01, T1-16.

**Qué tiene que estar terminado para comenzar:** S1 cuenta con auctions versionada y reglas S2 aceptadas.

**Acciones concretas:**

1. Verificar los entregables de G2-01, T1-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear moderations: reviewer, auction, ronda, versión, decisión, motivo y timestamp.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** FK/índices y auditoría append-only verificables en SQL.

**Trabajo que desbloquea:** T2-11, T2-13, T2-16, T2-17.

#### T2-16 — Integridad rondas y estados en BD

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S2 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-06, HU-08 · **Requisitos RF:** RF-SUB-04, RF-MOD-04

**Prerequisitos por ID:** T2-15.

**Qué tiene que estar terminado para comenzar:** Tabla auditoría creada.

**Acciones concretas:**

1. Verificar los entregables de T2-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Agregar campos de ronda/versión e índices para decisiones vigentes.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** No se aceptan versiones inválidas; migración repetible en entorno de pruebas.

**Trabajo que desbloquea:** T2-10, T2-12, T2-17, T3-15.

#### T2-17 — Semillas y pruebas de edición concurrente

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S2 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-08 · **Requisitos RF:** RF-MOD-02, RF-MOD-04

**Prerequisitos por ID:** T2-15, T2-16.

**Qué tiene que estar terminado para comenzar:** Modelo de auditoría/rondas estable.

**Acciones concretas:**

1. Verificar los entregables de T2-15, T2-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear solicitudes de prueba para rechazo, reenvío y aprobación conflictiva.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Scripts reproducibles demuestran rechazo de doble decisión.

**Trabajo que desbloquea:** T2-12, T2-18.

#### T2-18 — Prueba de flujo de moderación completo

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S2 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-05…HU-09 · **Requisitos RF:** RF-MOD-02, RF-CAT-01

**Prerequisitos por ID:** T2-04, T2-06, T2-08, T2-12, T2-17.

**Qué tiene que estar terminado para comenzar:** Vistas y endpoints S2 integrados.

**Acciones concretas:**

1. Verificar los entregables de T2-04, T2-06, T2-08, T2-12, T2-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Probar rechazar→editar→reenviar→aprobar→catálogo; inspeccionar registros.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Evidencia HU05-HU09 y cero filtraciones de pendientes al catálogo.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

### D. Compuerta de salida del Sprint 2

**HITO-S2:** Administrador aprueba/rechaza; el usuario corrige; solamente lo aprobado aparece en catálogo.

La evidencia incluye captura funcional, caso de prueba o informe reproducible, código integrado en rama compartida y revisión del Product Owner.


## 5. SPRINT 3 — Motor de ofertas y cierre automático

**Sprint Goal:** Pujas con concurrencia resuelta y cierre idempotente al vencimiento, con ganador persistido.

**Esfuerzo:** 18 tareas x 4 h = 72 h; capacidad bruta presupuesta 108 h, reserva 36 h. **Día 5:** integración, QA, Review y Retrospectiva.

###  A. Actividades habilitantes del Product Owner y Scrum Master

**G3-01 — Confirmar reglas de puja y cierre** · Producto · PO-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S2.  
- **Condición de entrada:** Catálogo público y aprobación S2 aceptados.  
- **Salida verificable:** Documento reglas: primera oferta, incremento COP, no auto-oferta, cierre UTC, sin ganador.  
- **Habilita:** T3-01, T3-09, T3-15, G3-02.

**G3-02 — Diseñar escenarios negativos de pujas** · Producto · PO-02 · Día 1 · 2 h.  
- **Prerequisito por ID:** G3-01.  
- **Condición de entrada:** Reglas de negocio de ofertas confirmadas.  
- **Salida verificable:** Casos de aceptación incluyendo simultaneidad, tardía y doble ejecución del cierre.  
- **Habilita:** T3-01.

**G3-03 — Activar Sprint 3 y revisar riesgos** · Scrum · SM-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S2.  
- **Condición de entrada:** Registro de defectos S2 y capacidad de 9 dev confirmados.  
- **Salida verificable:** Secuencia prioriza bloqueo transaccional y automatización de cierre.  
- **Habilita:** Criterios de Sprint Review.

### B. Índice operativo de tareas técnicas y dependencias

| Código | Área | Estudiante | Día | HU | Predecesoras obligatorias |
| --- | --- | --- | --- | --- | --- |
| T3-01 | Diseño | D01 | D1 | HU-10, HU-11, HU-12 | G3-01, G3-02 |
| T3-02 | Diseño | D01 | D2 | HU-12, HU-13 | T3-01 |
| T3-03 | Frontend | D02 | D3 | HU-10 | T3-01, T3-09 |
| T3-04 | Frontend | D02 | D5 | HU-10 | T3-03, T3-10 |
| T3-05 | Frontend | D03 | D4 | HU-11 | T3-01, T3-12 |
| T3-06 | Frontend | D03 | D5 | HU-11 | T3-04, T3-05 |
| T3-07 | Frontend | D04 | D3 | HU-12 | T3-02, T3-13 |
| T3-08 | Frontend | D04 | D5 | HU-12, HU-13 | T3-07, T3-14 |
| T3-09 | Backend | D05 | D3 | HU-10 | T3-15, G3-01 |
| T3-10 | Backend | D05 | D4 | HU-10 | T3-09, T3-16, T3-17 |
| T3-11 | Backend | D06 | D2 | HU-11 | T3-15 |
| T3-12 | Backend | D06 | D3 | HU-11 | T3-11 |
| T3-13 | Backend | D07 | D2 | HU-12 | T2-12 |
| T3-14 | Backend | D07 | D4 | HU-13 | T3-13, T3-15, T3-16 |
| T3-15 | Base de datos | D08 | D1 | HU-10, HU-13 | G3-01, T2-16 |
| T3-16 | Base de datos | D08 | D2 | HU-10 | T3-15 |
| T3-17 | Base de datos | D09 | D3 | HU-10 | T3-16 |
| T3-18 | Base de datos | D09 | D5 | HU-10…HU-13 | T3-04, T3-08, T3-10, T3-14, T3-17 |

### C. Fichas ejecutables de todas las tareas

#### T3-01 — Prototipos puja, precio e historial

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S3 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10, HU-11, HU-12 · **Requisitos RF:** RF-PUJ-01, RF-PUJ-04, RF-TMP-01

**Prerequisitos por ID:** G3-01, G3-02.

**Qué tiene que estar terminado para comenzar:** Reglas de monto, moneda y errores de puja aprobadas.

**Acciones concretas:**

1. Verificar los entregables de G3-01, G3-02 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Diseñar input COP, incrementos permitidos, líder, historial y estados vacíos.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** PO-02 aprueba mensajes para 409, puja propia y tiempo vencido.

**Trabajo que desbloquea:** T3-02, T3-03, T3-05.

#### T3-02 — Estados visuales del temporizador y cierre

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S3 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-12, HU-13 · **Requisitos RF:** RF-TMP-01, RF-CIE-01

**Prerequisitos por ID:** T3-01.

**Qué tiene que estar terminado para comenzar:** Prototipo de pujas disponible.

**Acciones concretas:**

1. Verificar los entregables de T3-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Diseñar conteo regresivo, indicador finalizada y estado sin ofertas.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Diseño cubre vencimiento, carga, error de red y móvil.

**Trabajo que desbloquea:** T3-07.

#### T3-03 — Formulario de pujas con estados

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S3 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-01, RF-PUJ-02

**Prerequisitos por ID:** T3-01, T3-09.

**Qué tiene que estar terminado para comenzar:** Contrato de endpoint y reglas de pujas públicas.

**Acciones concretas:**

1. Verificar los entregables de T3-01, T3-09 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Implementar validaciones locales y bloqueos de botón por estado/propiedad.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Se muestran monto mínimo e indicación de cierre sin sustituir validación backend.

**Trabajo que desbloquea:** T3-04.

#### T3-04 — Conectar POST pujas + conflictos 409

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S3 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-01, RF-PUJ-03

**Prerequisitos por ID:** T3-03, T3-10.

**Qué tiene que estar terminado para comenzar:** API transaccional disponible y componente Vue completado.

**Acciones concretas:**

1. Verificar los entregables de T3-03, T3-10 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Enviar oferta e interpretar errores servidor con relectura de líder.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Oferta válida aparece; oferta perdedora concurrente recibe mensaje sin duplicado.

**Trabajo que desbloquea:** T3-06, T3-18.

#### T3-05 — Precio actual e historial público

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S3 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-11 · **Requisitos RF:** RF-PUJ-04

**Prerequisitos por ID:** T3-01, T3-12.

**Qué tiene que estar terminado para comenzar:** API historial de ofertas disponible.

**Acciones concretas:**

1. Verificar los entregables de T3-01, T3-12 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Mostrar líder y lista con alias anónimo/importe/fecha.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Sin correos ni identidad privada; primera puja actualiza vista.

**Trabajo que desbloquea:** T3-06.

#### T3-06 — Refrescar ofertas tras puja

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S3 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-11 · **Requisitos RF:** RF-PUJ-04

**Prerequisitos por ID:** T3-04, T3-05.

**Qué tiene que estar terminado para comenzar:** Oferta integrada e historial mostrado.

**Acciones concretas:**

1. Verificar los entregables de T3-04, T3-05 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Actualizar líder después de enviar y con consulta periódica moderada.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Dos navegadores ven precio convergente tras recargar/esperar sondeo.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

#### T3-07 — Cuenta regresiva basada en hora servidor

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S3 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-12 · **Requisitos RF:** RF-TMP-01

**Prerequisitos por ID:** T3-02, T3-13.

**Qué tiene que estar terminado para comenzar:** ends_at y server_time disponibles en detalle/API.

**Acciones concretas:**

1. Verificar los entregables de T3-02, T3-13 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Mostrar tiempo restante sin fiarse del reloj local, redondeo correcto.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Contador llega a cero; refresco mantiene consistencia razonable.

**Trabajo que desbloquea:** T3-08.

#### T3-08 — Estado finalizada y bloqueo visual

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S3 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-12, HU-13 · **Requisitos RF:** RF-TMP-01, RF-CIE-01

**Prerequisitos por ID:** T3-07, T3-14.

**Qué tiene que estar terminado para comenzar:** Reloj y job cierre funcionando.

**Acciones concretas:**

1. Verificar los entregables de T3-07, T3-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Retirar oferta en UI tras vencer; cargar estado final.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** El botón queda deshabilitado al vencer; backend también rechaza la puja.

**Trabajo que desbloquea:** T3-18.

#### T3-09 — Validar oferta ascendente y reglas

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S3 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-01, RF-PUJ-02

**Prerequisitos por ID:** T3-15, G3-01.

**Qué tiene que estar terminado para comenzar:** Tabla bids creada y política de incremento aprobada.

**Acciones concretas:**

1. Verificar los entregables de T3-15, G3-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Implementar DTO POST /auctions/{id}/bids con rango, propietario y fecha.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Primera oferta >= inicial; siguiente respeta mínimo; inválidas no persisten.

**Trabajo que desbloquea:** T3-03, T3-10.

#### T3-10 — Transacción de pujas simultáneas

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S3 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-03

**Prerequisitos por ID:** T3-09, T3-16, T3-17.

**Qué tiene que estar terminado para comenzar:** Validaciones y estrategia de bloqueo definidas en BD.

**Acciones concretas:**

1. Verificar los entregables de T3-09, T3-16, T3-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Bloquear auction row / serializar dentro de transacción, recalcular líder, guardar o 409.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Test concurrente no permite dos ofertas incompatibles ni pérdida de ganador.

**Trabajo que desbloquea:** T3-04, T3-18.

#### T3-11 — GET precio líder para un artículo

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S3 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-11 · **Requisitos RF:** RF-PUJ-04

**Prerequisitos por ID:** T3-15.

**Qué tiene que estar terminado para comenzar:** Bids existe y DTO público de subasta acordado.

**Acciones concretas:**

1. Verificar los entregables de T3-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear servicio para precio base si no hay pujas y líder si existen.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Consulta devuelve monto actual correcto sin datos personales.

**Trabajo que desbloquea:** T3-12.

#### T3-12 — GET historial paginado sin datos privados

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S3 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-11 · **Requisitos RF:** RF-PUJ-04

**Prerequisitos por ID:** T3-11.

**Qué tiene que estar terminado para comenzar:** Consulta líder implementada.

**Acciones concretas:**

1. Verificar los entregables de T3-11 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer historial reciente con alias y fecha, DTO sin email.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Pruebas confirman orden, paginación y ausencia de PII.

**Trabajo que desbloquea:** T3-05, T4-05, T4-11.

#### T3-13 — Fecha final y hora oficial en API

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S3 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-12 · **Requisitos RF:** RF-TMP-01

**Prerequisitos por ID:** T2-12.

**Qué tiene que estar terminado para comenzar:** Aprobación define inicio y fin desde servidor.

**Acciones concretas:**

1. Verificar los entregables de T2-12 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Incluir starts_at, ends_at y server_time en detalle o endpoint de reloj.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Cuenta regresiva UI puede sincronizarse sin depender del reloj del cliente.

**Trabajo que desbloquea:** T3-07, T3-14.

#### T3-14 — Job cierre idempotente + ganador

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S3 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-13 · **Requisitos RF:** RF-CIE-01, RF-CIE-02

**Prerequisitos por ID:** T3-13, T3-15, T3-16.

**Qué tiene que estar terminado para comenzar:** Bids, índices y fechas de servidor disponibles.

**Acciones concretas:**

1. Verificar los entregables de T3-13, T3-15, T3-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** @Scheduled cierra ACTIVA vencida, guarda winning_bid_id o NULL y closed_at.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Dos ejecuciones consecutivas producen mismo resultado; no se aceptan tardías.

**Trabajo que desbloquea:** T3-08, T3-18, T4-09.

#### T3-15 — Migración bids y referencia de ganador

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S3 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10, HU-13 · **Requisitos RF:** RF-PUJ-01, RF-CIE-02

**Prerequisitos por ID:** G3-01, T2-16.

**Qué tiene que estar terminado para comenzar:** Esquema de auctions final y campos de resultado definidos.

**Acciones concretas:**

1. Verificar los entregables de G3-01, T2-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear bids, FK a users/auctions y winning_bid_id nullable asociado.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Integridad de bids y winner; migración limpia reproducible.

**Trabajo que desbloquea:** T3-09, T3-11, T3-14, T3-16, T4-15.

#### T3-16 — Índices de ofertas y estrategia de bloqueo

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S3 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-03

**Prerequisitos por ID:** T3-15.

**Qué tiene que estar terminado para comenzar:** Migración bids aplicada.

**Acciones concretas:**

1. Verificar los entregables de T3-15 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Índices (auction_id, amount, created_at) y aislamiento documentado.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Explicar transacción serializadora; prueba de consulta usa índices acordados.

**Trabajo que desbloquea:** T3-10, T3-14, T3-17.

#### T3-17 — Fixtures y prueba de concurrencia sobre PostgreSQL

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S3 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10 · **Requisitos RF:** RF-PUJ-03

**Prerequisitos por ID:** T3-16.

**Qué tiene que estar terminado para comenzar:** Índices y transacción diseñada.

**Acciones concretas:**

1. Verificar los entregables de T3-16 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Cargar 2 USER, varias ofertas y script que ejecute >=20 solicitudes paralelas.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Registro de pujas aceptadas/rechazadas auditable sin importes contradictorios.

**Trabajo que desbloquea:** T3-10, T3-18.

#### T3-18 — Pruebas integradas de pujas y cierre

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S3 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-10…HU-13 · **Requisitos RF:** RF-PUJ-03, RF-CIE-01, RF-CIE-02

**Prerequisitos por ID:** T3-04, T3-08, T3-10, T3-14, T3-17.

**Qué tiene que estar terminado para comenzar:** Frontend, motor transaccional y job integrados.

**Acciones concretas:**

1. Verificar los entregables de T3-04, T3-08, T3-10, T3-14, T3-17 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Ejecutar concurrencia, vencimiento, auto-puja y doble cierre; documentar resultados.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Pasa matriz de escenarios y quedan evidencias SQL/API sin fallos P0.

**Trabajo que desbloquea:** T4-17.

### D. Compuerta de salida del Sprint 3

**HITO-S3:** Pujas con concurrencia resuelta y cierre idempotente al vencimiento, con ganador persistido.

La evidencia incluye captura funcional, caso de prueba o informe reproducible, código integrado en rama compartida y revisión del Product Owner.


## 6. SPRINT 4 — Resultados y estabilización

**Sprint Goal:** Recorrido completo demostrado, resultado correcto, pruebas P0 y documentación reproducible.

**Esfuerzo:** 18 tareas x 4 h = 72 h; capacidad bruta presupuesta 108 h, reserva 36 h. **Día 5:** integración, QA, Review y Retrospectiva.

###  A. Actividades habilitantes del Product Owner y Scrum Master

**G4-01 — Priorizar entrega final y decidir extras** · Producto · PO-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S3.  
- **Condición de entrada:** Motor de puja y cierre S3 aceptados.  
- **Salida verificable:** HU-14 P0 bloqueante y HU-15…18 condicionadas; criterio de liberación firmado.  
- **Habilita:** T4-01, T4-11, T4-12, T4-13, T4-14, G4-02.

**G4-02 — Planificar demo y validación final** · Producto · PO-02 · Día 1 · 2 h.  
- **Prerequisito por ID:** G4-01.  
- **Condición de entrada:** Prioridades P0/P1 claras.  
- **Salida verificable:** Guion demostración: publicación→rechazo→reenvío→aprobación→oferta→resultado.  
- **Habilita:** T4-01.

**G4-03 — Gestionar estabilización y despliegue** · Scrum · SM-01 · Día 1 · 2 h.  
- **Prerequisito por ID:** HITO-S3.  
- **Condición de entrada:** Defectos y riesgos S3 conocidos.  
- **Salida verificable:** Plan daily de bloqueos, ventana de congelación y lista de verificaciones.  
- **Habilita:** T4-15.

### B. Índice operativo de tareas técnicas y dependencias

| Código | Área | Estudiante | Día | HU | Predecesoras obligatorias |
| --- | --- | --- | --- | --- | --- |
| T4-01 | Diseño | D01 | D1 | HU-14 | G4-01, G4-02 |
| T4-02 | Diseño | D01 | D3 | HU-14 | T4-01 |
| T4-03 | Frontend | D02 | D3 | HU-14 | T4-01, T4-09 |
| T4-04 | Frontend | D02 | D4 | HU-14 | T4-03, T4-02 |
| T4-05 | Frontend | D03 | D3 | HU-15 | T3-12, T4-11 |
| T4-06 | Frontend | D03 | D4 | HU-16 | T2-07, T4-12 |
| T4-07 | Frontend | D04 | D4 | HU-17 | T2-06, T4-13 |
| T4-08 | Frontend | D04 | D5 | HU-18 | T4-14, T1-14 |
| T4-09 | Backend | D05 | D2 | HU-14 | T3-14 |
| T4-10 | Backend | D05 | D4 | HU-14 | T4-09, T2-12 |
| T4-11 | Backend | D06 | D2 | HU-15 | T3-12, T1-14, G4-01 |
| T4-12 | Backend | D06 | D3 | HU-16 | T2-13, G4-01 |
| T4-13 | Backend | D07 | D3 | HU-17 | T2-12, G4-01 |
| T4-14 | Backend | D07 | D4 | HU-18 | T1-14, G4-01 |
| T4-15 | Base de datos | D08 | D2 | HU-14 | T3-15, G4-03 |
| T4-16 | Base de datos | D08 | D4 | HU-14 | T4-15, T4-09 |
| T4-17 | Base de datos | D09 | D4 | HU-14 | T4-09, T4-03, T4-15, T3-18 |
| T4-18 | Base de datos | D09 | D5 | HU-14 | T4-17, T4-16, T4-04 |

### C. Fichas ejecutables de todas las tareas

#### T4-01 — Diseño pantalla resultado y estado sin ofertas

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S4 / día 1 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01

**Prerequisitos por ID:** G4-01, G4-02.

**Qué tiene que estar terminado para comenzar:** PO define qué información del ganador puede ser pública.

**Acciones concretas:**

1. Verificar los entregables de G4-01, G4-02 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Diseñar resultado final y mensaje sin pujas; proteger privacidad.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Diseño validado con alias público, monto o estado sin adjudicación.

**Trabajo que desbloquea:** T4-02, T4-03.

#### T4-02 — Revisión accesibilidad y consistencia del MVP

**Área:** Diseño · **Responsable:** D01 · **Sprint/día:** S4 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-USA-01

**Prerequisitos por ID:** T4-01.

**Qué tiene que estar terminado para comenzar:** Pantallas reales y pantalla resultado disponibles.

**Acciones concretas:**

1. Verificar los entregables de T4-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Realizar checklist teclado, textos, error, responsive 360 px y accesibilidad visual.
3. **Verificación y evidencia:** Entregar prototipo legible, estados error/carga/vacío, navegación en móvil y validación visual con PO-02.

**Criterio de salida / Definition of Done específica:** Registrar correcciones y validar que navegación principal es usable.

**Trabajo que desbloquea:** T4-04.

#### T4-03 — Vista Vue ganador / sin ganador

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S4 / día 3 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01

**Prerequisitos por ID:** T4-01, T4-09.

**Qué tiene que estar terminado para comenzar:** API de resultados y diseño final listos.

**Acciones concretas:**

1. Verificar los entregables de T4-01, T4-09 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Desarrollar vista en detalle de subasta finalizada.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Ganador muestra alias/importe; si no hubo pujas indica Sin ofertas.

**Trabajo que desbloquea:** T4-04, T4-17.

#### T4-04 — Integración y revisión visual del recorrido P0

**Área:** Frontend · **Responsable:** D02 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-USA-01

**Prerequisitos por ID:** T4-03, T4-02.

**Qué tiene que estar terminado para comenzar:** Pantalla resultados y checklist UX disponibles.

**Acciones concretas:**

1. Verificar los entregables de T4-03, T4-02 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Verificar todos los pasos del recorrido en móvil y desktop y corregir UI P0.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Flujo de demo completo usable sin datos sensibles ni bloqueos.

**Trabajo que desbloquea:** T4-18.

#### T4-05 — Vista Mis ofertas (condicional)

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S4 / día 3 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-15 · **Requisitos RF:** RF-CLI-01

**Prerequisitos por ID:** T3-12, T4-11.

**Qué tiene que estar terminado para comenzar:** API personal publicada; PO aprueba trabajo P1 tras priorizar P0.

**Acciones concretas:**

1. Verificar los entregables de T3-12, T4-11 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Mostrar únicamente ofertas propias por fecha.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Ninguna oferta ajena visible; si P0 falla, se reemplaza por corrección P0.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-06 — Filtros de catálogo Vue (condicional)

**Área:** Frontend · **Responsable:** D03 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-16 · **Requisitos RF:** RF-CAT-03

**Prerequisitos por ID:** T2-07, T4-12.

**Qué tiene que estar terminado para comenzar:** Backend filtros y catálogo P0 operativos; capacidad P1 autorizada.

**Acciones concretas:**

1. Verificar los entregables de T2-07, T4-12 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Añadir filtro categoría/estado y búsqueda simple.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Filtra solo visibles; sustituible por regresión si hay bloqueos críticos.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-07 — Historial de moderación UI (condicional)

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-17 · **Requisitos RF:** RF-MOD-05

**Prerequisitos por ID:** T2-06, T4-13.

**Qué tiene que estar terminado para comenzar:** API auditoría ADMIN disponible y capacidad P1 aprobada.

**Acciones concretas:**

1. Verificar los entregables de T2-06, T4-13 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Mostrar decisiones de una subasta solo a ADMIN.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** Rondas y causas visibles sin opción de edición; sustituible por QA P0.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-08 — Listado de usuarios ADMIN (condicional)

**Área:** Frontend · **Responsable:** D04 · **Sprint/día:** S4 / día 5 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-18 · **Requisitos RF:** RF-ADM-01

**Prerequisitos por ID:** T4-14, T1-14.

**Qué tiene que estar terminado para comenzar:** API usuarios protegida y sprint sin defectos P0.

**Acciones concretas:**

1. Verificar los entregables de T4-14, T1-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Crear pantalla paginada de usuarios sin hashes.
3. **Verificación y evidencia:** Probar el flujo en navegador con API real o mock contractual; revisar errores, validación, permisos y responsive.

**Criterio de salida / Definition of Done específica:** 403 USER y datos limitados; sustituible por documentación/QA final.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-09 — API resultado final y anonimización

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S4 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01

**Prerequisitos por ID:** T3-14.

**Qué tiene que estar terminado para comenzar:** Job cierre S3 escribe ganador o NULL.

**Acciones concretas:**

1. Verificar los entregables de T3-14 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer GET /auctions/{id}/result con estado, alias y puja ganadora.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Retorna Sin ofertas si no hay bids; no retorna email/hash.

**Trabajo que desbloquea:** T4-03, T4-10, T4-16, T4-17.

#### T4-10 — Endurecimiento de API y errores P0

**Área:** Backend · **Responsable:** D05 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-SEG-02

**Prerequisitos por ID:** T4-09, T2-12.

**Qué tiene que estar terminado para comenzar:** Endpoints P0 disponibles; PO aprobó semántica de errores.

**Acciones concretas:**

1. Verificar los entregables de T4-09, T2-12 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Revisar autorización/errores y completar contrato API REST.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Tests 401/403/404/409/422 pasan en rutas protegidas.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

#### T4-11 — GET /bids/mine (condicional)

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S4 / día 2 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-15 · **Requisitos RF:** RF-CLI-01

**Prerequisitos por ID:** T3-12, T1-14, G4-01.

**Qué tiene que estar terminado para comenzar:** Ofertas persistidas y regla de propiedad disponible.

**Acciones concretas:**

1. Verificar los entregables de T3-12, T1-14, G4-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Endpoint filtra por usuario de sesión sin ID de terceros.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Prueba no revela ofertas ajenas; reemplazable por bugs P0 si se pospone.

**Trabajo que desbloquea:** T4-05.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-12 — GET catálogo con filtros (condicional)

**Área:** Backend · **Responsable:** D06 · **Sprint/día:** S4 / día 3 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-16 · **Requisitos RF:** RF-CAT-03

**Prerequisitos por ID:** T2-13, G4-01.

**Qué tiene que estar terminado para comenzar:** Catálogo P0 y filtros negociados por PO.

**Acciones concretas:**

1. Verificar los entregables de T2-13, G4-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Añadir búsqueda por título, categoría y estado sin ver privados.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Resultados paginados y jamás exponen pendientes/rechazadas.

**Trabajo que desbloquea:** T4-06.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-13 — GET historial ADMIN (condicional)

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S4 / día 3 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-17 · **Requisitos RF:** RF-MOD-05

**Prerequisitos por ID:** T2-12, G4-01.

**Qué tiene que estar terminado para comenzar:** Auditoría de moderación persistida en S2.

**Acciones concretas:**

1. Verificar los entregables de T2-12, G4-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Exponer decisiones por subasta y proteger rol ADMIN.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** Historia ordenada y 403 USER; si bloqueos P0, sustituir por cierre/QA.

**Trabajo que desbloquea:** T4-07.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-14 — GET usuarios ADMIN (condicional)

**Área:** Backend · **Responsable:** D07 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debería

**Historia vinculada:** HU-18 · **Requisitos RF:** RF-ADM-01

**Prerequisitos por ID:** T1-14, G4-01.

**Qué tiene que estar terminado para comenzar:** Roles Spring Security disponibles.

**Acciones concretas:**

1. Verificar los entregables de T1-14, G4-01 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Listar usuarios paginados sin credenciales ni hashes.
3. **Verificación y evidencia:** Agregar prueba de servicio/controlador para caso positivo y caso negativo; comprobar HTTP, seguridad y transacción cuando aplique.

**Criterio de salida / Definition of Done específica:** 403 a USER, respuesta limitada; sustituible por soporte P0.

**Trabajo que desbloquea:** T4-08.

**Condicional:** Ejecutar únicamente si PO-01 confirma que no quedan defectos bloqueantes del MVP. Si hay riesgo, sustituir por pruebas y correcciones P0.

#### T4-15 — Semillas de demo y respaldo de release

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S4 / día 2 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-OPA-01

**Prerequisitos por ID:** T3-15, G4-03.

**Qué tiene que estar terminado para comenzar:** Modelo S3 y checklist de release disponibles.

**Acciones concretas:**

1. Verificar los entregables de T3-15, G4-03 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Preparar migración desde vacío, cuentas de prueba separadas y backup/restore.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Una nueva BD se inicializa y contiene casos sin ofertas y con ganador.

**Trabajo que desbloquea:** T4-16, T4-17.

#### T4-16 — Auditar integridad winner y fechas

**Área:** Base de datos · **Responsable:** D08 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-CIE-02, RNF-FIA-02

**Prerequisitos por ID:** T4-15, T4-09.

**Qué tiene que estar terminado para comenzar:** API resultado y BD de ensayo disponibles.

**Acciones concretas:**

1. Verificar los entregables de T4-15, T4-09 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Verificar winner pertenece a subasta, no hay ofertas post ends_at y job es idempotente.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Checklist SQL aprobado con cero inconsistencias P0.

**Trabajo que desbloquea:** T4-18.

#### T4-17 — E2E y regresión de cierre, pujas y seguridad

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S4 / día 4 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-FIA-01, RNF-FIA-02

**Prerequisitos por ID:** T4-09, T4-03, T4-15, T3-18.

**Qué tiene que estar terminado para comenzar:** MVP completo y datos de prueba listos.

**Acciones concretas:**

1. Verificar los entregables de T4-09, T4-03, T4-15, T3-18 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Ejecutar matriz funcional, simultaneidad, doble cierre y acceso por rol.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Sin bugs P0 y evidencias de cada RF Debe con responsable.

**Trabajo que desbloquea:** T4-18.

#### T4-18 — Entorno demostración, README y smoke test

**Área:** Base de datos · **Responsable:** D09 · **Sprint/día:** S4 / día 5 · **Estimación:** 4 h · **Prioridad:** Debe

**Historia vinculada:** HU-14 · **Requisitos RF:** RF-RES-01, RNF-OPA-01

**Prerequisitos por ID:** T4-17, T4-16, T4-04.

**Qué tiene que estar terminado para comenzar:** Regresión e integridad P0 aprobadas.

**Acciones concretas:**

1. Verificar los entregables de T4-17, T4-16, T4-04 y dejar evidencia en la tarjeta de la tarea.
2. **Implementar:** Levantar frontend, backend y BD con instrucciones; ejecutar demo de extremo a extremo.
3. **Verificación y evidencia:** Ejecutar migración o consulta de control en PostgreSQL, comprobar llaves/restricciones y guardar resultado de prueba.

**Criterio de salida / Definition of Done específica:** Otro estudiante reproduce instalación y el PO acepta incremento final.

**Trabajo que desbloquea:** La demostración/validación del incremento del Sprint.

### D. Compuerta de salida del Sprint 4

**HITO-S4:** Recorrido completo demostrado, resultado correcto, pruebas P0 y documentación reproducible.

La evidencia incluye captura funcional, caso de prueba o informe reproducible, código integrado en rama compartida y revisión del Product Owner.

## 7. Dependencias transversales y bloqueos críticos

| Cadena | Dependencia verificable | Riesgo si no está lista |
| --- | --- | --- |
| G1-01 -> T1-15 -> T1-16 -> T1-11 | Reglas, SQL y contrato de subasta | Backend sin entidades persistentes |
| T1-09 -> T1-10 -> T1-14 -> T2-11 | Registro, sesión, RBAC y cola | Se exponen acciones ADMIN |
| T2-15 -> T2-16 -> T2-12 | Auditoría, versión y aprobación | Revisión doble inconsistente |
| T2-12 -> T3-13 -> T3-07 | Aprobación da hora de fin -> temporizador | Fin manipulable por reloj local |
| T3-15 -> T3-16 -> T3-10 -> T3-18 | Bids e índices -> serialización -> pruebas | Pujas simultáneas inconsistentes |
| T3-14 -> T4-09 -> T4-03 | Cierre -> resultado API -> pantalla | Ganador mostrado incorrectamente |
## 8. Política operativa de desbloqueo

1. Responsable de tarea predecesora adjunta commit, enlace a prototipo, consulta SQL o evidencia de prueba.
2. Quien depende revisa la salida y confirma contrato mínimo, esquema o comportamiento esperado.
3. Si hay discrepancia, se crea incidencia en el tablero y no se marca la tarea bloqueada como iniciable.
4. SM-01 registra bloqueos diarios; PO-01 decide los cambios de alcance y los extras del Sprint 4.
5. Se recomienda ejecutar pruebas de integración desde Sprint 1, no aplazarlas para la última semana.

**Regla de coherencia:** todas las 72 tareas del plan tienen prerequisito explícito, historia, RF, dueño, área, horas, día y evidencia. Los 12 habilitadores PO/SM tienen dependencias y entregables. Consultar auditoría en 09_Anexos/Auditoria_Dependencias_v4.json.
