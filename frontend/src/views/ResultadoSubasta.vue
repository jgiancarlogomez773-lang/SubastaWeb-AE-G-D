<script setup>
// T4-03 · Vista de subasta finalizada: ganador / sin ofertas
// Endpoint (TRD v4, sección 5): GET /api/v1/auctions/{id}/result  (público)
//   200 -> resultado final
//   404 -> subasta no existe
//   409 -> la subasta sigue ACTIVA (no se declara ganador anticipadamente)
// Cuerpo del 200: nombres de campos ASUMIDOS, confírmalos con TRD 5.1 / T4-09:
//   { id, titulo, estado: "FINALIZADA", totalPujas: number,
//     ganador: { alias: string, importe: number } | null }
import { ref, computed, onMounted } from 'vue'

const props = defineProps({
  id: { type: [String, Number], required: true },
  // true = usa datos simulados mientras la API real no esté lista
  usarMock: { type: Boolean, default: false },
  // con mock, 'ganador' | 'sin-ofertas' | 'error'
  escenarioMock: { type: String, default: 'ganador' },
})

const cargando = ref(true)
const error = ref('')
const resultado = ref(null)

const hayGanador = computed(() => !!resultado.value?.ganador)

const moneda = new Intl.NumberFormat('es-CO', {
  style: 'currency',
  currency: 'COP',
  maximumFractionDigits: 0,
})

async function obtenerResultado() {
  if (props.usarMock) {
    await new Promise((r) => setTimeout(r, 400))
    if (props.escenarioMock === 'error') throw new Error('Error simulado')
    if (props.escenarioMock === 'activa') return { activa: true }
    return {
      id: props.id,
      titulo: 'Reloj de bolsillo, 1920',
      estado: 'FINALIZADA',
      totalPujas: props.escenarioMock === 'sin-ofertas' ? 0 : 12,
      ganador:
        props.escenarioMock === 'sin-ofertas'
          ? null
          : { alias: 'coleccionista_77', importe: 850000 },
    }
  }
  const resp = await fetch(`/api/v1/auctions/${props.id}/result`)
  if (resp.status === 404) throw new Error('Esta subasta no existe.')
  if (resp.status === 409) return { activa: true }
  if (!resp.ok) throw new Error('No pudimos cargar el resultado. Inténtalo de nuevo.')
  return resp.json()
}

async function cargar() {
  cargando.value = true
  error.value = ''
  try {
    resultado.value = await obtenerResultado()
  } catch (e) {
    error.value = e.message || 'No pudimos cargar el resultado.'
  } finally {
    cargando.value = false
  }
}

onMounted(cargar)
</script>

<template>
  <section class="resultado" aria-live="polite">
    <p v-if="cargando" class="estado">Cargando resultado…</p>

    <div v-else-if="error" class="estado estado--error" role="alert">
      <p>{{ error }}</p>
      <button type="button" @click="cargar">Reintentar</button>
    </div>

    <div v-else-if="resultado?.activa" class="estado" data-testid="activa">
      <p>Esta subasta sigue activa.</p>
      <p class="detalle">El resultado se publicará cuando termine.</p>
    </div>

    <article v-else-if="resultado" class="tarjeta" :class="{ 'tarjeta--vacia': !hayGanador }">
      <h1 class="titulo">{{ resultado.titulo }}</h1>
      <p class="meta">Subasta finalizada</p>

      <!-- Con ganador -->
      <div v-if="hayGanador" class="ganador" data-testid="ganador">
        <p class="etiqueta">Ganador</p>
        <p class="alias">{{ resultado.ganador.alias }}</p>
        <p class="importe">{{ moneda.format(resultado.ganador.importe) }}</p>
        <p class="detalle">{{ resultado.totalPujas }} pujas en total</p>
      </div>

      <!-- Sin pujas -->
      <div v-else class="sin-ofertas" data-testid="sin-ofertas">
        <p class="alias">Sin ofertas</p>
        <p class="detalle">Nadie pujó por este artículo.</p>
      </div>
    </article>
  </section>
</template>

<style scoped>
.resultado {
  max-width: 36rem;
  margin: 0 auto;
  padding: 1.5rem 1rem;
  font-family: Georgia, 'Times New Roman', serif;
  color: #1f2a37;
}
.tarjeta {
  border: 2px solid #1f2a37;
  border-radius: 4px;
  padding: 1.5rem;
  background: #fffdf8;
}
.tarjeta--vacia {
  border-style: dashed;
  background: #f5f5f4;
}
.titulo {
  margin: 0;
  font-size: clamp(1.4rem, 5vw, 2rem);
  line-height: 1.2;
}
.meta,
.detalle,
.etiqueta {
  margin: 0.25rem 0 0;
  color: #57606a;
  font-size: 0.95rem;
}
.ganador {
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid #d0d7de;
}
.alias {
  margin: 0.25rem 0 0;
  font-size: 1.5rem;
  font-weight: 700;
  overflow-wrap: anywhere;
}
.importe {
  margin: 0.25rem 0 0;
  font-size: clamp(2rem, 9vw, 3rem);
  font-weight: 700;
  color: #0a5c36;
  font-variant-numeric: tabular-nums;
}
.sin-ofertas {
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px dashed #a8a29e;
}
.estado {
  text-align: center;
  padding: 2rem 0;
}
.estado--error {
  color: #9a1c1c;
}
button {
  margin-top: 0.5rem;
  padding: 0.5rem 1rem;
  font: inherit;
  cursor: pointer;
}
button:focus-visible {
  outline: 3px solid #1d4ed8;
  outline-offset: 2px;
}
</style>
