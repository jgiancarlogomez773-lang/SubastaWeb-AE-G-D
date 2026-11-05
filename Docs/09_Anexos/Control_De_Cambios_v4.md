# Cambios, decisiones y límites del paquete v4

**Proyecto:** SubastaWeb · **ID:** CONTROL-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

## Qué se corrigió

El plan v3 de tareas no debe perderse: **se incluye el Word original sin cambios de contenido** como `04_Historias_y_Tareas/00_ULTIMO_DOCUMENTO_ANTERIOR_v3_12_Estudiantes.docx`. Su SHA256 se conserva en el archivo de auditoría.

Ahora existe el **Plan vigente v4** con fichas de las 72 tareas (predecesoras, responsable, área, RF, HU, día, horas, condiciones de inicio, actividad paso a paso, criterios de salida y tareas posteriores). Los documentos SRS/PRD/TRD/Scrum ya describen **12 personas y Vue 3 + Spring Boot + PostgreSQL**, en vez de conservar una contradicción con la antigua versión de 13.

## Base de datos: mejoras

- Siete tablas, desde cinco anteriores, separando metadatos de imágenes y eventos auditables.
- Índices parciales para catálogo, cola pendiente y vencimientos.
- FK compuesta de ganador al par `(auction_id,id)` de ofertas.
- Montos exactos COP enteros, controles de fecha, versión, ronda, rol y checks de estado.
- Transacciones del backend para autopuja, hora, monto mínimo, una decisión ADMIN por ronda y cierre idempotente.
- Dos migraciones Flyway separadas, consultas de control y diccionario completo.

## Pruebas documentales realizadas

- Se verificaron los 72 códigos técnicos, 12 G, dependencias existentes, orden entre sprints, grafos sin ciclos y carga 8 h técnicas por Developer por sprint.
- Se verificaron las 18 HU, 26 RF, 14 RNF y correspondencia de tareas RF Debe.
- Se generaron diez diagramas vectoriales y raster desde fuentes editables.
- El código SQL se revisó estáticamente; **no se declara ejecutado en una instancia PostgreSQL**, y la aplicación no está implementada.

## Decisiones pendientes de producto

Confirmar horas reales, versión exacta de herramientas, servidor de demo, política institucional de datos personales, duración de subasta, monto inicial e incremento, mecanismo de almacenamiento y visibilidad pública de alias. Estas condiciones no se presentan como aprobadas.
