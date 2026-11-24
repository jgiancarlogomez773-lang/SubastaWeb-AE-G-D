// Cliente HTTP mínimo para la API (prefijo /api/v1, sesión por cookie HttpOnly SameSite=Lax).
// El backend no usa token CSRF: protege con SameSite y CORS restringido (SecurityConfig).
// Errores del contrato: { code, message, requestId, details: [{ field, message }] }.

const BASE = '/api/v1'

export class ErrorApi extends Error {
  constructor(status, cuerpo) {
    super(cuerpo?.message || 'No se pudo completar la operación')
    this.status = status
    this.code = cuerpo?.code || 'ERROR'
    this.details = cuerpo?.details || []
    this.requestId = cuerpo?.requestId
  }
}

// Aviso global cuando una petición autenticada responde 401 (sesión vencida o cerrada en otro lugar).
let alSesionExpirada = null
export function registrarSesionExpirada(fn) {
  alSesionExpirada = fn
}

// `cuerpo` puede ser un objeto (JSON) o un FormData (multipart: el navegador pone el Content-Type).
export async function pedir(metodo, ruta, cuerpo) {
  const cabeceras = { Accept: 'application/json' }
  const opciones = { method: metodo, headers: cabeceras, credentials: 'same-origin' }
  if (cuerpo instanceof FormData) {
    opciones.body = cuerpo
  } else if (cuerpo !== undefined) {
    cabeceras['Content-Type'] = 'application/json'
    opciones.body = JSON.stringify(cuerpo)
  }

  const resp = await fetch(BASE + ruta, opciones)
  if (resp.status === 204) return null
  const datos = await resp.json().catch(() => null)
  if (!resp.ok) {
    // Un 401 en login significa credenciales malas, no sesión vencida; /auth/me lo maneja el llamador.
    const esAcceso = ruta === '/auth/login' || ruta === '/auth/me'
    if (resp.status === 401 && !esAcceso && alSesionExpirada) alSesionExpirada()
    throw new ErrorApi(resp.status, datos)
  }
  return datos
}
