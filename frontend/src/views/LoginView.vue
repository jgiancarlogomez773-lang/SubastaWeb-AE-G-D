<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { entrar } from '../stores/sesion'
import { destinoSeguro } from '../router/guardas'
import { usarMock } from '../services/auth'

const route = useRoute()
const router = useRouter()

const email = ref('')
const password = ref('')
const cargando = ref(false)
const error = ref('')
const tocado = ref(false)

const errorEmail = computed(() => {
  if (!email.value.trim()) return 'El correo es obligatorio.'
  if (!/^\S+@\S+\.\S+$/.test(email.value.trim())) return 'El formato del correo no es válido.'
  return ''
})
const errorPassword = computed(() => (password.value ? '' : 'La contraseña es obligatoria.'))

async function enviar() {
  tocado.value = true
  error.value = ''
  if (errorEmail.value || errorPassword.value) return
  cargando.value = true
  try {
    await entrar(email.value.trim(), password.value)
    router.replace(destinoSeguro(route.query.redirect))
  } catch (e) {
    error.value =
      e.status === 401
        ? 'Correo o contraseña incorrectos. Revisa tus datos e inténtalo de nuevo.'
        : 'No pudimos iniciar sesión. Inténtalo de nuevo en unos minutos.'
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <section>
    <h1>Iniciar sesión</h1>
    <p class="subtitulo">Entra para publicar artículos y pujar.</p>

    <p v-if="route.query.redirect && !error" class="aviso" role="status">Inicia sesión para continuar.</p>
    <p v-if="error" class="error" role="alert">{{ error }}</p>

    <form novalidate @submit.prevent="enviar">
      <label for="email">Correo electrónico</label>
      <input id="email" v-model="email" type="email" autocomplete="username" :aria-invalid="tocado && !!errorEmail" />
      <small v-if="tocado && errorEmail" class="campo-error">{{ errorEmail }}</small>

      <label for="password">Contraseña</label>
      <input id="password" v-model="password" type="password" autocomplete="current-password" :aria-invalid="tocado && !!errorPassword" />
      <small v-if="tocado && errorPassword" class="campo-error">{{ errorPassword }}</small>

      <button type="submit" :disabled="cargando">{{ cargando ? 'Ingresando…' : 'Iniciar sesión' }}</button>
    </form>

    <p class="pie">¿No tienes cuenta? <router-link to="/registro">Crear cuenta</router-link></p>
    <p v-if="usarMock" class="pie demo">Modo demo: usuario@example.test / Demo1234 · admin@example.test / Admin1234</p>
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
.aviso { background: #eff6ff; color: #1e40af; border-radius: 8px; padding: 0.6rem 0.8rem; }
.campo-error { color: #b91c1c; }
.pie { color: var(--suave); }
.demo { font-size: 0.8rem; }
</style>
