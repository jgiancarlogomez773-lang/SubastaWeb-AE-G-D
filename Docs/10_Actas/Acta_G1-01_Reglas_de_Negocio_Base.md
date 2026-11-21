# Acta de decisiones — G1-01 Definir reglas de negocio base

**Proyecto:** SubastaWeb · **Sprint:** 1 · **Tarea:** G1-01 · **Responsable:** PO-01 · **Estimación:** 2 h
**Documentos base:** SRS-SW-004 v4.0 (RN-01 a RN-16, RF-AUT-01, RF-SUB-01) y PLAN-SW-004 v4.0
**Alcance confirmado:** HU-01 Registro, HU-02 Sesión, HU-03 Permisos por rol y propiedad, HU-04 Solicitud de publicación

> Esta acta deja por escrito los parámetros que el SRS marcaba como "propuestos". PO-01 confirma que ya estaban aprobados; el acta los registra como base para el Sprint 1. Registro: 21/11/2026.

## 1. Decisiones

| # | Tema | Decisión | Regla / fuente |
| --- | --- | --- | --- |
| 1 | Rol al registrarse | Todo registro público recibe **solo el rol USER**. El ADMIN se crea por un procedimiento controlado, nunca por un endpoint público. | RN-01 |
| 2 | Campos mínimos de registro | **Nombre completo, correo y contraseña.** La contraseña se guarda solo como hash. | RF-AUT-01 |
| 3 | Rechazo de duplicados | Un **correo ya registrado se rechaza** (respuesta 409). | RF-AUT-01 |
| 4 | Estado inicial de una solicitud | Nace en **PENDIENTE_REVISION** y no es visible en el catálogo público. | RN-02 |
| 5 | Campos de la solicitud | Título, descripción, categoría, precio inicial, duración e imagen. | RF-SUB-01 |
| 6 | Precio inicial | Desde **COP 1.000**, entero positivo, sin decimales. | RN-07 |
| 7 | Duración de la subasta | **24, 48 o 72 horas.** | RN-06 |
| 8 | Imagen | **Una sola** por solicitud, formato **JPG, PNG o WebP**, de hasta **3 MB**. | RN-15 |

## 2. Qué desbloquea esta acta

T1-01 (prototipos), T1-09 (registro en backend), T1-11 (contrato de solicitud), T1-15 (migración de usuarios y categorías), G1-02 (criterios de PO-02) y G1-03 (objetivo del sprint de SM-01).

## 3. Salida verificable

- [x] Decisiones documentadas en esta acta.
- [ ] Enlace al acta agregado como evidencia en la tarjeta de G1-01 del tablero.
- [ ] PO-02 informado para revisar los criterios de aceptación (G1-02).

## 4. Aprobación

| Rol | Responsable | Fecha | Aprobado |
| --- | --- | --- | --- |
| Product Owner | PO-01 | | [ ] |
