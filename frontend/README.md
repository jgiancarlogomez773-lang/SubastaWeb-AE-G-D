# Frontend — Vue 3 + Vite

Responsables: D02, D03, D04 (diseño: D01).

Estructura prevista: `src/views`, `src/components`, `src/services`, `router`.
Las llamadas a la API usan el proxy `/api` del servidor de desarrollo (mismo origen en la demo).
Contratos de cada sprint: ver `../contratos/`.

## Ejecutar

```bash
cd frontend
npm install
npm run dev     # http://localhost:5173
npm test        # pruebas de guardas, validaciones y cliente de API
npm run build
```

### Datos simulados o API real

Por defecto el frontend usa datos simulados (mock contractual). Para usar la API real:

1. Levanta el backend en `http://localhost:8080` (ver `../backend`).
2. Crea `frontend/.env.local` con `VITE_USAR_MOCK=false` (hay un ejemplo en `.env.example`).
3. Reinicia `npm run dev`. El proxy de Vite envía `/api` al backend, así que la cookie de sesión funciona en el mismo origen.

El backend no usa token CSRF: la sesión es una cookie HttpOnly SameSite=Lax.
