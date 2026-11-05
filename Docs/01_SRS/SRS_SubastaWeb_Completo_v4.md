# SRS - Especificación de requisitos de software

**Proyecto:** SubastaWeb · **ID:** SRS-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. Documento consolidado: PRD + TRD + Scrum; el plan detallado se mantiene en un artefacto aparte.

## Control documental

| Campo | Información |
| --- | --- |
| Identificador | SRS-SW-004 |
| Versión | 4.0 |
| Fecha | 30 de octubre de 2026 |
| Equipo | 2 integrantes de producto, 1 Scrum Master, 9 Developers |
| Norma | Estructura inspirada en ISO/IEC/IEEE 29148:2018 |
| Clasificación | Proyecto académico |
| Estado | Borrador para validación |
| Responsables | PO-01 responsable; PO-02 apoyo; SM-01 coordinación |
**Aprobaciones pendientes:** firma del PO-01, revisión técnica por D05-D09 y validación de reglas por PO-02. No se afirman aprobaciones aún no recibidas.

## Índice y documentos relacionados

- **Parte I / PRD:** problema, escenarios, reglas, 26 RF, 14 RNF, 18 HU y aceptación.
- **Parte II / TRD:** arquitectura, API REST, modelo de 7 tablas, seguridad, flujos, operaciones transaccionales y pruebas.
- **Parte III / Scrum:** roles de 12 estudiantes, 4 sprints, capacidad, riesgos, DoD.
- **Anexo de ejecución:** `04_Historias_y_Tareas/01_PLAN_VIGENTE_v4_Tareas_Detalladas_y_Dependencias.docx` contiene 72 fichas por tarea con predecesoras.
- **Anexo gráfico:** 10 diagramas PNG/SVG con fuentes editables Mermaid y Graphviz.

## Diagnóstico de viabilidad

**Dictamen: VIABLE CON AJUSTES.** El alcance se limita al recorrido de aprobación, ofertas y cierre. Las historias Debería no se comprometen por defecto. Se presupone una disponibilidad **no confirmada** de 12 h/semana por cada uno de los nueve Developers, con 8 horas de tareas estimadas y 4 h para actividades transversales, QA y contingencia. El motor de pujas y la moderación son riesgos de alta severidad. Si el tiempo real es menor, reducir las funciones opcionales y acordar criterios P0 antes de comenzar.


# PARTE I - ESPECIFICACIÓN DEL PRODUCTO (PRD)

## 1. Problema y visión

En un proceso informal de subasta, las ofertas, los plazos y las decisiones de publicación no quedan trazables. SubastaWeb permite registrar artículos, someterlos a moderación y aceptar pujas sobre publicaciones aprobadas, con resultado verificable. El objetivo es **demostrar una subasta ascendente completa** en un proyecto académico; no administrar cobros, entregas ni garantías comerciales.

**Usuarios:** visitante, usuario registrado (vende y/o puja) y administrador (modera). El segundo Product Owner apoya a PO-01, pero solo PO-01 prioriza el Product Backlog.

## 2. Objetivos medibles

| ID | Resultado de producto | Métrica de aceptación | Validación |
| --- | --- | --- | --- |
| OBJ-01 | Alta y sesión segura | Registro + login + logout + roles en entorno demo | TC-01 a TC-04 |
| OBJ-02 | Moderación verificable | Rechazo, corrección, aprobación y auditabilidad | TC-05 a TC-11 |
| OBJ-03 | Subasta íntegra | 20 solicitudes concurrentes sin pujas incompatibles | TC-15 a TC-18 |
| OBJ-04 | Cierre y ganador | Subasta vencida cierra una sola vez; ganador/NULL correcto | TC-19 a TC-23 |
| OBJ-05 | Entrega académica | 4 Sprint Reviews + documentación + instalación reproducible | Checklist de cierre |
## 3. Alcance y prioridades MoSCoW

**Debe:** registro e inicio/cierre de sesión, permisos, publicar solicitud privada, ver solicitudes propias, editar y reenviar rechazo, bandeja ADMIN, aprobar/rechazar, catálogo y detalle, pujas válidas con protección de concurrencia, historial público, temporizador, cierre y ganador, pantalla de resultados.

**Debería (si Sprint 4 tiene capacidad):** Mis ofertas, búsqueda/filtros, auditoría de moderación en pantalla, listado básico de usuarios.

**No en esta versión:** pasarela de pagos, cobros, envíos, mensajería, reputación, notificaciones externas, chat en tiempo real, app móvil nativa y pujas automáticas. Son propuestas para evolución futura, no compromisos de cuatro semanas.

## 4. Escenarios prioritarios

| Escenario | Nombre | Recorrido principal | Excepciones |
| --- | --- | --- | --- |
| E-01 | Registro | Visitante aporta datos -> sistema crea cuenta USER -> abre sesión en flujo posterior | Correo duplicado y datos inválidos |
| E-02 | Solicitud | Vendedor carga artículo, monto inicial, categoría, duración e imagen -> pendiente | Imagen o importe inválidos |
| E-03 | Moderación | ADMIN visualiza pendientes y registra APROBAR/RECHAZAR con control de ronda | Dos ADMIN deciden al mismo tiempo |
| E-04 | Corrección | Vendedor lee rechazo, edita contenido y reenvía nueva ronda | Sin cambios o subasta ya activa |
| E-05 | Puja | Comprador ve líder, oferto >= mínimo -> DB acepta dentro de transacción | Self-bid, vencimiento y pujas concurrentes |
| E-06 | Cierre | Trabajo periódico revisa vencidas y fija winning_bid_id o NULL | Reejecución, ausencia de ofertas |
| E-07 | Consulta | Visitante ve subasta pública, historial despersonalizado y resultado | Intento de leer pendiente/rechazada |
## 5. Reglas de negocio propuestas

| ID | Regla de negocio |
| --- | --- |
| RN-01 | Alta pública asigna solo USER; ADMIN se aprovisiona por procedimiento controlado. |
| RN-02 | Una solicitud nace PENDIENTE_REVISION; no es visible en catálogo. |
| RN-03 | La revisión ADMIN es atómica: una decisión por ronda. |
| RN-04 | Solo RECHAZADA se puede editar; para reenviar debe haber cambios desde el rechazo. |
| RN-05 | Al aprobar, starts_at y ends_at se calculan desde hora de servidor. |
| RN-06 | La duración permitida es 24, 48 o 72 h. Pendiente de aprobación final de PO. |
| RN-07 | Precio inicial desde COP 1.000; importe entero positivo. |
| RN-08 | Primer bid >= precio inicial; siguientes >= mayor bid + COP 1.000. |
| RN-09 | El propietario no puede ofertar por su propia publicación. |
| RN-10 | No se admiten bids después de ends_at, incluso si el worker no ha cerrado. |
| RN-11 | El control de monto y el insert deben ejecutarse bajo bloqueo de la fila auction. |
| RN-12 | Cada puja válida es inmutable; no se eliminan bids en el MVP. |
| RN-13 | Al cerrar, gana la mayor oferta aceptada; si no hubo pujas, NULL. |
| RN-14 | El cierre es idempotente, aunque el job se ejecute varias veces. |
| RN-15 | Una sola imagen JPG/PNG/WebP de hasta 3 MB por solicitud en MVP. |
| RN-16 | El público consulta alias del ganador, sin correo ni teléfono. |

**Nota:** precios, incrementos, duración e imagen constituyen parámetros propuestos, no decisiones institucionales. Deben aprobarse en G1-01/G3-01 antes de comprometer la implementación.

## 6. Requisitos funcionales (RF)

| ID | Módulo | Comportamiento exigido | Prioridad | Criterio verificable |
| --- | --- | --- | --- | --- |
| RF-AUT-01 | Registro | El visitante registra cuenta con nombre, correo y contraseña; siempre recibe rol USER. | Debe | Correo repetido -> 409; contraseña se guarda como hash. |
| RF-AUT-02 | Inicio de sesión | El usuario con credenciales válidas abre una sesión segura. | Debe | Credenciales erróneas -> 401; cookie HttpOnly. |
| RF-AUT-03 | Cierre de sesión | El usuario cierra sesión e invalida la sesión del servidor. | Debe | Ruta protegida después de logout -> 401. |
| RF-SEG-01 | Autorización | El servidor verifica rol USER/ADMIN y propiedad del recurso. | Debe | USER no accede a moderación; otro usuario no edita solicitud. |
| RF-SUB-01 | Crear solicitud | USER crea subasta con categoría, descripción, precio, duración e imagen; queda privada. | Debe | Se guarda PENDIENTE_REVISION; jamás aparece en catálogo. |
| RF-SUB-02 | Mis subastas | USER consulta exclusivamente sus solicitudes y sus estados. | Debe | Una cuenta no obtiene solicitudes ajenas. |
| RF-SUB-03 | Editar rechazo | USER puede modificar una solicitud RECHAZADA, no ACTIVA. | Debe | Versión incrementa; modificación ACTIVA rechazada. |
| RF-SUB-04 | Reenviar | USER reenvía una RECHAZADA solo tras realizar un cambio. | Debe | Nueva ronda pendiente; reenvío sin cambios -> 409. |
| RF-MOD-01 | Bandeja | ADMIN lista solicitudes pendientes de revisión. | Debe | USER -> 403, pendientes solamente. |
| RF-MOD-02 | Aprobar | ADMIN aprueba solicitud; reloj comienza al aprobar. | Debe | Estado ACTIVA, empieza y termina con horas de servidor. |
| RF-MOD-03 | Rechazar | ADMIN rechaza con motivo verificable. | Debe | Motivo de 10 a 500 caracteres, estado RECHAZADA. |
| RF-MOD-04 | Evitar doble decisión | La API serializa decisiones y rechaza una revisión obsoleta. | Debe | Segundo intento concurrente -> 409, no duplica auditoría. |
| RF-MOD-05 | Historial | ADMIN consulta decisiones anteriores y sus rondas. | Debería | Auditoría ordenada y protegida; datos solo de ADMIN. |
| RF-CAT-01 | Catálogo | Visitante lista únicamente subastas ACTIVA y FINALIZADA. | Debe | Pendientes/rechazadas nunca aparecen. |
| RF-CAT-02 | Detalle | Visitante ve detalle de subasta pública sin información privada. | Debe | No expone correo ni hash del vendedor. |
| RF-CAT-03 | Filtros | Visitante filtra por categoría y título en catálogo público. | Debería | Filtros no revelan subastas privadas. |
| RF-PUJ-01 | Enviar puja | USER envía oferta solamente en subasta ACTIVA ajena. | Debe | Autopuja/no autenticado/vencida se rechazan. |
| RF-PUJ-02 | Monto mínimo | Primera oferta >= precio inicial; siguientes >= líder + COP 1.000. | Debe | Importes inferiores -> 422 o 409 según causa. |
| RF-PUJ-03 | Concurrencia | Pujas de la misma subasta se verifican dentro de transacción serializada. | Debe | 20 pujas simultáneas no generan dos líderes incompatibles. |
| RF-PUJ-04 | Historial | Visitante ve mejor precio e historial resumido sin correos. | Debe | Orden estable, consulta paginada, sin PII. |
| RF-TMP-01 | Reloj | La vista muestra tiempo restante calculado desde fecha/hora del servidor. | Debe | No permite extender la subasta al cambiar reloj local. |
| RF-CIE-01 | Finalizar | Un proceso automático cierra ACTIVA vencida idempotentemente. | Debe | Doble ejecución no duplica cambios; puja tardía rechazada. |
| RF-CIE-02 | Ganador | Se guarda id de la oferta válida máxima, o NULL si no hubo pujas. | Debe | FK impide ganador de otra subasta. |
| RF-RES-01 | Resultado | Visitante ve al finalizar alias del ganador y monto, o Sin ofertas. | Debe | Resultado coherente con última oferta aceptada. |
| RF-CLI-01 | Mis ofertas | USER consulta sus ofertas sin exponer las ajenas. | Debería | Filtro por sesión, nunca por id enviado por usuario. |
| RF-ADM-01 | Usuarios | ADMIN lista usuarios básicos sin credenciales. | Debería | USER -> 403; password_hash jamás retorna. |

## 7. Requisitos no funcionales (RNF)

| ID | Categoría | Objetivo propuesto | Evidencia |
| --- | --- | --- | --- |
| RNF-SEG-01 | Sesiones | Cookie HttpOnly, Secure en HTTPS, SameSite=Lax y CSRF validado | Pruebas manuales de cookie y peticiones inseguras |
| RNF-SEG-02 | Credenciales | BCrypt/Argon2 configurado para hash; no contraseñas en logs | Inspección de DB y logs de pruebas |
| RNF-SEG-03 | Entradas | DTO con Bean Validation, tipos de archivo permitidos y límite de 3 MB | Tests JSON y multipart inválidos |
| RNF-PRI-01 | Datos personales | El API público no muestra correo, hash ni identidades de pujadores | Contrato JSON + pruebas rol/propiedad |
| RNF-FIA-01 | Integridad transaccional | Moderación y pujas en transacciones PostgreSQL serializadas por subasta | Dos revisiones y 20 pujas concurrentes |
| RNF-FIA-02 | Idempotencia | El cierre automático es seguro ante dos ejecuciones consecutivas | Cierre doble sobre mismo ID |
| RNF-REN-01 | Rendimiento | Meta de laboratorio: 95% de GET públicos < 1.5 s para 20 usuarios concurrentes | Prueba simple en ambiente compartido |
| RNF-DIS-01 | Recuperación | Se documenta restauración desde un respaldo de demostración | Ensayo backup/restore |
| RNF-USA-01 | Responsive | Vistas utilizables desde 360 px y teclado en acciones críticas | Checklist en móvil/desktop |
| RNF-ACC-01 | Accesibilidad | Inputs etiquetados, errores textuales, foco visible y contraste adecuado | Revisión de interfaz por Diseño |
| RNF-MAN-01 | Mantenibilidad | PR revisadas, lint y build ejecutados antes de merge | Evidencia Git + pipeline |
| RNF-OPA-01 | Entorno reproducible | README con Java, Node, PostgreSQL, variables sin secretos | Instalación por un compañero distinto |
| RNF-OPA-02 | Observabilidad | Logs sin PII para alta, moderación, pujas y cierre, con ID correlación | Revisión de logs en demo |
| RNF-COM-01 | Compatibilidad | Frontend en navegadores modernos Chromium/Firefox | Smoke test en dos navegadores |

## 8. Historias de usuario y criterios de aceptación

### HU-01 - Registro de usuario

**Historia:** Como visitante, quiero crear una cuenta para participar en subastas.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-AUT-01

- Dado un correo nuevo, cuando envío datos válidos, entonces se registra USER y se devuelve 201.
- Dado un correo ya registrado con distinto uso de mayúsculas, cuando registro, entonces el servicio responde 409.
- Dado un alta pública, cuando intento asignarme ADMIN, entonces el servidor ignora el rol solicitado.

**Tareas vinculadas:** T1-01, T1-03, T1-04, T1-09, T1-15, T1-17, T1-18.

### HU-02 - Inicio y cierre de sesión

**Historia:** Como usuario, quiero iniciar y cerrar sesión para acceder a mis funciones.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-AUT-02, RF-AUT-03

- Dado un USER válido, cuando inicia sesión, entonces puede abrir Mis subastas.
- Dado una contraseña incorrecta, cuando inicia, entonces se devuelve 401.
- Dado un usuario conectado, cuando cierra sesión, entonces el servidor invalida su acceso.

**Tareas vinculadas:** T1-01, T1-03, T1-05, T1-06, T1-10.

### HU-03 - Permisos por rol y propiedad

**Historia:** Como usuario o administrador, quiero que cada acción respete mi rol y propiedad.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-SEG-01

- Dado USER, cuando invoca /admin, entonces obtiene 403.
- Dado una sesión ausente, cuando consulta una ruta privada, entonces obtiene 401.
- Dado un recurso de otro vendedor, cuando intenta editarlo, entonces se deniega sin revelar datos.

**Tareas vinculadas:** T1-02, T1-05, T1-06, T1-14.

### HU-04 - Solicitud de publicación

**Historia:** Como usuario, quiero registrar un artículo para enviarlo a revisión.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-SUB-01

- Dado un USER conectado, cuando envía datos e imagen permitida, entonces se guarda PENDIENTE_REVISION.
- Dado una solicitud pendiente, cuando se consulta catálogo público, entonces no aparece.
- Dado imagen inválida o precio fuera de rango, cuando envío, entonces obtengo error sin persistencia parcial.

**Tareas vinculadas:** T1-01, T1-02, T1-07, T1-08, T1-11, T1-12, T1-13, T1-15, T1-16, T1-17, T1-18.

### HU-05 - Consultar mis solicitudes

**Historia:** Como usuario, quiero consultar el estado de mis solicitudes.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-SUB-02

- Dado un USER, cuando consulta Mis subastas, entonces solo ve publicaciones propias con estado.
- Dado una publicación rechazada, cuando consulto la ficha propia, entonces veo causa.
- Dado otro USER, cuando solicita el mismo ID privado, entonces la API deniega el acceso.

**Tareas vinculadas:** T2-01, T2-03, T2-09, T2-18.

### HU-06 - Editar y reenviar rechazada

**Historia:** Como usuario, quiero corregir una solicitud rechazada y reenviarla.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-SUB-03, RF-SUB-04

- Dado una rechazada, cuando edito contenido y reenvío, entonces queda PENDIENTE_REVISION en nueva ronda.
- Dado una rechazada sin cambios, cuando reenvío, entonces obtengo 409.
- Dado una subasta ACTIVA, cuando intento editar, entonces el backend rechaza el cambio.

**Tareas vinculadas:** T2-01, T2-04, T2-10, T2-16.

### HU-07 - Cola de revisión ADMIN

**Historia:** Como administrador, quiero ver las solicitudes pendientes.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-MOD-01

- Dado ADMIN, cuando consulta pendientes, entonces recibe solicitudes con versión vigente.
- Dado USER, cuando solicita la bandeja, entonces obtiene 403.
- Dado que no hay pendientes, cuando abre bandeja, entonces ve listado vacío sin error.

**Tareas vinculadas:** T2-01, T2-05, T2-11, T2-15.

### HU-08 - Aprobar o rechazar

**Historia:** Como administrador, quiero decidir con motivo y control de versión.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-MOD-02, RF-MOD-03, RF-MOD-04

- Dado ADMIN y solicitud pendiente, cuando aprueba, entonces queda ACTIVA con starts_at y ends_at.
- Dado rechazo, cuando registra motivo válido, entonces el usuario ve RECHAZADA y la causa.
- Dado una revisión ya decidida, cuando intenta decidir de nuevo, entonces obtiene 409 sin duplicar decisión.

**Tareas vinculadas:** T2-01, T2-06, T2-12, T2-15, T2-16, T2-17.

### HU-09 - Catálogo y detalle público

**Historia:** Como visitante, quiero consultar subastas aprobadas.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-CAT-01, RF-CAT-02

- Dado una ACTIVA, cuando entro sin sesión, entonces veo catálogo y detalle.
- Dado una RECHAZADA o PENDIENTE_REVISION, cuando consulto público, entonces no aparece.
- Dado detalle público, cuando examino respuesta, entonces no hay emails ni hashes.

**Tareas vinculadas:** T2-02, T2-07, T2-08, T2-13, T2-14, T2-18.

### HU-10 - Registrar puja

**Historia:** Como comprador, quiero ofertar de forma válida y segura.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-PUJ-01, RF-PUJ-02, RF-PUJ-03

- Dado ACTIVA ajena, cuando oferto mínimo válido, entonces se registra 201.
- Dado el vendedor, cuando intenta ofertar, entonces se rechaza.
- Dado dos ofertas simultáneas por debajo del nuevo mínimo, cuando compiten, entonces solo persiste la válida.

**Tareas vinculadas:** T3-01, T3-03, T3-04, T3-09, T3-10, T3-15, T3-16, T3-17, T3-18.

### HU-11 - Precio actual e historial

**Historia:** Como visitante, quiero observar precio líder e historial público.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-PUJ-04

- Dado subasta sin ofertas, cuando consulto precio actual, entonces se muestra precio inicial.
- Dado ofertas aceptadas, cuando consulto historial, entonces veo precio líder y orden.
- Dado visitante, cuando consulta pujas, entonces no recibe datos personales.

**Tareas vinculadas:** T3-01, T3-05, T3-06, T3-11, T3-12.

### HU-12 - Temporizador

**Historia:** Como participante, quiero saber cuánto tiempo queda.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-TMP-01

- Dado ACTIVA, cuando carga detalle, entonces se muestra contador basado en ends_at y server_time.
- Dado vencimiento, cuando transcurre el tiempo, entonces el contador llega a cero.
- Dado reloj local adulterado, cuando intento pujar después de ends_at, entonces el servidor rechaza.

**Tareas vinculadas:** T3-01, T3-02, T3-07, T3-08, T3-13.

### HU-13 - Cierre y ganador interno

**Historia:** Como sistema, quiero finalizar vencidas y guardar ganador o nulo.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-CIE-01, RF-CIE-02

- Dado una ACTIVA vencida, cuando corre el trabajo de cierre, entonces pasa a FINALIZADA.
- Dado 0 pujas, cuando cierra, entonces winner_bid_id queda NULL.
- Dado doble ejecución del trabajo, cuando vuelve a cerrar, entonces resultado permanece inalterado.

**Tareas vinculadas:** T3-02, T3-08, T3-14, T3-15, T3-18.

### HU-14 - Resultados finales

**Historia:** Como visitante, quiero consultar el resultado cuando termina una subasta.

**Sprint:** 4 · **Prioridad:** Debe · **RF:** RF-RES-01

- Dado FINALIZADA con pujas, cuando veo el resultado, entonces obtengo alias y monto ganador.
- Dado FINALIZADA sin pujas, cuando veo resultado, entonces indica Sin ofertas.
- Dado ACTIVA, cuando consulto resultado final, entonces no se declara ganador anticipadamente.

**Tareas vinculadas:** T4-01, T4-02, T4-03, T4-04, T4-09, T4-10, T4-15, T4-16, T4-17, T4-18.

### HU-15 - Mis ofertas

**Historia:** Como usuario, quiero consultar mis ofertas registradas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-CLI-01

- Dado USER, cuando consulta Mis ofertas, entonces ve exclusivamente sus pujas.
- Dado USER A, cuando suplanta ID de USER B, entonces no recibe ofertas de B.

**Tareas vinculadas:** T4-05, T4-11.

### HU-16 - Filtros de catálogo

**Historia:** Como visitante, quiero filtrar subastas aprobadas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-CAT-03

- Dado catálogo público, cuando filtro categoría o título, entonces lista resultados públicos.
- Dado filtro sin coincidencias, cuando ejecuto, entonces veo estado vacío sin privados.

**Tareas vinculadas:** T4-06, T4-12.

### HU-17 - Historial de moderación

**Historia:** Como administrador, quiero auditar decisiones anteriores.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-MOD-05

- Dado ADMIN, cuando consulta una subasta reenviada, entonces ve decisiones por ronda.
- Dado USER, cuando consulta auditoría, entonces recibe 403.

**Tareas vinculadas:** T4-07, T4-13.

### HU-18 - Administración de usuarios

**Historia:** Como administrador, quiero consultar las cuentas registradas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-ADM-01

- Dado ADMIN, cuando lista usuarios, entonces ve correo, nombre y rol.
- Dado USER, cuando lista usuarios, entonces recibe 403.
- Dado cualquier respuesta, cuando la examino, entonces no incluye password_hash.

**Tareas vinculadas:** T4-08, T4-14.

## 9. Interfaz prevista y accesibilidad

Pantallas: registro, login, catálogo, detalle público, crear solicitud, Mis subastas, editar solicitud rechazada, bandeja administrativa, moderación, formulario de puja, historial y resultado. Se utilizarán formularios etiquetados, navegación por teclado, estados vacíos y mensajes claros; el frontend será Vue 3 + Vite.

## 10. Condiciones de aceptación del MVP

1. El usuario publica una solicitud que permanece fuera del catálogo público.
2. ADMIN la rechaza con causa; el vendedor la corrige y reenvía con nueva ronda.
3. ADMIN la aprueba; el reloj de la subasta inicia en el servidor.
4. Dos compradores ofertan; se valida el incremento y la concurrencia.
5. La puja tardía o la del propietario se rechaza.
6. La subasta vencida se cierra y muestra ganador o Sin ofertas.
7. La aplicación puede ejecutarse usando README y datos de demostración sin claves fijas publicadas.
8. Todos los RF Debe cuentan con al menos una evidencia de prueba; los extras no comprometen P0.

## 11. Decisiones pendientes

- PEND-01: disponibilidad semanal real por persona (propuesta 12 h por desarrollador).
- PEND-02: política institucional de tratamiento de datos y conservación.
- PEND-03: alojamiento e infraestructura de demostración.
- PEND-04: límite de imagen y aceptación de roles/credenciales.
- PEND-05: duración, mínimo inicial e incremento de la subasta.
- PEND-06: grado de visibilidad de alias e historial público.

**Trazabilidad:** El archivo 09_Anexos/Matriz_Trazabilidad_v4.md liga HU, RF, tareas y pruebas. El detalle técnico completo está en TRD-SW-004.

# PARTE II - ESPECIFICACIÓN TÉCNICA (TRD)

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

# PARTE III - EJECUCIÓN CON SCRUM

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

# PARTE IV - AUDITORÍA DE COHERENCIA

## Validaciones a realizar antes de la línea base

1. PO-01 confirma reglas RN-01..RN-16 y límites del MVP.
2. Los nueve Developers confirman horas disponibles y revisan estimación por tarea.
3. D05-D09 validan SQL y Flyway en PostgreSQL real; el script se entrega como propuesta verificable, no como migración aplicada.
4. D05-D07 implementan y prueban bloqueo transaccional para pujas y decisiones; D08-D09 documentan evidencia.
5. D01-D04 validan pantallas y estados en dos navegadores.
6. SM-01 comprueba dependencias y que los cuatro hitos se demuestran.
7. Las historias P1 solo se aprueban después de superar los criterios P0.

**Resultado documental:** código, HU, RF, flujos, diagrama, endpoint, esquema y casos de prueba tienen identificadores comunes. Los scripts de la base de datos y las pruebas de concurrencia requieren validación ejecutada por el equipo en Sprint 1 a Sprint 3.

## Historial de cambios

- **v2:** primera especificación con 13 estudiantes y stack anterior (histórico, reemplazado).
- **v3:** plan de 12 integrantes y 72 tareas (conservado sin cambios como archivo histórico en el ZIP).
- **v4:** todos los documentos vigentes coherentes en 12 integrantes; Vue + Spring Boot + PostgreSQL; siete tablas, diez diagramas, 72 fichas técnicas y matriz de trazabilidad.

## Glosario

**Puja:** cantidad ofrecida por un usuario distinto del vendedor. **Moderación:** revisión ADMIN previa a la publicación pública. **Ronda de revisión:** ciclo independiente de aprobación/rechazo. **Concurrencia:** peticiones simultáneas que compiten sobre una misma subasta. **Idempotente:** ejecutar repetidas veces tiene el mismo estado final. **RFC/ADR:** registro breve de decisiones técnicas. **DoD:** Definition of Done. **P0:** imprescindible en MVP; **P1:** negociable.
