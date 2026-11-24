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

const CLAVE_REGISTRADOS = 'subastaweb.mock.registrados'
function leerRegistrados() {
  try {
    return JSON.parse(sessionStorage.getItem(CLAVE_REGISTRADOS)) || []
  } catch {
    return []
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
  registrar: (datos) => pedir('POST', '/auth/register', datos),
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
    const u = [...USUARIOS_MOCK, ...leerRegistrados()].find((x) => x.email === email.trim().toLowerCase() && x.password === password)
    if (!u) throw new ErrorApi(401, { code: 'AUTH_REQUIRED', message: 'Correo o contraseña incorrectos.' })
    const { password: _omitida, ...publico } = u
    sessionStorage.setItem(CLAVE, JSON.stringify(publico))
    return publico
  },
  logout: async () => {
    await pausa(100)
    sessionStorage.removeItem(CLAVE)
  },
  // Simula POST /auth/register: 201 con el usuario creado (siempre USER) o 409 si el correo existe.
  registrar: async ({ fullName, email, password }) => {
    await pausa()
    const correo = email.trim().toLowerCase()
    const registrados = leerRegistrados()
    const existe = [...USUARIOS_MOCK, ...registrados].some((x) => x.email === correo)
    if (existe) throw new ErrorApi(409, { code: 'EMAIL_ALREADY_EXISTS', message: 'Ya existe una cuenta con ese correo.' })
    const nuevo = { id: Date.now(), fullName: fullName.trim(), email: correo, password, role: 'USER' }
    sessionStorage.setItem(CLAVE_REGISTRADOS, JSON.stringify([...registrados, nuevo]))
    const { password: _omitida, ...publico } = nuevo
    return publico
  },
  usuarioActual: async () => leerMock(),
}

const servicio = usarMock ? mock : real
export const iniciarSesion = servicio.login
export const registrarUsuario = servicio.registrar
export const cerrarSesion = servicio.logout
export const obtenerUsuarioActual = servicio.usuarioActual
