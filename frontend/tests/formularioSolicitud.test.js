import test from 'node:test'
import assert from 'node:assert/strict'
import { construirFormulario } from '../src/services/formularioSolicitud.js'

test('construye el multipart con los seis campos del contrato', () => {
  const imagen = new File([new Uint8Array([1, 2, 3])], 'foto.png', { type: 'image/png' })
  const fd = construirFormulario({
    title: '  Bicicleta de ruta ',
    description: ' Bicicleta de aluminio talla M. ',
    categoryId: 2,
    startingPrice: 20000,
    durationHours: 48,
    image: imagen,
  })
  assert.equal(fd.get('title'), 'Bicicleta de ruta')
  assert.equal(fd.get('description'), 'Bicicleta de aluminio talla M.')
  assert.equal(fd.get('categoryId'), '2')
  assert.equal(fd.get('startingPrice'), '20000')
  assert.equal(fd.get('durationHours'), '48')
  assert.equal(fd.get('image').name, 'foto.png')
  assert.deepEqual([...fd.keys()].sort(), ['categoryId', 'description', 'durationHours', 'image', 'startingPrice', 'title'])
})
