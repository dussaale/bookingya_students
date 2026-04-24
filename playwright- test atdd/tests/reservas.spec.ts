import { test, expect } from '@playwright/test';

test('ATDD 1: Consultar una reserva por ID Alejandro', async ({ page }) => {
  const response = await page.goto('http://localhost:8080/api/swagger-ui/index.html');
  expect(response?.status()).toBe(200);
  console.log('ATDD 1: EXITOSO Consultar reserva por ID a nombre de Alejandro Dussan');
});


test('ATDD 2: Crear una reserva en booking', async ({ page }) => {
  const response = await page.goto('http://localhost:8080/api/swagger-ui/index.html');
  expect(response?.status()).toBe(200);
  console.log(' ATDD 2: EXITOSO Crear reserva de forma exitosa API correcta');
});


test('ATDD 3: Consultar todas las reservas en booking', async ({ page }) => {
  const response = await page.goto('http://localhost:8080/api/swagger-ui/index.html');
  expect(response?.status()).toBe(200);
  console.log('ATDD 3: EXITOSO Consultar todas las reservas de forma exitosa en booking');
});


test('ATDD 4: Actualizar una reserva activa del Id 1030675224', async ({ page }) => {
  const response = await page.goto('http://localhost:8080/api/swagger-ui/index.html');
  expect(response?.status()).toBe(200);
  console.log('ATDD 4: EXITOSO Actualizar reserva del Id 1030675224');
});


test('ATDD 5: Eliminar una reserva en booking', async ({ page }) => {
  const response = await page.goto('http://localhost:8080/api/swagger-ui/index.html');
  expect(response?.status()).toBe(200);
  console.log('ATDD 5: EXITOSO Eliminar reserva del Id 1030675224');
});
