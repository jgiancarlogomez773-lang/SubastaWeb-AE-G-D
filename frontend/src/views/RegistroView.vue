<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { registrarUsuario, usarMock } from '../services/auth'
import { validarRegistro, erroresDeServidor } from '../services/validacion'

const router = useRouter()

const form = reactive({ fullName: '', email: '', password: '', confirmacion: '' })
const errores = ref({})
const error = ref('')
const cargando = ref(false)
const intentado = ref(false)

async function enviar() {
  // Bloquea el doble envío mientras hay una petición en curso.
  if (cargando.value) return
  intentado.value = true
  error.value = ''
  errores.value = validarRegistro(form)
  if (Object.keys(errores.value).length) return

  cargando.value = true
  try {
    await registrarUsuario({ fullName: form.fullName.trim(), email: form.email.trim(), password: form.password })
    router.replace({ name: 'login', query: { registrado: '1' } })
  } catch (e) {
    if (e.status === 409) {
      errores.value = { email: 'Ya existe una cuenta con ese correo. Inicia sesión o usa otro correo.' }
    } else if (e.status === 422) {
      errores.value = erroresDeServidor(e.details)
      if (!Object.keys(errores.value).length) error.value = e.message
    } else {
      error.value = 'No pudimos crear la cuenta. Inténtalo de nuevo en unos minutos.'
    }
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <section>
    <h1>Crear cuenta</h1>
    <p class="subtitulo">Regístrate para publicar artículos y pujar.</p>

    <p v-if="error" class="error" role="alert">{{ error }}</p>

    <form novalidate @submit.prevent="enviar">
      <label for="fullName">Nombre completo</label>
      <input id="fullName" v-model="form.fullName" type="text" autocomplete="name" maxlength="150" :aria-invalid="!!errores.fullName" />
      <small v-if="errores.fullName" class="campo-error">{{ errores.fullName }}</small>

      <label for="email">Correo electrónico</label>
      <input id="email" v-model="form.email" type="email" autocomplete="email" :aria-invalid="!!errores.email" />
      <small v-if="errores.email" class="campo-error">{{ errores.email }}</small>

      <label for="password">Contraseña</label>
      <input id="password" v-model="form.password" type="password" autocomplete="new-password" :aria-invalid="!!errores.password" />
      <small v-if="errores.password" class="campo-error">{{ errores.password }}</small>
      <small v-else class="ayuda">Entre 8 y 100 caracteres.</small>

      <label for="confirmacion">Confirmar contraseña</label>
      <input id="confirmacion" v-model="form.confirmacion" type="password" autocomplete="new-password" :aria-invalid="!!errores.confirmacion" />
      <small v-if="errores.confirmacion" class="campo-error">{{ errores.confirmacion }}</small>

      <button type="submit" :disabled="cargando">{{ cargando ? 'Creando cuenta…' : 'Crear cuenta' }}</button>
    </form>

    <p class="pie">¿Ya tienes cuenta? <router-link to="/login">Iniciar sesión</router-link></p>
    <p v-if="usarMock" class="pie demo">Modo demo: la cuenta se guarda solo en esta pestaña.</p>
  </section>
</template>

<style scoped>
form { display: flex; flex-direction: column; gap: 0.4rem; }
label { font-weight: 600; margin-top: 0.5rem; }
input { padding: 0.6rem 0.7rem; border: 1px solid var(--borde); border-radius: 8px; font: inherit; }
input[aria-invalid='true'] { border-color: #b91c1c; }
button { margin-top: 1rem; padding: 0.7rem; border: 0; border-radius: 8px; background: var(--primario); color: #fff; font: inherit; font-weight: 600; cursor: pointer; }
button:disabled { opacity: 0.6; cursor: wait; }
.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; border-radius: 8px; padding: 0.6rem 0.8rem; }
.campo-error { color: #b91c1c; }
.ayuda, .pie { color: var(--suave); }
.demo { font-size: 0.8rem; }
</style>
