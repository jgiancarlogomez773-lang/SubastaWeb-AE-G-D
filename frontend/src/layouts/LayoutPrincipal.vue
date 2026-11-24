<script setup>
// Layout reutilizable con barra de navegación (según los mockups).
// T1-05/T1-06 mostrarán u ocultarán enlaces según la sesión y el rol.
const enlaces = [
  { to: '/', texto: 'Catálogo' },
  { to: '/mis-subastas', texto: 'Mis subastas' },
  { to: '/mis-ofertas', texto: 'Mis ofertas' },
  { to: '/perfil', texto: 'Mi perfil' },
  { to: '/publicar', texto: 'Publicar artículo' },
]
</script>

<template>
  <div class="app">
    <header class="barra">
      <router-link to="/" class="marca">SubastaWeb</router-link>
      <nav class="enlaces" aria-label="Principal">
        <router-link v-for="e in enlaces" :key="e.to" :to="e.to" class="enlace">{{ e.texto }}</router-link>
      </nav>
      <router-link to="/login" class="enlace acceso">Iniciar sesión</router-link>
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
.contenido { max-width: 1100px; margin: 0 auto; padding: 1.5rem; }
</style>
