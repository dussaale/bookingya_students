import { test, expect } from '@playwright/test';

import { RoomClient } from '../src/api/RoomClient';
import { GuestClient } from '../src/api/GuestClient';
import { ReservationsClient } from '../src/api/ReservationsClient';


/*
Creación de una reserva - DONE
Consulta de una reserva - DONE
Obtención de una reserva por ID - DONE
Actualización de una reserva existente - DONE
Eliminación de una reserva - DONE

Creación de una reserva - DONE
Actualización de una reserva existente - DONE
Eliminación (cancelación) de una reserva
Consulta de todas las reservas - DONE
Obtención de una reserva por ID - DONE
Consulta de reservas por usuario - DONE

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
        "name": "Santiago",
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

  test('5 - Eliminar reserva (Cancelación)', async ({ request }) => {
    const reservationsClient = new ReservationsClient(request);

    const deleteReservations = await reservationsClient.deleteReservationsById(reservationId.id);
  
    expect(deleteReservations.status()).toBe(200); 
    console.log('Reserva eliminada');
  });

});
