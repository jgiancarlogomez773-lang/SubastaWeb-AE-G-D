// Cliente HTTP mínimo para la API (prefijo /api/v1, sesión por cookie).
// Contrato: contratos/sprint-1.md. Errores: { code, message, requestId, details }.

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

// Spring Security exige el token CSRF en las peticiones que modifican datos.
let csrf = null
async function obtenerCsrf(forzar = false) {
  if (csrf && !forzar) return csrf
  const resp = await fetch(`${BASE}/auth/csrf`, { credentials: 'same-origin' })
  if (!resp.ok) throw new ErrorApi(resp.status, null)
  csrf = await resp.json() // { headerName, token }
  return csrf
}

export async function pedir(metodo, ruta, cuerpo) {
  const cabeceras = { Accept: 'application/json' }
  const opciones = { method: metodo, headers: cabeceras, credentials: 'same-origin' }
  if (cuerpo !== undefined) {
    cabeceras['Content-Type'] = 'application/json'
    opciones.body = JSON.stringify(cuerpo)
  }
  if (metodo !== 'GET') {
    const t = await obtenerCsrf()
    cabeceras[t.headerName] = t.token
  }
  const resp = await fetch(BASE + ruta, opciones)
  if (resp.status === 204) return null
  const datos = await resp.json().catch(() => null)
  if (!resp.ok) throw new ErrorApi(resp.status, datos)
  return datos
}

// Tras login/logout la sesión cambia y el token CSRF anterior deja de servir.
export function olvidarCsrf() {
  csrf = null
}
