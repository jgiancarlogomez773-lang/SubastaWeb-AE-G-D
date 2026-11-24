<script setup>
// Marcador de las pantallas que se construyen en tareas posteriores.
defineProps({
  titulo: { type: String, required: true },
  tarea: { type: String, default: '' },
})
</script>

<template>
  <section>
    <h1>{{ titulo }}</h1>
    <p class="subtitulo">Pantalla en construcción<span v-if="tarea"> ({{ tarea }})</span>.</p>
  </section>
</template>
