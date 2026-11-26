# Acta de revisión G1-02: criterios de aceptación y datos

| Campo | Valor |
|---|---|
| **Tarea** | [G1-02] Revisar criterios de aceptación y datos (issue #2) |
| **Sprint / día / estimación** | S1 / día 1 / 2 h |
| **Responsable** | PO-02 (@Joseber32) |
| **Prerrequisito** | G1-01: [enlace al acta o issue de G1-01] |
| **Habilitó** | T1-01 |
| **Historias** | HU-01, HU-02, HU-03, HU-04 |
| **Pruebas asociadas** | TC-01, TC-02, TC-03, TC-04 |
| **Documento elaborado** | 25/11/2026, al cierre del Sprint 1, para dejar la evidencia de la revisión |

## 1. Alcance y fuentes revisadas
SRS v4 (idéntico al v4.1), PRD v4, TRD v4, historias de usuario (18), matriz de trazabilidad, plan vigente de tareas, flujos FL-01 a FL-04, V1__schema_postgresql.sql, V2__categorias.sql, diccionario de datos y, del frontend, `RegistroView.vue`, `LoginView.vue` y el router.

## 2. Resultado por historia
| Historia | Criterios revisados | Gherkin | Prueba | Observación |
|---|---|---|---|---|
| HU-01 Registro | 3 del backlog + 1 de validación (422) | Sí (sección 4) | TC-01 | Faltaba el criterio de datos inválidos; se agregó |
| HU-02 Sesión | 3 del backlog + correo inexistente | Sí | TC-02 | Se agregó el mismo 401 genérico para correo inexistente |
| HU-03 Permisos | 3 del backlog | Sí | TC-03 | El escenario de editar recurso ajeno depende de PATCH (Sprint 2) |
| HU-04 Solicitud | 3 del backlog + sesión ausente y precio/duración/categoría | Sí | TC-04 | "No aparece en catálogo" se verifica con consulta de control en Sprint 1 |

## 3. Datos y reglas revisados
Fuente: SRS v4/v4.1 (idénticos), V1__schema_postgresql.sql, V2__categorias.sql, RN-01..RN-16.

### Registro (POST /auth/register)
| Campo | Regla | Estado |
|---|---|---|
| fullName | 2 a 100 caracteres (CHECK en BD) | Definido |
| email | Se normaliza (trim + minúsculas). Único sin distinguir mayúsculas. Máx. 254. | Definido. El CHECK de BD solo exige `@`; el formato completo lo valida Bean Validation |
| password | Se guarda como hash BCrypt/Argon2. No aparece en logs ni respuestas | Política mínima **sin definir** (H2) |
| role | Siempre USER; si el cliente envía un rol, se ignora (RN-01) | Definido |

### Solicitud (POST /auctions, multipart)
| Campo | Regla | Estado |
|---|---|---|
| title | 5 a 120 caracteres | Definido (BD) |
| description | 20 a 2000 caracteres | Definido (BD) |
| categoryId | Debe existir y estar activa. Semilla V2: Electrónica, Libros, Hogar, Deportes, Otros | Definido |
| startingPrice | Entero en COP, ≥ 1.000 (RN-07) | Mínimo propuesto; **tope máximo sin definir** |
| durationHours | 24, 48 o 72 (RN-06) | **Pendiente de aprobación PO** (PEND-05) |
| image | 1 archivo JPG/PNG/WebP, ≤ 3 MB (3.145.728 bytes) (RN-15) | **Pendiente PEND-04** |
| Estado inicial | PENDIENTE_REVISION, review_round 1, content_version 1, sin fechas | Definido |
| min_increment | No lo envía el usuario; valor por defecto 1.000 | Definido |

### Verificación contra la implementación del frontend
| Elemento | Lo observado | Conclusión |
|---|---|---|
| Nombre en el registro | `maxlength="150"` | La BD acepta de 2 a 100: ajustar a 100 |
| Contraseña | El formulario pide de 8 a 100 caracteres | Coherente con la propuesta; falta confirmar la aprobación de PO-01 (el SRS no la define) |
| Rol | El formulario no envía `role` | Correcto (RN-01) |
| Duplicado y validación | Maneja 409 (error en el correo) y 422 (vía `erroresDeServidor`) | Coherente con HU-01 |
| Login | Una sola ruta `/login` | Coincide con el único `POST /auth/login` del SRS |
| Doble envío | Bandera `cargando` desactiva el botón | Cumple el criterio de T1-04 |

## 4. Escenarios Gherkin HU-01 a HU-04
```gherkin
Característica: HU-01 Registro de usuario

  Escenario: Registro exitoso
    Dado un visitante con un correo que no existe
    Cuando envía nombre, correo y contraseña válidos
    Entonces el servidor responde 201
    Y la cuenta se crea con rol USER
    Y la respuesta no incluye contraseña ni hash

  Escenario: Correo duplicado con otras mayúsculas
    Dado que existe la cuenta "ana@correo.com"
    Cuando un visitante se registra con "ANA@Correo.com"
    Entonces el servidor responde 409
    Y no se crea una segunda cuenta

  Escenario: Intento de asignarse ADMIN
    Dado un visitante que envía "role": "ADMIN" en el registro
    Cuando el servidor procesa el alta
    Entonces ignora el rol enviado
    Y la cuenta queda con rol USER

  Escenario: Datos inválidos
    Dado un visitante con correo sin formato válido, nombre fuera de rango o contraseña inválida
    Cuando envía el registro
    Entonces el servidor responde 422
    Y no se crea ninguna cuenta
```

```gherkin
Característica: HU-02 Inicio y cierre de sesión

  Escenario: Inicio de sesión válido
    Dado un USER registrado
    Cuando inicia sesión con sus credenciales
    Entonces el servidor responde 200 y emite una cookie de sesión HttpOnly
    Y GET /auth/me responde 200 con su identidad
    Y puede abrir "Mis subastas"

  Escenario: Contraseña incorrecta
    Dado un USER registrado
    Cuando inicia sesión con una contraseña incorrecta
    Entonces el servidor responde 401 con el mensaje genérico de credenciales

  Escenario: Correo inexistente
    Dado un correo que no está registrado
    Cuando intenta iniciar sesión
    Entonces el servidor responde 401 con el mismo mensaje que para contraseña incorrecta

  Escenario: Cierre de sesión
    Dado un usuario conectado
    Cuando cierra sesión
    Entonces el servidor responde 204 e invalida la sesión
    Y una ruta privada posterior responde 401
```

```gherkin
Característica: HU-03 Permisos por rol y propiedad

  Escenario: USER intenta entrar a una ruta de administración
    Dado un USER conectado
    Cuando invoca una ruta /admin
    Entonces obtiene 403

  Escenario: Sesión ausente en ruta privada
    Dado un visitante sin sesión
    Cuando consulta una ruta privada
    Entonces obtiene 401

  Escenario: Recurso de otro vendedor
    Dado una solicitud que pertenece a otro USER
    Cuando un USER distinto intenta editarla
    Entonces obtiene 403 sin datos del recurso en la respuesta
```

```gherkin
Característica: HU-04 Solicitud de publicación

  Escenario: Solicitud válida
    Dado un USER conectado
    Cuando envía título, descripción, categoría, precio, duración e imagen permitida
    Entonces el servidor responde 201
    Y la solicitud queda PENDIENTE_REVISION, ronda 1, sin fechas de inicio ni fin
    Y se registra el evento ENVIADA

  Escenario: La solicitud pendiente no es pública
    Dado una solicitud en PENDIENTE_REVISION
    Cuando se consulta el catálogo público
    Entonces la solicitud no aparece

  Escenario: Imagen inválida
    Dado un USER conectado
    Cuando envía una imagen de otro tipo o mayor a 3 MB
    Entonces el servidor responde 422 con código INVALID_IMAGE
    Y no se guarda la solicitud ni queda archivo temporal

  Escenario: Precio, duración o categoría fuera de regla
    Dado un USER conectado
    Cuando envía precio menor a COP 1.000 o no entero, una duración distinta de 24/48/72 o una categoría inexistente
    Entonces el servidor responde 422
    Y no hay persistencia parcial

  Escenario: Sin sesión
    Dado un visitante sin sesión
    Cuando intenta crear una solicitud
    Entonces el servidor responde 401
```

## 5. Textos de error acordados
Los códigos AUTH_REQUIRED, FORBIDDEN, AUCTION_NOT_PUBLIC e INVALID_IMAGE ya existen en el TRD. Los marcados con (*) son **propuesta nueva** para aprobar.

| Situación | HTTP | Código | Mensaje al usuario |
|---|---|---|---|
| Correo ya registrado | 409 | EMAIL_ALREADY_EXISTS (*) | Ya existe una cuenta con este correo. |
| Credenciales incorrectas (contraseña o correo inexistente) | 401 | INVALID_CREDENTIALS (*) | Correo o contraseña incorrectos. |
| Correo con formato inválido | 422 | VALIDATION_ERROR (*) | Escribe un correo válido, por ejemplo nombre@correo.com. |
| Nombre fuera de rango | 422 | VALIDATION_ERROR (*) | El nombre debe tener entre 2 y 100 caracteres. |
| Contraseña inválida | 422 | VALIDATION_ERROR (*) | La contraseña debe tener al menos 8 caracteres. (valor sujeto a H2) |
| Sin sesión | 401 | AUTH_REQUIRED | Debes iniciar sesión para continuar. |
| Sin permisos o recurso ajeno | 403 | FORBIDDEN | No tienes permisos para realizar esta acción. |
| Solicitud privada consultada por otro usuario | 404 | AUCTION_NOT_PUBLIC | Esta subasta no está disponible. |
| Título fuera de rango | 422 | VALIDATION_ERROR (*) | El título debe tener entre 5 y 120 caracteres. |
| Descripción fuera de rango | 422 | VALIDATION_ERROR (*) | La descripción debe tener entre 20 y 2000 caracteres. |
| Categoría inválida | 422 | VALIDATION_ERROR (*) | Selecciona una categoría válida. |
| Precio inválido | 422 | VALIDATION_ERROR (*) | El precio inicial debe ser un número entero de al menos COP 1.000. |
| Duración inválida | 422 | VALIDATION_ERROR (*) | Elige una duración de 24, 48 o 72 horas. |
| Imagen inválida | 422 | INVALID_IMAGE | La imagen debe ser JPG, PNG o WebP y pesar máximo 3 MB. |

## 6. Contratos mínimos
Solo el cuerpo de **registro** y los campos de **nueva solicitud** están definidos en el TRD. Lo demás es propuesta para validar con D05/D06 (backend) y D02/D04 (frontend).

```jsonc
// POST /api/v1/auth/register   (definido en el TRD)
{ "fullName": "Usuario Demo", "email": "usuario@example.test", "password": "********" }
// 201 (propuesto): { "id": 7, "fullName": "Usuario Demo", "email": "usuario@example.test", "role": "USER" }

// POST /api/v1/auth/login   (propuesto)
{ "email": "usuario@example.test", "password": "********" }
// 200: cookie de sesión HttpOnly + { "id": 7, "fullName": "Usuario Demo", "role": "USER" }

// GET /api/v1/auth/me   → 200 { "id", "fullName", "role" } | 401
// POST /api/v1/auth/logout → 204 | 401
// GET /api/v1/categories → 200 [ { "id": 1, "name": "Electrónica", "slug": "electronica" } ]

// POST /api/v1/auctions   multipart (campos definidos en el TRD)
// title, description, categoryId, startingPrice, durationHours, image
// 201 (propuesto): { "id": 42, "status": "PENDIENTE_REVISION", "reviewRound": 1, "contentVersion": 1 }

// Error estable (definido en el TRD; la forma exacta de "details" debe confirmarla el backend)
{ "code": "INVALID_IMAGE", "message": "…", "requestId": "req-123", "details": [ … ] }
```

## 7. Hallazgos y estado
| ID | Hallazgo | Prioridad | Estado |
|---|---|---|---|
| H1 | Duración 24/48/72, precio mínimo e imagen de 3 MB figuraban como propuestos; falta tope máximo de precio | P0 | [confirmar en acta G1-01] |
| H2 | Política de contraseña no definida en el SRS | P0 | El formulario aplica 8 a 100; [confirmar aprobación PO-01] |
| H3 | El alias público no existe en `users` (RN-16, HU-11, HU-14, PEND-06) | P0 | [decisión pendiente] |
| H4 | Login único o separado por rol | P0 | El frontend usa un solo `/login`; [confirmar con PO-01] |
| H5 | Forma exacta del campo `details` de los errores 422 | P1 | [confirmar con backend] |
| H6 | Cómo obtiene el token CSRF un visitante antes de registrarse o iniciar sesión | P1 | [confirmar con D05] |
| H7 | Escenario "editar recurso ajeno" de HU-03 depende de PATCH (Sprint 2) | P1 | Se traslada a TC-06 |
| H8 | "No aparece en catálogo" depende de GET /auctions (Sprint 2) | P1 | Se verifica con la consulta de control 1 |
| H9 | Datos de demo: 2 USER y 1 ADMIN sin claves fijas publicadas | P1 | [confirmar con D09] |
| H10 | El CHECK de correo en BD es mínimo; la validación completa va en Bean Validation | P1 | Aceptado |
| H11 | `maxlength` del nombre (150) mayor que el límite de BD (100) | P1 | [corregir en el frontend] |

## 8. Trazabilidad
RF-AUT-01 → HU-01 → TC-01 · RF-AUT-02 y RF-AUT-03 → HU-02 → TC-02 · RF-SEG-01 → HU-03 → TC-03 · RF-SUB-01 → HU-04 → TC-04.

## 9. Aprobación
| Rol | Nombre | Decisión | Fecha |
|---|---|---|---|
| PO-02 (revisión) | @Joseber32 | [ ] | [ ] |
| PO-01 (aprobación) | [nombre] | [ ] | [ ] |
