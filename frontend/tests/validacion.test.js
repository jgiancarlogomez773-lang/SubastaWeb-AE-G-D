import test from 'node:test'
import assert from 'node:assert/strict'
import { validarRegistro, erroresDeServidor } from '../src/services/validacion.js'

const valido = { fullName: 'Ana Pérez', email: 'ana@example.test', password: 'Clave1234', confirmacion: 'Clave1234' }

test('datos válidos no producen errores', () => {
  assert.deepEqual(validarRegistro(valido), {})
})
test('campos vacíos son obligatorios', () => {
  const e = validarRegistro({ fullName: ' ', email: '', password: '', confirmacion: '' })
  assert.ok(e.fullName && e.email && e.password)
})
test('correo con formato inválido', () => {
  assert.ok(validarRegistro({ ...valido, email: 'ana@' }).email)
})
test('contraseña corta o larga', () => {
  assert.ok(validarRegistro({ ...valido, password: '1234567', confirmacion: '1234567' }).password)
  const larga = 'a'.repeat(101)
  assert.ok(validarRegistro({ ...valido, password: larga, confirmacion: larga }).password)
})
test('confirmación distinta', () => {
  assert.ok(validarRegistro({ ...valido, confirmacion: 'otra' }).confirmacion)
})
test('nombre de más de 150 caracteres', () => {
  assert.ok(validarRegistro({ ...valido, fullName: 'a'.repeat(151) }).fullName)
})
test('erroresDeServidor mapea details del 422', () => {
  const e = erroresDeServidor([{ field: 'email', issue: 'mal' }, { field: 'email', issue: 'otro' }, { field: 'password', issue: 'corta' }])
  assert.deepEqual(e, { email: 'mal', password: 'corta' })
  assert.deepEqual(erroresDeServidor(undefined), {})
})
