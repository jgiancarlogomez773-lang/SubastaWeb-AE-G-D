<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import LayoutPrincipal from './layouts/LayoutPrincipal.vue'
import LayoutAcceso from './layouts/LayoutAcceso.vue'

const route = useRoute()
// Cada ruta declara su layout en meta.layout ('acceso' o 'principal').
const layout = computed(() => (route.meta.layout === 'acceso' ? LayoutAcceso : LayoutPrincipal))
</script>

<template>
  <component :is="layout">
    <router-view />
  </component>
</template>
