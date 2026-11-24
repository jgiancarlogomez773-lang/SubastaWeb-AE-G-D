import { createRouter, createWebHistory } from 'vue-router'
import PantallaPendiente from '../views/PantallaPendiente.vue'

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
  { path: '/:pathMatch(.*)*', name: 'no-encontrada', component: () => import('../views/NoEncontradaView.vue') },
]

export default createRouter({
  history: createWebHistory(),
  routes: rutas,
  scrollBehavior: () => ({ top: 0 }),
})
