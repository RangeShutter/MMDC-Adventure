import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'
import path from 'node:path'

export default defineConfig({
  base: process.env.FIGMA_PUBLIC_URL ? `${process.env.FIGMA_PUBLIC_URL}/` : '/',
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: process.env.FIGMA ? Number(process.env.PORT) : 5173,
    host: process.env.FIGMA ? (process.env.FIGMA_DEV_SERVER_HOST || '0.0.0.0') : undefined,
    strictPort: Boolean(process.env.FIGMA),
  },
})
