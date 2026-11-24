import test from 'node:test'
import assert from 'node:assert/strict'
import { decidirAcceso, destinoSeguro } from '../src/router/guardas.js'

const user = { role: 'USER' }
const admin = { role: 'ADMIN' }

test('ruta pública permite a visitantes', () => {
  assert.equal(decidirAcceso({ name: 'catalogo', meta: {} }, null), true)
})
test('ruta privada sin sesión redirige a login conservando destino', () => {
  const r = decidirAcceso({ name: 'publicar', fullPath: '/publicar', meta: { requiereSesion: true } }, null)
  assert.deepEqual(r, { name: 'login', query: { redirect: '/publicar' } })
})
test('ruta privada con sesión permite', () => {
  assert.equal(decidirAcceso({ name: 'perfil', meta: { requiereSesion: true } }, user), true)
})
test('USER en ruta ADMIN va a 403', () => {
  const r = decidirAcceso({ name: 'admin', meta: { requiereSesion: true, rol: 'ADMIN' } }, user)
  assert.deepEqual(r, { name: 'prohibido' })
})
test('ADMIN en ruta ADMIN permite', () => {
  assert.equal(decidirAcceso({ name: 'admin', meta: { requiereSesion: true, rol: 'ADMIN' } }, admin), true)
})
test('con sesión, login y registro redirigen al catálogo', () => {
  assert.deepEqual(decidirAcceso({ name: 'login', meta: {} }, user), { name: 'catalogo' })
  assert.deepEqual(decidirAcceso({ name: 'registro', meta: {} }, user), { name: 'catalogo' })
})
test('destinoSeguro rechaza redirecciones externas', () => {
  assert.equal(destinoSeguro('/perfil'), '/perfil')
  assert.equal(destinoSeguro('//evil.com'), '/')
  assert.equal(destinoSeguro('https://evil.com'), '/')
  assert.equal(destinoSeguro(undefined), '/')
})
test('ADMIN en ruta de USER (publicar) va a 403', () => {
  const r = decidirAcceso({ name: 'publicar', meta: { requiereSesion: true, rol: 'USER' } }, admin)
  assert.deepEqual(r, { name: 'prohibido' })
})
