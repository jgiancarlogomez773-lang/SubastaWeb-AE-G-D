// Servicio de subastas. En T1-07 solo existe la versión simulada (mock contractual);
// T1-08 conecta GET /api/v1/categories y POST /api/v1/auctions (multipart/form-data).
const CATEGORIAS_MOCK = [
  { id: 1, name: 'Accesorios' },
  { id: 2, name: 'Deportes' },
  { id: 3, name: 'Fotografía' },
  { id: 4, name: 'Hogar' },
  { id: 5, name: 'Música' },
]

const pausa = (ms = 400) => new Promise((r) => setTimeout(r, ms))

export async function obtenerCategorias() {
  await pausa(150)
  return CATEGORIAS_MOCK
}

// Devuelve lo mismo que el contrato: la solicitud nace PENDIENTE_REVISION y no es pública.
export async function enviarSolicitud({ title, description, categoryId, startingPrice, durationHours }) {
  await pausa()
  return {
    id: Date.now(),
    title: title.trim(),
    description: description.trim(),
    categoryId: Number(categoryId),
    startingPrice: Number(startingPrice),
    durationHours: Number(durationHours),
    status: 'PENDIENTE_REVISION',
    createdAt: new Date().toISOString(),
  }
}
