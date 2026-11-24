<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { sesion, salir } from '../stores/sesion'

const router = useRouter()
// Layout reutilizable con barra de navegación (según los mockups).
// T1-05/T1-06 mostrarán u ocultarán enlaces según la sesión y el rol.
const base = [
  { to: '/', texto: 'Catálogo' },
  { to: '/mis-subastas', texto: 'Mis subastas' },
  { to: '/mis-ofertas', texto: 'Mis ofertas' },
  { to: '/perfil', texto: 'Mi perfil' },
  { to: '/publicar', texto: 'Publicar artículo' },
]
// Visitantes ven solo el catálogo; usuarios con sesión ven sus secciones; ADMIN, la bandeja.
const enlaces = computed(() => {
  if (!sesion.usuario) return base.slice(0, 1)
  // ADMIN no publica ni puja: ve el catálogo y la bandeja de revisión.
  if (sesion.usuario.role === 'ADMIN') return [base[0], { to: '/admin', texto: 'Bandeja de revisión' }]
  return base
})

async function cerrar() {
  await salir()
  router.push({ name: 'login' })
}
</script>

<template>
  <div class="app">
    <header class="barra">
      <router-link to="/" class="marca">SubastaWeb</router-link>
      <nav class="enlaces" aria-label="Principal">
        <router-link v-for="e in enlaces" :key="e.to" :to="e.to" class="enlace">{{ e.texto }}</router-link>
      </nav>
      <div class="cuenta">
        <template v-if="sesion.usuario">
          <span class="nombre">{{ sesion.usuario.fullName }}</span>
          <button type="button" class="salir" @click="cerrar">Cerrar sesión</button>
        </template>
        <router-link v-else to="/login" class="enlace acceso">Iniciar sesión</router-link>
      </div>
    </header>
    <main class="contenido"><slot /></main>
  </div>
</template>

<style scoped>
.barra { display: flex; align-items: center; gap: 1.5rem; flex-wrap: wrap; padding: 0.75rem 1.5rem; background: #fff; border-bottom: 1px solid var(--borde); }
.marca { font-weight: 700; font-size: 1.15rem; color: var(--primario); text-decoration: none; }
.enlaces { display: flex; gap: 1rem; flex-wrap: wrap; flex: 1; }
.enlace { color: var(--texto); text-decoration: none; padding: 0.25rem 0; border-bottom: 2px solid transparent; }
.enlace:hover, .enlace.router-link-exact-active { border-bottom-color: var(--primario); color: var(--primario); }
.cuenta { display: flex; align-items: center; gap: 0.75rem; }
.nombre { color: var(--suave); }
.salir { border: 1px solid var(--borde); background: #fff; border-radius: 8px; padding: 0.35rem 0.7rem; font: inherit; cursor: pointer; }
.contenido { max-width: 1100px; margin: 0 auto; padding: 1.5rem; }
</style>
