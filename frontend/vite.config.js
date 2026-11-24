import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Las llamadas a la API usan el proxy /api (mismo origen en la demo).
// El backend de Spring Boot escucha en el puerto 8080.
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
})
