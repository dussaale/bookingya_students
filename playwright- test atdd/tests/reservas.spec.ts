import { test, expect } from '@playwright/test';

import { RoomClient } from '../src/api/RoomClient';
import { GuestClient } from '../src/api/GuestClient';
import { ReservationsClient } from '../src/api/ReservationsClient';


/*

Creación de una reserva - DONE
Actualización de una reserva existente - DONE
Eliminación (cancelación) de una reserva
Consulta de todas las reservas - DONE
Obtención de una reserva por ID - DONE
Consulta de reservas por usuario - DONE

Casos Alternativos
Crear el mismo usuario - DONE
crear la misma habitacion - DONE
Eliminación (cancelación) de una reserva no existente - DONE
Obtención de una reserva por ID no existente - DONE

*/


const BASE_URL = 'http://localhost:8080/api/reservation';
//const ROOM_CODE = 'room001'; 
//const GUEST_ID = '123456'; 
const ROOM_CODE = `room${Math.floor(100 + Math.random() * 900)}`; 
const GUEST_ID = Math.floor(100000 + Math.random() * 900000).toString();
const GUEST_EMAIL = `SAM@${Math.floor(100 + Math.random() * 900)}d.com`; 


test.describe('Pruebas de Aceptación - API de Reservas', () => {
let roomId: any;
let guestId: any;
let reservationId: any;

  test('1 - Crear reserva exitosamente', async ({ request }) => {

    const roomClient = new RoomClient(request);
    const guestClient = new GuestClient(request);
    const reservationsClient = new ReservationsClient(request);

    const room = await roomClient.createRoom({
        "code": ROOM_CODE,
        "name": "Santiago",
        "city": "Riohacha",
        "maxGuests": 2,
        "nightlyPrice": 50000,
        "available": true
    });

    const guest = await guestClient.createGuest({
        "identification": GUEST_ID,
        "name": "Alejo",
        "email": GUEST_EMAIL
    });


    roomId = await room.json();
    guestId = await guest.json();

    const reservation = await reservationsClient.createReservations({
        "guestId": guestId.id,
        "roomId": roomId.id,
        "checkIn": "2026-04-26T18:59:37.759Z",
        "checkOut": "2026-04-26T20:59:37.759Z",
        "guestsCount": 1,
        "notes": "Prueba"
    });
    reservationId = await reservation.json();

    expect(reservation.ok()).toBeTruthy();
    const body = await reservation.json();
    expect(body).toHaveProperty('id');
    console.log(`1 - Crear reserva - COMPLETADO. Id generado: ${body.id}`);
  });



  test('2 - Consultar listado completo', async ({ request }) => {

    const reservationsClient = new ReservationsClient(request);
    const reservations = await reservationsClient.getReservations();

    expect(reservations.status()).toBe(200);
    const body = await reservations.json();
    expect(Array.isArray(body)).toBeTruthy();
    console.log(`Registros encontrado: \n${JSON.stringify(body, null, 2)}`);
  });


    test('3 - Consulta de una reserva por ID', async ({ request }) => {
     const reservationsClient = new ReservationsClient(request);

    const reservations = await reservationsClient.getReservationsById(reservationId.id);
    
    expect(reservations.status()).toBe(200);
    const body = await reservations.json();
    console.log(`Registro encontrado: \n${JSON.stringify(body, null, 2)}`);
  });
  
    test('4 - Consulta de una reserva por huesped', async ({ request }) => {
    const reservationsClient = new ReservationsClient(request);
    const reservations = await reservationsClient.getReservationsByGuest(guestId.id);
    expect(reservations.status()).toBe(200);
    const body = await reservations.json();
    console.log(`Registro encontrado: \n${JSON.stringify(body, null, 2)}`);
  });
  

  test('5 - Actualización de una reserva existente ', async ({ request }) => {
    const reservationsClient = new ReservationsClient(request);

    const newReservations = await reservationsClient.UpdateReservations({
        "guestId": guestId.id,
        "roomId": roomId.id,
        "checkIn": "2026-04-28T15:59:37.759Z",
        "checkOut": "2026-04-30T12:59:37.759Z",
        "guestsCount": 1,
        "notes": "Actualizacion de reserva"
    },reservationId.id);

    
    expect(newReservations.status()).toBe(200);
    const body = await newReservations.json();
    console.log(`Reservacion actualizada: \n${JSON.stringify(body, null, 2)}`);
  });

  test('6 - Eliminar reserva (Cancelación)', async ({ request }) => {
    const reservationsClient = new ReservationsClient(request);

    const deleteReservations = await reservationsClient.deleteReservationsById(reservationId.id);
  
    expect(deleteReservations.status()).toBe(200); 
    console.log('Reserva eliminada');
  });

  test('7 - Crear un huespet ya existente', async ({ request }) => {

    
    const guestClient = new GuestClient(request);

    const guest = await guestClient.createGuest(    {
        "identification": GUEST_ID,
        "name": "Alejo",
        "email": GUEST_EMAIL
    });

    guestId = await guest.json();

    expect(guest.status()).toBe(409); 
    expect(guestId).toHaveProperty('error');
    expect(guestId.error).toEqual("Guest already exists");
    console.log(`No se pudo crear el cliente ya existente`);
  });


  test('8 - Crear una habitacion ya existente', async ({ request }) => {

    const roomClient = new RoomClient(request);

    const room = await roomClient.createRoom({
        "code": ROOM_CODE,
        "name": "Santiago",
        "city": "Riohacha",
        "maxGuests": 2,
        "nightlyPrice": 50000,
        "available": true
    });

    roomId = await room.json();

    expect(room.status()).toBe(409); 
    expect(roomId).toHaveProperty('error');
    expect(roomId.error).toEqual("Room already exists");
    console.log(`No se pudo crear el cliente ya existente`);
  });

    test('9 - Eliminar reserva no existente', async ({ request }) => {
    const reservationsClient = new ReservationsClient(request);

    const deleteReservations = await reservationsClient.deleteReservationsById("27f073bb-5686-4223-8d52-1aeb59b3ae68");
  
   const deleteId = await deleteReservations.json();
    expect(deleteReservations.status()).toBe(404); 
    expect(deleteId).toHaveProperty('error');
    expect(deleteId.error).toEqual("Reservation not found");
    console.log('Reserva eliminada');
  });

    test('10 - Obtener una reserva no existente', async ({ request }) => {
     const reservationsClient = new ReservationsClient(request);

    const reservations = await reservationsClient.getReservationsById("27f073bb-5686-4223-8d52-1aeb59b3ae68");
  
   const reservatioId = await reservations.json();
    expect(reservations.status()).toBe(404); 
    expect(reservatioId).toHaveProperty('error');
    expect(reservatioId.error).toEqual("Reservation not found");
    console.log('Reserva eliminada');
  });

});
