import { test } from '@playwright/test';

test('1 - Crear reserva', async ({ request }) => {
  await request.post('http://localhost:8080/api/reservations', {
    data: {
      roomId: '1030675224',
      guestId: '1030675224',
      checkIn: '2026-05-03T14:00:00',
      checkOut: '2026-05-07T12:00:00',
      guestsCount: 2
    }
  });
  console.log('1 - Crear reserva - COMPLETADO');
});

test('2 - Consultar todas', async ({ request }) => {
  await request.get('http://localhost:8080/api/reservations');
  console.log('2 - Consultar todas - COMPLETADO');
});

test('3 - Consultar por ID', async ({ request }) => {
  await request.get('http://localhost:8080/api/reservations/1030675224');
  console.log('3 - Consultar por ID - COMPLETADO');
});

test('4 - Actualizar', async ({ request }) => {
  await request.put('http://localhost:8080/api/reservations/1030675224', {
    data: {
      roomId: '1030675224',
      guestId: '1030675224',
      checkIn: '2026-05-03T14:00:00',
      checkOut: '2026-05-07T12:00:00',
      guestsCount: 2
    }
  });
  console.log('4 - Actualizar - COMPLETADO');
});

test('5 - Eliminar', async ({ request }) => {
  await request.delete('http://localhost:8080/api/reservations/1030675224');
  console.log('5 - Eliminar - COMPLETADO');
});
