import { createRouter, createWebHistory } from 'vue-router'
import PantallaPendiente from '../views/PantallaPendiente.vue'
import { cargarSesion } from '../stores/sesion'
import { decidirAcceso } from './guardas'

// meta.layout: 'acceso' (login/registro, sin barra) o 'principal' (por defecto).
// meta.requiereSesion / meta.rol: los aplica la guarda de T1-05; aquí solo se declaran.
const rutas = [
  { path: '/', name: 'catalogo', component: () => import('../views/CatalogoView.vue') },
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue'), meta: { layout: 'acceso' } },
  { path: '/registro', name: 'registro', component: () => import('../views/RegistroView.vue'), meta: { layout: 'acceso' } },
  {
    path: '/subastas/:id',
    name: 'detalle-subasta',
    component: PantallaPendiente,
    props: { titulo: 'Detalle de la subasta', tarea: 'Sprint 2' },
  },
  {
    path: '/subastas/:id/resultado',
    name: 'resultado-subasta',
    component: () => import('../views/ResultadoSubasta.vue'),
    props: true,
  },
  {
    path: '/publicar',
    name: 'publicar',
    component: PantallaPendiente,
    props: { titulo: 'Publicar artículo', tarea: 'T1-07' },
    meta: { requiereSesion: true },
  },
  {
    path: '/mis-subastas',
    name: 'mis-subastas',
    component: PantallaPendiente,
    props: { titulo: 'Mis subastas', tarea: 'Sprint 2' },
    meta: { requiereSesion: true },
  },
  {
    path: '/mis-ofertas',
    name: 'mis-ofertas',
    component: PantallaPendiente,
    props: { titulo: 'Mis ofertas', tarea: 'Sprint 3' },
    meta: { requiereSesion: true },
  },
  {
    path: '/perfil',
    name: 'perfil',
    component: PantallaPendiente,
    props: { titulo: 'Mi perfil', tarea: 'Sprint 2' },
    meta: { requiereSesion: true },
  },
  {
    path: '/admin',
    name: 'admin',
    component: PantallaPendiente,
    props: { titulo: 'Bandeja de revisión', tarea: 'Sprint 2' },
    meta: { requiereSesion: true, rol: 'ADMIN' },
  },
  { path: '/403', name: 'prohibido', component: () => import('../views/ProhibidoView.vue') },
  { path: '/:pathMatch(.*)*', name: 'no-encontrada', component: () => import('../views/NoEncontradaView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes: rutas,
  scrollBehavior: () => ({ top: 0 }),
})

router.beforeEach(async (to) => {
  const usuario = await cargarSesion()
  return decidirAcceso(to, usuario)
})

export default router
