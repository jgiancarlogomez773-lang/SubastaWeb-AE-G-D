# Catálogo de diagramas y flujos del sistema

**Proyecto:** SubastaWeb · **ID:** DIAGRAMA-SW-004 · **Versión:** 4.0 · **Fecha:** 30/10/2026

**Equipo:** 12 estudiantes: PO-01, PO-02, SM-01 y D01-D09. **Stack:** Vue 3 + Vite, Java + Spring Boot y PostgreSQL. **Duración:** 4 sprints de una semana.

**Estado:** Borrador técnico para validación por PO principal. 

Los diez diagramas se proporcionan como **PNG**, **SVG**, fuente **Graphviz DOT** y fuente **Mermaid MMD**. Para documentar cambios usar el archivo MMD, y regenerar imagen con Graphviz (DOT) cuando corresponda. La representación PNG es referencia visual, no sustituye las reglas textuales ni SQL.

## Modelo entidad-relacion fisico mejorado (7 tablas)

![Modelo entidad-relacion fisico mejorado (7 tablas)](08_Diagramas/01_Modelo_Entidad_Relacion.png)

**Fuentes editables:** `08_Diagramas/01_Modelo_Entidad_Relacion.mmd`, `08_Diagramas/01_Modelo_Entidad_Relacion.dot`.

## Arquitectura frontend, backend, base de datos y cierre

![Arquitectura frontend, backend, base de datos y cierre](08_Diagramas/02_Arquitectura_Componentes.png)

**Fuentes editables:** `08_Diagramas/02_Arquitectura_Componentes.mmd`, `08_Diagramas/02_Arquitectura_Componentes.dot`.

## Máquina de estados y transiciones permitidas

![Máquina de estados y transiciones permitidas](08_Diagramas/03_Estados_Subasta.png)

**Fuentes editables:** `08_Diagramas/03_Estados_Subasta.mmd`, `08_Diagramas/03_Estados_Subasta.dot`.

## Flujo de creación y solicitud privada

![Flujo de creación y solicitud privada](08_Diagramas/04_Solicitud.png)

**Fuentes editables:** `08_Diagramas/04_Solicitud.mmd`, `08_Diagramas/04_Solicitud.dot`.

## Aprobación o rechazo con transacción y auditoría

![Aprobación o rechazo con transacción y auditoría](08_Diagramas/05_Moderacion.png)

**Fuentes editables:** `08_Diagramas/05_Moderacion.mmd`, `08_Diagramas/05_Moderacion.dot`.

## Corrección y reenvío de publicación rechazada

![Corrección y reenvío de publicación rechazada](08_Diagramas/06_Reenvio.png)

**Fuentes editables:** `08_Diagramas/06_Reenvio.mmd`, `08_Diagramas/06_Reenvio.dot`.

## Flujo de oferta ascendente protegida contra concurrencia

![Flujo de oferta ascendente protegida contra concurrencia](08_Diagramas/07_Puja_Transaccional.png)

**Fuentes editables:** `08_Diagramas/07_Puja_Transaccional.mmd`, `08_Diagramas/07_Puja_Transaccional.dot`.

## Cierre automático idempotente y ganador

![Cierre automático idempotente y ganador](08_Diagramas/08_Cierre_Automatico.png)

**Fuentes editables:** `08_Diagramas/08_Cierre_Automatico.mmd`, `08_Diagramas/08_Cierre_Automatico.dot`.

## Secuencia API/BD de ofertas simultáneas

![Secuencia API/BD de ofertas simultáneas](08_Diagramas/09_Secuencia_Puja.png)

**Fuentes editables:** `08_Diagramas/09_Secuencia_Puja.mmd`, `08_Diagramas/09_Secuencia_Puja.dot`.

## Ruta crítica por sprint y compuertas de avance

![Ruta crítica por sprint y compuertas de avance](08_Diagramas/10_Ruta_Critica_Sprints.png)

**Fuentes editables:** `08_Diagramas/10_Ruta_Critica_Sprints.mmd`, `08_Diagramas/10_Ruta_Critica_Sprints.dot`.
