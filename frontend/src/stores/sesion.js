import { reactive } from 'vue'
import { iniciarSesion, cerrarSesion, obtenerUsuarioActual } from '../services/auth'

// Estado global mínimo de la sesión (sin librerías adicionales).
export const sesion = reactive({ usuario: null, cargada: false })

let pendiente = null

// Consulta una sola vez la sesión del servidor al abrir la app.
export function cargarSesion() {
  if (sesion.cargada) return Promise.resolve(sesion.usuario)
  if (!pendiente) {
    pendiente = obtenerUsuarioActual()
      .then((u) => (sesion.usuario = u))
      .catch(() => (sesion.usuario = null))
      .finally(() => {
        sesion.cargada = true
        pendiente = null
      })
  }
  return pendiente
}

export async function entrar(email, password) {
  sesion.usuario = await iniciarSesion(email, password)
  sesion.cargada = true
  return sesion.usuario
}

export async function salir() {
  try {
    await cerrarSesion()
  } finally {
    sesion.usuario = null
  }
}
