// Validaciones de "Publicar artículo", alineadas con CreateAuctionForm y AuctionRules del backend
// y con las reglas RN-07 y RN-15 de contratos/sprint-1.md.
export const PRECIO_MINIMO = 1000
export const PRECIO_MAXIMO = 1_000_000_000
export const DURACIONES = [24, 48, 72]
export const TIPOS_IMAGEN = ['image/jpeg', 'image/png', 'image/webp']
export const MAX_IMAGEN_BYTES = 3 * 1024 * 1024

export function validarImagen(archivo) {
  if (!archivo) return 'Debes adjuntar una imagen.'
  if (!TIPOS_IMAGEN.includes(archivo.type)) return 'La imagen debe ser JPG, PNG o WebP.'
  if (archivo.size > MAX_IMAGEN_BYTES) return 'La imagen supera el máximo de 3 MB.'
  return ''
}

export function validarSolicitud({ title, description, categoryId, startingPrice, durationHours, image }) {
  const errores = {}
  const titulo = (title || '').trim()
  const descripcion = (description || '').trim()

  if (!titulo) errores.title = 'El título es obligatorio.'
  else if (titulo.length < 5 || titulo.length > 120) errores.title = 'El título debe tener entre 5 y 120 caracteres.'

  if (!descripcion) errores.description = 'La descripción es obligatoria.'
  else if (descripcion.length < 20 || descripcion.length > 2000) errores.description = 'La descripción debe tener entre 20 y 2000 caracteres.'

  if (!categoryId) errores.categoryId = 'La categoría es obligatoria.'

  const precio = Number(startingPrice)
  if (startingPrice === '' || startingPrice === null || startingPrice === undefined) errores.startingPrice = 'El precio inicial es obligatorio.'
  else if (!Number.isInteger(precio)) errores.startingPrice = 'El precio debe ser un número entero en COP.'
  else if (precio < PRECIO_MINIMO) errores.startingPrice = 'El precio inicial mínimo es COP 1.000.'
  else if (precio > PRECIO_MAXIMO) errores.startingPrice = 'El precio inicial supera el máximo permitido.'

  if (!DURACIONES.includes(Number(durationHours))) errores.durationHours = 'Elige una duración de 24, 48 o 72 horas.'

  const errorImagen = validarImagen(image)
  if (errorImagen) errores.image = errorImagen

  return errores
}
