/**
 * Application Entry Point
 * Mounts the React app to the DOM element with id="root".
 * StrictMode helps detect potential problems during development.
 */
import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
