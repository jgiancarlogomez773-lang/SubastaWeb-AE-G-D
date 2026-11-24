// Servicio de subastas. Con VITE_USAR_MOCK distinto de 'false' usa datos simulados;
// con 'false' consume GET /api/v1/categories y POST /api/v1/auctions (multipart/form-data).
import { pedir } from './api'
import { usarMock } from './auth'
import { construirFormulario } from './formularioSolicitud'

const CATEGORIAS_MOCK = [
  { id: 1, name: 'Accesorios' },
  { id: 2, name: 'Deportes' },
  { id: 3, name: 'Fotografía' },
  { id: 4, name: 'Hogar' },
  { id: 5, name: 'Música' },
]

const pausa = (ms = 400) => new Promise((r) => setTimeout(r, ms))

async function categoriasMock() {
  await pausa(150)
  return CATEGORIAS_MOCK
}

// Devuelve lo mismo que el contrato: la solicitud nace PENDIENTE_REVISION y no es pública.
async function enviarMock({ title, description, categoryId, startingPrice, durationHours }) {
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

// GET /categories (público) -> [{ id, name }]
const categoriasReal = () => pedir('GET', '/categories')

// POST /auctions -> 201 { id, title, ..., status: 'PENDIENTE_REVISION' }. Errores 401/403/422 como ErrorApi.
const enviarReal = (datos) => pedir('POST', '/auctions', construirFormulario(datos))

export const obtenerCategorias = usarMock ? categoriasMock : categoriasReal
export const enviarSolicitud = usarMock ? enviarMock : enviarReal
