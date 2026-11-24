// Decide si se puede entrar a una ruta según la sesión. Función pura para probarla sin navegador.
// Devuelve true (permitir) o el destino alternativo.
export function decidirAcceso(to, usuario) {
  const meta = to.meta || {}

  // Con sesión iniciada, login y registro no tienen sentido.
  if (usuario && (to.name === 'login' || to.name === 'registro')) return { name: 'catalogo' }

  if (meta.requiereSesion && !usuario) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }
  if (meta.rol && usuario && usuario.role !== meta.rol) {
    return { name: 'prohibido' }
  }
  return true
}

// Solo se aceptan redirecciones internas para evitar saltos a sitios externos.
export function destinoSeguro(redirect) {
  return typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//') ? redirect : '/'
}
