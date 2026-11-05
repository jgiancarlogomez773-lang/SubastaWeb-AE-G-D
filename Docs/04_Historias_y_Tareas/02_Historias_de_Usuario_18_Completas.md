# Product Backlog - Historias completas y criterios de aceptación

**Proyecto:** SubastaWeb · **ID:** BACKLOG-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

## Resumen por sprint

| Sprint | Meta | Historias | Puntos |
| --- | --- | --- | --- |
| 1 | Registro, acceso y solicitud privada | HU-01, HU-02, HU-03, HU-04 | Estimación por equipo en Planning |
| 2 | Moderación administrativa y publicación | HU-05, HU-06, HU-07, HU-08, HU-09 | Estimación por equipo en Planning |
| 3 | Motor de ofertas y cierre automático | HU-10, HU-11, HU-12, HU-13 | Estimación por equipo en Planning |
| 4 | Resultados y estabilización | HU-14, HU-15, HU-16, HU-17, HU-18 | Estimación por equipo en Planning |

## HU-01 - Registro de usuario

**Historia:** Como visitante, quiero crear una cuenta para participar en subastas.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-AUT-01

**Criterios Dado/Cuando/Entonces:**

- Dado un correo nuevo, cuando envío datos válidos, entonces se registra USER y se devuelve 201.
- Dado un correo ya registrado con distinto uso de mayúsculas, cuando registro, entonces el servicio responde 409.
- Dado un alta pública, cuando intento asignarme ADMIN, entonces el servidor ignora el rol solicitado.

**Tareas de implementación:** T1-01, T1-03, T1-04, T1-09, T1-15, T1-17, T1-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-02 - Inicio y cierre de sesión

**Historia:** Como usuario, quiero iniciar y cerrar sesión para acceder a mis funciones.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-AUT-02, RF-AUT-03

**Criterios Dado/Cuando/Entonces:**

- Dado un USER válido, cuando inicia sesión, entonces puede abrir Mis subastas.
- Dado una contraseña incorrecta, cuando inicia, entonces se devuelve 401.
- Dado un usuario conectado, cuando cierra sesión, entonces el servidor invalida su acceso.

**Tareas de implementación:** T1-01, T1-03, T1-05, T1-06, T1-10.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-03 - Permisos por rol y propiedad

**Historia:** Como usuario o administrador, quiero que cada acción respete mi rol y propiedad.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-SEG-01

**Criterios Dado/Cuando/Entonces:**

- Dado USER, cuando invoca /admin, entonces obtiene 403.
- Dado una sesión ausente, cuando consulta una ruta privada, entonces obtiene 401.
- Dado un recurso de otro vendedor, cuando intenta editarlo, entonces se deniega sin revelar datos.

**Tareas de implementación:** T1-02, T1-05, T1-06, T1-14.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-04 - Solicitud de publicación

**Historia:** Como usuario, quiero registrar un artículo para enviarlo a revisión.

**Sprint:** 1 · **Prioridad:** Debe · **RF:** RF-SUB-01

**Criterios Dado/Cuando/Entonces:**

- Dado un USER conectado, cuando envía datos e imagen permitida, entonces se guarda PENDIENTE_REVISION.
- Dado una solicitud pendiente, cuando se consulta catálogo público, entonces no aparece.
- Dado imagen inválida o precio fuera de rango, cuando envío, entonces obtengo error sin persistencia parcial.

**Tareas de implementación:** T1-01, T1-02, T1-07, T1-08, T1-11, T1-12, T1-13, T1-15, T1-16, T1-17, T1-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-05 - Consultar mis solicitudes

**Historia:** Como usuario, quiero consultar el estado de mis solicitudes.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-SUB-02

**Criterios Dado/Cuando/Entonces:**

- Dado un USER, cuando consulta Mis subastas, entonces solo ve publicaciones propias con estado.
- Dado una publicación rechazada, cuando consulto la ficha propia, entonces veo causa.
- Dado otro USER, cuando solicita el mismo ID privado, entonces la API deniega el acceso.

**Tareas de implementación:** T2-01, T2-03, T2-09, T2-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-06 - Editar y reenviar rechazada

**Historia:** Como usuario, quiero corregir una solicitud rechazada y reenviarla.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-SUB-03, RF-SUB-04

**Criterios Dado/Cuando/Entonces:**

- Dado una rechazada, cuando edito contenido y reenvío, entonces queda PENDIENTE_REVISION en nueva ronda.
- Dado una rechazada sin cambios, cuando reenvío, entonces obtengo 409.
- Dado una subasta ACTIVA, cuando intento editar, entonces el backend rechaza el cambio.

**Tareas de implementación:** T2-01, T2-04, T2-10, T2-16.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-07 - Cola de revisión ADMIN

**Historia:** Como administrador, quiero ver las solicitudes pendientes.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-MOD-01

**Criterios Dado/Cuando/Entonces:**

- Dado ADMIN, cuando consulta pendientes, entonces recibe solicitudes con versión vigente.
- Dado USER, cuando solicita la bandeja, entonces obtiene 403.
- Dado que no hay pendientes, cuando abre bandeja, entonces ve listado vacío sin error.

**Tareas de implementación:** T2-01, T2-05, T2-11, T2-15.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-08 - Aprobar o rechazar

**Historia:** Como administrador, quiero decidir con motivo y control de versión.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-MOD-02, RF-MOD-03, RF-MOD-04

**Criterios Dado/Cuando/Entonces:**

- Dado ADMIN y solicitud pendiente, cuando aprueba, entonces queda ACTIVA con starts_at y ends_at.
- Dado rechazo, cuando registra motivo válido, entonces el usuario ve RECHAZADA y la causa.
- Dado una revisión ya decidida, cuando intenta decidir de nuevo, entonces obtiene 409 sin duplicar decisión.

**Tareas de implementación:** T2-01, T2-06, T2-12, T2-15, T2-16, T2-17.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-09 - Catálogo y detalle público

**Historia:** Como visitante, quiero consultar subastas aprobadas.

**Sprint:** 2 · **Prioridad:** Debe · **RF:** RF-CAT-01, RF-CAT-02

**Criterios Dado/Cuando/Entonces:**

- Dado una ACTIVA, cuando entro sin sesión, entonces veo catálogo y detalle.
- Dado una RECHAZADA o PENDIENTE_REVISION, cuando consulto público, entonces no aparece.
- Dado detalle público, cuando examino respuesta, entonces no hay emails ni hashes.

**Tareas de implementación:** T2-02, T2-07, T2-08, T2-13, T2-14, T2-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-10 - Registrar puja

**Historia:** Como comprador, quiero ofertar de forma válida y segura.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-PUJ-01, RF-PUJ-02, RF-PUJ-03

**Criterios Dado/Cuando/Entonces:**

- Dado ACTIVA ajena, cuando oferto mínimo válido, entonces se registra 201.
- Dado el vendedor, cuando intenta ofertar, entonces se rechaza.
- Dado dos ofertas simultáneas por debajo del nuevo mínimo, cuando compiten, entonces solo persiste la válida.

**Tareas de implementación:** T3-01, T3-03, T3-04, T3-09, T3-10, T3-15, T3-16, T3-17, T3-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-11 - Precio actual e historial

**Historia:** Como visitante, quiero observar precio líder e historial público.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-PUJ-04

**Criterios Dado/Cuando/Entonces:**

- Dado subasta sin ofertas, cuando consulto precio actual, entonces se muestra precio inicial.
- Dado ofertas aceptadas, cuando consulto historial, entonces veo precio líder y orden.
- Dado visitante, cuando consulta pujas, entonces no recibe datos personales.

**Tareas de implementación:** T3-01, T3-05, T3-06, T3-11, T3-12.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-12 - Temporizador

**Historia:** Como participante, quiero saber cuánto tiempo queda.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-TMP-01

**Criterios Dado/Cuando/Entonces:**

- Dado ACTIVA, cuando carga detalle, entonces se muestra contador basado en ends_at y server_time.
- Dado vencimiento, cuando transcurre el tiempo, entonces el contador llega a cero.
- Dado reloj local adulterado, cuando intento pujar después de ends_at, entonces el servidor rechaza.

**Tareas de implementación:** T3-01, T3-02, T3-07, T3-08, T3-13.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-13 - Cierre y ganador interno

**Historia:** Como sistema, quiero finalizar vencidas y guardar ganador o nulo.

**Sprint:** 3 · **Prioridad:** Debe · **RF:** RF-CIE-01, RF-CIE-02

**Criterios Dado/Cuando/Entonces:**

- Dado una ACTIVA vencida, cuando corre el trabajo de cierre, entonces pasa a FINALIZADA.
- Dado 0 pujas, cuando cierra, entonces winner_bid_id queda NULL.
- Dado doble ejecución del trabajo, cuando vuelve a cerrar, entonces resultado permanece inalterado.

**Tareas de implementación:** T3-02, T3-08, T3-14, T3-15, T3-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-14 - Resultados finales

**Historia:** Como visitante, quiero consultar el resultado cuando termina una subasta.

**Sprint:** 4 · **Prioridad:** Debe · **RF:** RF-RES-01

**Criterios Dado/Cuando/Entonces:**

- Dado FINALIZADA con pujas, cuando veo el resultado, entonces obtengo alias y monto ganador.
- Dado FINALIZADA sin pujas, cuando veo resultado, entonces indica Sin ofertas.
- Dado ACTIVA, cuando consulto resultado final, entonces no se declara ganador anticipadamente.

**Tareas de implementación:** T4-01, T4-02, T4-03, T4-04, T4-09, T4-10, T4-15, T4-16, T4-17, T4-18.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-15 - Mis ofertas

**Historia:** Como usuario, quiero consultar mis ofertas registradas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-CLI-01

**Criterios Dado/Cuando/Entonces:**

- Dado USER, cuando consulta Mis ofertas, entonces ve exclusivamente sus pujas.
- Dado USER A, cuando suplanta ID de USER B, entonces no recibe ofertas de B.

**Tareas de implementación:** T4-05, T4-11.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-16 - Filtros de catálogo

**Historia:** Como visitante, quiero filtrar subastas aprobadas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-CAT-03

**Criterios Dado/Cuando/Entonces:**

- Dado catálogo público, cuando filtro categoría o título, entonces lista resultados públicos.
- Dado filtro sin coincidencias, cuando ejecuto, entonces veo estado vacío sin privados.

**Tareas de implementación:** T4-06, T4-12.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-17 - Historial de moderación

**Historia:** Como administrador, quiero auditar decisiones anteriores.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-MOD-05

**Criterios Dado/Cuando/Entonces:**

- Dado ADMIN, cuando consulta una subasta reenviada, entonces ve decisiones por ronda.
- Dado USER, cuando consulta auditoría, entonces recibe 403.

**Tareas de implementación:** T4-07, T4-13.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

## HU-18 - Administración de usuarios

**Historia:** Como administrador, quiero consultar las cuentas registradas.

**Sprint:** 4 · **Prioridad:** Debería · **RF:** RF-ADM-01

**Criterios Dado/Cuando/Entonces:**

- Dado ADMIN, cuando lista usuarios, entonces ve correo, nombre y rol.
- Dado USER, cuando lista usuarios, entonces recibe 403.
- Dado cualquier respuesta, cuando la examino, entonces no incluye password_hash.

**Tareas de implementación:** T4-08, T4-14.

**Evidencia de aceptación:** demostración y prueba funcional de los criterios anteriores, bajo una compilación integrada.

**Alcance:** Las historias HU-15 a HU-18 son negociables. Todas las HU Debe se validan antes de cerrar el MVP.
