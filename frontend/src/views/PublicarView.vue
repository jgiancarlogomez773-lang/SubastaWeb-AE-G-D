<script setup>
import { reactive, ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { obtenerCategorias, enviarSolicitud } from '../services/subastas'
import { validarSolicitud, validarImagen, DURACIONES } from '../services/validacionSolicitud'

const router = useRouter()

const form = reactive({ title: '', description: '', categoryId: '', startingPrice: '', durationHours: 24, image: null })
const categorias = ref([])
const errores = ref({})
const error = ref('')
const cargando = ref(false)
const enviada = ref(null)
const vistaPrevia = ref('')

const moneda = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })
const precioLegible = computed(() => (Number(form.startingPrice) > 0 ? moneda.format(Number(form.startingPrice)) : ''))

onMounted(async () => {
  try {
    categorias.value = await obtenerCategorias()
  } catch {
    error.value = 'No pudimos cargar las categorías. Recarga la página.'
  }
})

function liberarVistaPrevia() {
  if (vistaPrevia.value) URL.revokeObjectURL(vistaPrevia.value)
  vistaPrevia.value = ''
}
onBeforeUnmount(liberarVistaPrevia)

function elegirImagen(evento) {
  const archivo = evento.target.files?.[0] || null
  liberarVistaPrevia()
  const problema = archivo ? validarImagen(archivo) : ''
  if (problema) {
    form.image = null
    errores.value = { ...errores.value, image: problema }
    evento.target.value = ''
    return
  }
  form.image = archivo
  if (archivo) vistaPrevia.value = URL.createObjectURL(archivo)
  const { image: _quitado, ...resto } = errores.value
  errores.value = resto
}

async function enviar() {
  if (cargando.value) return
  error.value = ''
  errores.value = validarSolicitud(form)
  if (Object.keys(errores.value).length) return

  cargando.value = true
  try {
    enviada.value = await enviarSolicitud({ ...form })
  } catch {
    error.value = 'No pudimos enviar la solicitud. Inténtalo de nuevo en unos minutos.'
  } finally {
    cargando.value = false
  }
}

function otraSolicitud() {
  enviada.value = null
  Object.assign(form, { title: '', description: '', categoryId: '', startingPrice: '', durationHours: 24, image: null })
  liberarVistaPrevia()
  errores.value = {}
}
</script>

<template>
  <section>
    <h1>Publicar artículo</h1>
    <p class="subtitulo">Tu solicitud queda privada hasta que un administrador la apruebe.</p>

    <div v-if="enviada" class="exito" role="status">
      <strong>Solicitud enviada a revisión.</strong>
      <p>«{{ enviada.title }}» quedó en estado {{ enviada.status }} y no es visible en el catálogo.</p>
      <button type="button" class="secundario" @click="otraSolicitud">Publicar otro artículo</button>
    </div>

    <form v-else novalidate @submit.prevent="enviar">
      <p v-if="error" class="error" role="alert">{{ error }}</p>

      <label for="title">Título</label>
      <input id="title" v-model="form.title" type="text" maxlength="120" :aria-invalid="!!errores.title" />
      <small v-if="errores.title" class="campo-error">{{ errores.title }}</small>

      <label for="categoryId">Categoría</label>
      <select id="categoryId" v-model="form.categoryId" :aria-invalid="!!errores.categoryId">
        <option value="" disabled>Elige una categoría</option>
        <option v-for="c in categorias" :key="c.id" :value="c.id">{{ c.name }}</option>
      </select>
      <small v-if="errores.categoryId" class="campo-error">{{ errores.categoryId }}</small>

      <label for="description">Descripción</label>
      <textarea id="description" v-model="form.description" rows="5" maxlength="2000" :aria-invalid="!!errores.description"></textarea>
      <small v-if="errores.description" class="campo-error">{{ errores.description }}</small>
      <small v-else class="ayuda">{{ form.description.trim().length }}/2000 (mínimo 20)</small>

      <div class="fila">
        <div class="col">
          <label for="startingPrice">Precio inicial (COP, mínimo 1.000)</label>
          <input id="startingPrice" v-model="form.startingPrice" type="number" inputmode="numeric" min="1000" step="1" :aria-invalid="!!errores.startingPrice" />
          <small v-if="errores.startingPrice" class="campo-error">{{ errores.startingPrice }}</small>
          <small v-else-if="precioLegible" class="ayuda">{{ precioLegible }}</small>
        </div>
        <div class="col">
          <label for="durationHours">Duración</label>
          <select id="durationHours" v-model.number="form.durationHours" :aria-invalid="!!errores.durationHours">
            <option v-for="h in DURACIONES" :key="h" :value="h">{{ h }} horas</option>
          </select>
          <small v-if="errores.durationHours" class="campo-error">{{ errores.durationHours }}</small>
        </div>
      </div>

      <label for="image">Imagen (JPG, PNG o WebP, máx. 3 MB)</label>
      <input id="image" type="file" accept="image/jpeg,image/png,image/webp" :aria-invalid="!!errores.image" @change="elegirImagen" />
      <small v-if="errores.image" class="campo-error">{{ errores.image }}</small>
      <img v-if="vistaPrevia" :src="vistaPrevia" alt="Vista previa de la imagen del artículo" class="previa" />

      <div class="acciones">
        <button type="button" class="secundario" :disabled="cargando" @click="router.push({ name: 'catalogo' })">Cancelar</button>
        <button type="submit" :disabled="cargando">{{ cargando ? 'Enviando…' : 'Enviar a revisión' }}</button>
      </div>
    </form>
  </section>
</template>

<style scoped>
section { max-width: 640px; }
form { display: flex; flex-direction: column; gap: 0.4rem; }
label { font-weight: 600; margin-top: 0.5rem; }
input, select, textarea { padding: 0.6rem 0.7rem; border: 1px solid var(--borde); border-radius: 8px; font: inherit; background: #fff; }
input[aria-invalid='true'], select[aria-invalid='true'], textarea[aria-invalid='true'] { border-color: #b91c1c; }
.fila { display: flex; gap: 1rem; flex-wrap: wrap; }
.col { flex: 1; min-width: 200px; display: flex; flex-direction: column; gap: 0.4rem; }
.previa { max-width: 240px; border-radius: 8px; border: 1px solid var(--borde); }
.acciones { display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1rem; }
button { padding: 0.7rem 1.2rem; border: 0; border-radius: 8px; background: var(--primario); color: #fff; font: inherit; font-weight: 600; cursor: pointer; }
button.secundario { background: #fff; color: var(--texto); border: 1px solid var(--borde); }
button:disabled { opacity: 0.6; cursor: wait; }
.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; border-radius: 8px; padding: 0.6rem 0.8rem; }
.exito { background: #f0fdf4; color: #166534; border: 1px solid #bbf7d0; border-radius: 8px; padding: 1rem; }
.campo-error { color: #b91c1c; }
.ayuda { color: var(--suave); }
</style>
