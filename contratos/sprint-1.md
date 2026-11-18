# Contrato del Sprint 1 — Registro, acceso y solicitud privada

**Proyecto:** SubastaWeb · **Documentos base:** SRS-SW-004 v4.0 y PLAN-SW-004 v4.0 · **Hito de cierre:** HITO-S1
**Fechas:** inicio mié 18/11/2026 · entrega lun 23/11/2026

> Este contrato fija lo que el equipo se compromete a entregar y lo que Frontend, Backend y Base de datos deben respetar entre sí. Cualquier cambio de alcance lo decide PO-01 y se registra en este archivo mediante Pull Request.

## 1. Convenciones comunes de la API
- Prefijo de rutas: `/api/v1`. Fechas en ISO 8601 UTC. Montos en COP, enteros sin decimales.
- Errores con `code`, `message`, `requestId` y `details`, sin revelar secretos.
- Códigos HTTP: 401 sesión no válida · 403 rol/propiedad · 404 no disponible o privado · 409 conflicto/cerrada/versión antigua · 422 validación.
- Códigos estables: `AUTH_REQUIRED`, `FORBIDDEN`, `AUCTION_NOT_PUBLIC`, `STALE_REVIEW`, `AUCTION_CLOSED`, `BID_BELOW_MINIMUM`, `SELF_BID_NOT_ALLOWED`, `INVALID_IMAGE`.

## 2. Historias comprometidas

### HU-01 — Registro de usuario
**Historia:** Como visitante, quiero crear una cuenta para participar en subastas.  
**Prioridad:** Debe · **RF:** RF-AUT-01  
**Tareas vinculadas:** T1-01, T1-03, T1-04, T1-09, T1-15, T1-17, T1-18.

**Criterios de aceptación**
- [ ] Dado un correo nuevo, cuando envío datos válidos, entonces se registra USER y se devuelve 201.
- [ ] Dado un correo ya registrado con distinto uso de mayúsculas, cuando registro, entonces el servicio responde 409.
- [ ] Dado un alta pública, cuando intento asignarme ADMIN, entonces el servidor ignora el rol solicitado.

### HU-02 — Inicio y cierre de sesión
**Historia:** Como usuario, quiero iniciar y cerrar sesión para acceder a mis funciones.  
**Prioridad:** Debe · **RF:** RF-AUT-02, RF-AUT-03  
**Tareas vinculadas:** T1-01, T1-03, T1-05, T1-06, T1-10.

**Criterios de aceptación**
- [ ] Dado un USER válido, cuando inicia sesión, entonces puede abrir Mis subastas.
- [ ] Dado una contraseña incorrecta, cuando inicia, entonces se devuelve 401.
- [ ] Dado un usuario conectado, cuando cierra sesión, entonces el servidor invalida su acceso.

### HU-03 — Permisos por rol y propiedad
**Historia:** Como usuario o administrador, quiero que cada acción respete mi rol y propiedad.  
**Prioridad:** Debe · **RF:** RF-SEG-01  
**Tareas vinculadas:** T1-02, T1-05, T1-06, T1-14.

**Criterios de aceptación**
- [ ] Dado USER, cuando invoca /admin, entonces obtiene 403.
- [ ] Dado una sesión ausente, cuando consulta una ruta privada, entonces obtiene 401.
- [ ] Dado un recurso de otro vendedor, cuando intenta editarlo, entonces se deniega sin revelar datos.

### HU-04 — Solicitud de publicación
**Historia:** Como usuario, quiero registrar un artículo para enviarlo a revisión.  
**Prioridad:** Debe · **RF:** RF-SUB-01  
**Tareas vinculadas:** T1-01, T1-02, T1-07, T1-08, T1-11, T1-12, T1-13, T1-15, T1-16, T1-17, T1-18.

**Criterios de aceptación**
- [ ] Dado un USER conectado, cuando envía datos e imagen permitida, entonces se guarda PENDIENTE_REVISION.
- [ ] Dado una solicitud pendiente, cuando se consulta catálogo público, entonces no aparece.
- [ ] Dado imagen inválida o precio fuera de rango, cuando envío, entonces obtengo error sin persistencia parcial.

## 3. Endpoints del sprint

| Método | Ruta (prefijo /api/v1) | Actor | Operación | HU | HTTP |
| --- | --- | --- | --- | --- | --- |
| POST | /auth/register | Visitante | Crear USER | HU-01 | 201 / 409 / 422 |
| POST | /auth/login | Visitante | Autenticar, cookie de sesión | HU-02 | 200 / 401 |
| POST | /auth/logout | USER/ADMIN | Invalidar sesión | HU-02 | 204 / 401 |
| GET | /auth/me | USER/ADMIN | Identidad autenticada | HU-02 | 200 / 401 |
| GET | /categories | Público | Categorías activas | HU-04 | 200 |
| POST | /auctions | USER | Crear solicitud privada + imagen | HU-04 | 201 / 401 / 422 |

## 4. Contratos JSON esenciales

**Registro `POST /api/v1/auth/register`**

```json
{"fullName":"Usuario Demo","email":"usuario@example.test","password":"********"}
```

**Nueva solicitud `POST /api/v1/auctions` (multipart/form-data)**

Campos: `title`, `description`, `categoryId`, `startingPrice`, `durationHours`, `image` (archivo JPG/PNG/WebP, máx. 3 MB)

## 5. Reglas de negocio aplicables

- **RN-01:** Alta pública asigna solo USER; ADMIN se aprovisiona por procedimiento controlado.
- **RN-02:** Una solicitud nace PENDIENTE_REVISION; no es visible en catálogo.
- **RN-07:** Precio inicial desde COP 1.000; importe entero positivo.
- **RN-15:** Una sola imagen JPG/PNG/WebP de hasta 3 MB por solicitud en MVP.

## 6. Compromisos por equipo

| Equipo | Integrantes | Entrega mínima del sprint |
| --- | --- | --- |
| Diseño | D01 | T1-01 Prototipos registro, login y nueva subasta; T1-02 Sistema visual y estados de validación |
| Frontend (Vue 3) | D02, D03, D04 | T1-03 Iniciar Vue 3 + Vite y navegación; T1-04 Integrar registro con Spring Boot; T1-05 Pantalla login, logout y guardas Vue; T1-06 Conectar sesión real y verificar roles; T1-07 Formulario de solicitud e imagen; T1-08 Conectar solicitud a API y confirmación |
| Backend (Spring Boot) | D05, D06, D07 | T1-09 POST /auth/register + hash contraseña; T1-10 Login, logout y usuario actual con Spring Security; T1-11 Inicializar Spring Boot y contrato de solicitud; T1-12 Guardar solicitud privada con imagen; T1-13 GET de categorías disponibles; T1-14 Autorización USER/ADMIN y propiedad |
| Base de datos / QA | D08, D09 | T1-15 Migración users y categories; T1-16 Migración auctions + estados iniciales; T1-17 Semillas y conexión PostgreSQL/Spring; T1-18 Prueba integrada y respaldo de datos del Sprint 1 |
| Producto | PO-01, PO-02 | G1-01 Definir reglas de negocio base; G1-02 Revisar criterios de aceptación y datos |
| Scrum | SM-01 | G1-03 Acordar objetivo y calidad del sprint |

## 7. Cadena de dependencias y hito
- **HITO-S1** debe cumplirse antes de iniciar el siguiente sprint.
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
