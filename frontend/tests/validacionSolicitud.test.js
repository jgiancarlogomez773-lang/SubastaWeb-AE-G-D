import test from 'node:test'
import assert from 'node:assert/strict'
import { validarSolicitud, validarImagen, MAX_IMAGEN_BYTES } from '../src/services/validacionSolicitud.js'

const imagen = { type: 'image/png', size: 1000 }
const valido = {
  title: 'Bicicleta de ruta',
  description: 'Bicicleta de aluminio talla M, 18 velocidades.',
  categoryId: 2,
  startingPrice: 20000,
  durationHours: 48,
  image: imagen,
}

test('solicitud válida no tiene errores', () => {
  assert.deepEqual(validarSolicitud(valido), {})
})
test('título y descripción fuera de rango', () => {
  assert.ok(validarSolicitud({ ...valido, title: 'abc' }).title)
  assert.ok(validarSolicitud({ ...valido, title: 'a'.repeat(121) }).title)
  assert.ok(validarSolicitud({ ...valido, description: 'muy corta' }).description)
  assert.ok(validarSolicitud({ ...valido, description: 'a'.repeat(2001) }).description)
})
test('categoría obligatoria', () => {
  assert.ok(validarSolicitud({ ...valido, categoryId: '' }).categoryId)
})
test('precio: mínimo, entero y máximo (RN-07)', () => {
  assert.ok(validarSolicitud({ ...valido, startingPrice: 999 }).startingPrice)
  assert.equal(validarSolicitud({ ...valido, startingPrice: 1000 }).startingPrice, undefined)
  assert.ok(validarSolicitud({ ...valido, startingPrice: 1500.5 }).startingPrice)
  assert.ok(validarSolicitud({ ...valido, startingPrice: '' }).startingPrice)
  assert.ok(validarSolicitud({ ...valido, startingPrice: 1_000_000_001 }).startingPrice)
})
test('duración solo 24, 48 o 72', () => {
  assert.ok(validarSolicitud({ ...valido, durationHours: 12 }).durationHours)
  assert.equal(validarSolicitud({ ...valido, durationHours: 72 }).durationHours, undefined)
})
test('imagen: obligatoria, tipo y tamaño (RN-15)', () => {
  assert.ok(validarImagen(null))
  assert.ok(validarImagen({ type: 'image/gif', size: 10 }))
  assert.ok(validarImagen({ type: 'application/pdf', size: 10 }))
  assert.ok(validarImagen({ type: 'image/jpeg', size: MAX_IMAGEN_BYTES + 1 }))
  assert.equal(validarImagen({ type: 'image/webp', size: MAX_IMAGEN_BYTES }), '')
})
