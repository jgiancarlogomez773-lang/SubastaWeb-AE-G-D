import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/base.css'
import { registrarSesionExpirada } from './services/api'
import { sesion } from './stores/sesion'

// Si el servidor responde 401 a una acción autenticada, se limpia la sesión y se pide iniciar sesión de nuevo.
registrarSesionExpirada(() => {
  sesion.usuario = null
  const actual = router.currentRoute.value
  if (actual.name !== 'login') router.push({ name: 'login', query: { redirect: actual.fullPath } })
})

createApp(App).use(router).mount('#app')
