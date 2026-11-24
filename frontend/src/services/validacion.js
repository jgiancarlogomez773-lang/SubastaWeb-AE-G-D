// Validaciones del registro, alineadas con el backend (RegisterRequest) y contratos/sprint-1.md.
export function validarRegistro({ fullName, email, password, confirmacion }) {
  const errores = {}
  const nombre = (fullName || '').trim()
  const correo = (email || '').trim()

  if (!nombre) errores.fullName = 'El nombre completo es obligatorio.'
  else if (nombre.length > 150) errores.fullName = 'El nombre completo no puede superar 150 caracteres.'

  if (!correo) errores.email = 'El correo es obligatorio.'
  else if (!/^\S+@\S+\.\S+$/.test(correo)) errores.email = 'El formato del correo no es válido.'
  else if (correo.length > 255) errores.email = 'El correo no puede superar 255 caracteres.'

  if (!password) errores.password = 'La contraseña es obligatoria.'
  else if (password.length < 8 || password.length > 100) errores.password = 'La contraseña debe tener entre 8 y 100 caracteres.'

  if (!errores.password && password !== confirmacion) errores.confirmacion = 'Las contraseñas no coinciden.'

  return errores
}

// Convierte details del 422 ([{ field, message }]) en errores por campo del formulario.
export function erroresDeServidor(details) {
  const errores = {}
  for (const d of details || []) {
    if (d && d.field && !errores[d.field]) errores[d.field] = d.message || d.issue
  }
  return errores
}
