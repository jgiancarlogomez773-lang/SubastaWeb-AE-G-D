import test from 'node:test'
import assert from 'node:assert/strict'
import { pedir, ErrorApi, registrarSesionExpirada } from '../src/services/api.js'

function simular(status, cuerpo) {
  const llamadas = []
  globalThis.fetch = async (url, opciones) => {
    llamadas.push({ url, opciones })
    return { status, ok: status >= 200 && status < 300, json: async () => cuerpo }
  }
  return llamadas
}

test('GET no manda cuerpo ni token CSRF y usa cookie de sesión', async () => {
  const llamadas = simular(200, [{ id: 1 }])
  const r = await pedir('GET', '/categories')
  assert.deepEqual(r, [{ id: 1 }])
  assert.equal(llamadas.length, 1)
  assert.equal(llamadas[0].url, '/api/v1/categories')
  assert.equal(llamadas[0].opciones.credentials, 'same-origin')
  assert.equal(llamadas[0].opciones.body, undefined)
})
test('POST JSON envía Content-Type y cuerpo', async () => {
  const llamadas = simular(200, { id: 1 })
  await pedir('POST', '/auth/login', { email: 'a@b.co', password: 'x' })
  assert.equal(llamadas[0].opciones.headers['Content-Type'], 'application/json')
  assert.equal(llamadas[0].opciones.body, JSON.stringify({ email: 'a@b.co', password: 'x' }))
})
test('FormData se envía sin Content-Type manual (multipart)', async () => {
  const llamadas = simular(201, { id: 5 })
  const fd = new FormData()
  fd.append('title', 'x')
  await pedir('POST', '/auctions', fd)
  assert.equal(llamadas[0].opciones.body, fd)
  assert.equal(llamadas[0].opciones.headers['Content-Type'], undefined)
})
test('204 devuelve null', async () => {
  simular(204, null)
  assert.equal(await pedir('POST', '/auth/logout'), null)
})
test('error 422 conserva code, message y details', async () => {
  simular(422, { code: 'VALIDATION_ERROR', message: 'Los datos enviados no son válidos.', details: [{ field: 'email', message: 'mal' }] })
  await assert.rejects(pedir('POST', '/auth/register', {}), (e) => {
    assert.ok(e instanceof ErrorApi)
    assert.equal(e.status, 422)
    assert.equal(e.code, 'VALIDATION_ERROR')
    assert.deepEqual(e.details, [{ field: 'email', message: 'mal' }])
    return true
  })
})
test('401 en una acción autenticada avisa sesión expirada; en login o /me no', async () => {
  let avisos = 0
  registrarSesionExpirada(() => avisos++)
  simular(401, { code: 'UNAUTHORIZED', message: 'No autenticado' })
  await assert.rejects(pedir('POST', '/auctions', new FormData()))
  assert.equal(avisos, 1)
  await assert.rejects(pedir('POST', '/auth/login', {}))
  await assert.rejects(pedir('GET', '/auth/me'))
  assert.equal(avisos, 1)
})
