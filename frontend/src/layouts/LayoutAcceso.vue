<template>
  <div class="acceso">
    <router-link to="/" class="marca">SubastaWeb</router-link>
    <main class="tarjeta"><slot /></main>
  </div>
</template>

<style scoped>
.acceso { min-height: 100vh; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 1rem; padding: 1rem; }
.marca { font-weight: 700; font-size: 1.4rem; color: var(--primario); text-decoration: none; }
.tarjeta { width: 100%; max-width: 420px; background: #fff; border: 1px solid var(--borde); border-radius: 12px; padding: 1.75rem; }
</style>
