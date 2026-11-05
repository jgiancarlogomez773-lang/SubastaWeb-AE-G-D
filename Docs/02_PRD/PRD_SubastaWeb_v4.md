# PRD - Requisitos de producto y alcance del MVP

**Proyecto:** SubastaWeb · **ID:** PRD-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

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
