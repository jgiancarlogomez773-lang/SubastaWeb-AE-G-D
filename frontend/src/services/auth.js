// Servicio de autenticación. Con VITE_USAR_MOCK distinto de 'false' usa datos
// simulados que respetan el contrato; T1-06 conecta la sesión real.
import { pedir, olvidarCsrf, ErrorApi } from './api'

export const usarMock = import.meta.env.VITE_USAR_MOCK !== 'false'

const USUARIOS_MOCK = [
  { id: 1, fullName: 'Usuario Demo', email: 'usuario@example.test', password: 'Demo1234', role: 'USER' },
  { id: 2, fullName: 'Admin Demo', email: 'admin@example.test', password: 'Admin1234', role: 'ADMIN' },
]
const CLAVE = 'subastaweb.mock.sesion'

function leerMock() {
  try {
    return JSON.parse(sessionStorage.getItem(CLAVE))
  } catch {
    return null
  }
}

const pausa = (ms = 300) => new Promise((r) => setTimeout(r, ms))

const real = {
  login: async (email, password) => {
    const u = await pedir('POST', '/auth/login', { email, password })
    olvidarCsrf()
    return u
  },
  logout: async () => {
    await pedir('POST', '/auth/logout')
    olvidarCsrf()
  },
  usuarioActual: async () => {
    try {
      return await pedir('GET', '/auth/me')
    } catch (e) {
      if (e instanceof ErrorApi && e.status === 401) return null
      throw e
    }
  },
}

const mock = {
  login: async (email, password) => {
    await pausa()
    const u = USUARIOS_MOCK.find((x) => x.email === email.trim().toLowerCase() && x.password === password)
    if (!u) throw new ErrorApi(401, { code: 'AUTH_REQUIRED', message: 'Correo o contraseña incorrectos.' })
    const { password: _omitida, ...publico } = u
    sessionStorage.setItem(CLAVE, JSON.stringify(publico))
    return publico
  },
  logout: async () => {
    await pausa(100)
    sessionStorage.removeItem(CLAVE)
  },
  usuarioActual: async () => leerMock(),
}

const servicio = usarMock ? mock : real
export const iniciarSesion = servicio.login
export const cerrarSesion = servicio.logout
export const obtenerUsuarioActual = servicio.usuarioActual
