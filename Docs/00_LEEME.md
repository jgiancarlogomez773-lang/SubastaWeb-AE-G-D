# SUBASTAWEB — Paquete v4 (documentación y planificación)

**Equipo definitivo:** 12 estudiantes: **2 producto (PO-01 responsable, PO-02 apoyo), 1 Scrum Master (SM-01), 9 Developers** (D01 Diseño, D02-D04 Frontend, D05-D07 Backend, D08-D09 Base de datos). **4 sprints, una semana cada uno.**

**Stack:** Vue 3 + Vite | Java + Spring Boot | PostgreSQL.

## Abre este archivo antes de leer otros

| Carpeta | Contenido vigente |
|---|---|
| `01_SRS` | `SRS_SubastaWeb_Completo_v4.docx` + Markdown: SRS consolidado |
| `02_PRD` | `PRD_SubastaWeb_v4.docx`: alcance, 26 RF, 14 RNF, 18 HU y criterios |
| `03_TRD` | `TRD_SubastaWeb_v4.docx`: arquitectura, contrato API, SQL, concurrencia y seguridad |
| `04_Historias_y_Tareas` | `01_PLAN_VIGENTE_v4_Tareas_Detalladas_y_Dependencias.docx` (72 fichas) + historias aparte; **aquí está además el último Word v3 anterior** |
| `05_Scrum` | plan equipo de 12 y capacidad por sprint |
| `06_Flujos` | 6 flujos de negocio, excepciones y datos; Word y MD |
| `07_Base_de_Datos` | esquema PostgreSQL V1, categorías V2, SQL de comprobación, diccionario Word y MD |
| `08_Diagramas` | 10 diagramas PNG/SVG y fuentes DOT/MMD editables; catálogo visual |
| `09_Anexos` | trazabilidad, auditoría DAG JSON, notas de cambios y manifiesto SHA256 |

## Importante: ubicación del documento que faltaba en el ZIP anterior

**Archivo anterior v3 incluido literalmente:**
`04_Historias_y_Tareas/00_ULTIMO_DOCUMENTO_ANTERIOR_v3_12_Estudiantes.docx`.

**Versión nueva y más detallada v4:**
`04_Historias_y_Tareas/01_PLAN_VIGENTE_v4_Tareas_Detalladas_y_Dependencias.docx`.

El primer archivo conserva la versión previa; el segundo es el documento operativo recomendado para asignar trabajo, controlar los prerequisitos y avanzar entre tareas. No mezclar versiones para asignaciones.

## Cómo iniciar el trabajo

1. PO-01 valida reglas y alcance del PRD, PO-02 termina criterios Gherkin.
2. SM-01 revisa dependencias y disponibilidad real de los nueve Developers.
3. D08-D09 ejecutan migraciones PostgreSQL V1/V2 en ambiente local; D05-D07 ajustan contrato Spring Boot.
4. D01-D04 alinean prototipos Vue y endpoints antes de implementar.
5. En cada Sprint Review se demuestra el incremento final y se registran evidencias de los RF Debe.

## Notas sobre calidad del entregable

Se validan documentalmente los archivos, diagramas, referencias de dependencias, formatos Word y contenido del comprimido; **no se afirma que los scripts SQL hayan sido ejecutados en un servidor ni que exista una plataforma desplegada**. Las horas son hipótesis hasta que los estudiantes confirmen disponibilidad.
