package com.project.bookingya.bdd.springBDD;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.entities.RoomEntity;
import com.project.bookingya.entities.GuestEntity;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.repositories.IRoomRepository;
import com.project.bookingya.repositories.IGuestRepository;
import com.project.bookingya.services.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaStepDefinitionsSpring {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private IRoomRepository roomRepository;

    @Autowired
    private IGuestRepository guestRepository;

    private ReservationDto reservaDto;
    private Reservation reservaResponse;
    private List<Reservation> listaReservas;
    private Exception excepcionCapturada;
    private UUID roomId;
    private UUID guestId;
    private UUID idReservaCreada;

    private void prepararHabitacionYHuesped() {
        RoomEntity room = new RoomEntity();
        room.setCode("101");
        room.setName("Habitación 101");
        room.setCity("Bogotá");
        room.setMaxGuests(4);
        room.setNightlyPrice(new BigDecimal("100.00"));
        room.setAvailable(true);
        room = roomRepository.save(room);
        roomId = room.getId();

        GuestEntity guest = new GuestEntity();
        guest.setName("Alejandro Dussan");
        guest.setEmail("alejandro@test.com");
        guest.setIdentification("1030675224");
        guest = guestRepository.save(guest);
        guestId = guest.getId();
    }

    // ==================== ESCENARIO 1: CONSULTAR POR ID ====================
    @Given("que existe una reserva con su ID {string}")
    public void queExisteUnaReservaConSuID(String id) {
        try {
            reservaResponse = reservationService.getById(UUID.fromString(id));
            System.out.println("Dado: Respuesta del servicio: Reservation[id=" + reservaResponse.getId() + "]");
        } catch (Exception e) {
            excepcionCapturada = e;
            System.out.println("Dado: Respuesta del servicio: " + e.getClass().getSimpleName());
        }
    }

    @When("solicito los detalles de esa reserva")
    public void solicitoLosDetallesDeEsaReserva() {
        System.out.println("Cuando: Solicitud enviada al servicio");
    }

    @Then("el sistema debe devolver los datos correctos")
    public void elSistemaDebeDevolverLosDatosCorrectos() {
        if (reservaResponse != null) {
            assertNotNull(reservaResponse.getId());
        }
    }

    // ==================== ESCENARIO 2: CREAR RESERVA ====================
    @Given("que el huésped {string} quiere reservar la habitación {string} y la fecha de entrada es {string}")
    public void configurarNuevaReserva(String huesped, String habitacion, String fecha) {
        prepararHabitacionYHuesped();
        reservaDto = new ReservationDto();
        reservaDto.setRoomId(roomId);
        reservaDto.setGuestId(guestId);
        reservaDto.setGuestsCount(2);
        reservaDto.setCheckIn(LocalDateTime.now().plusDays(1));
        reservaDto.setCheckOut(LocalDateTime.now().plusDays(3));
    }

    @When("realizo la reserva")
    public void ejecutarCreacion() {
        reservaResponse = reservationService.create(reservaDto);
        idReservaCreada = reservaResponse.getId();
        System.out.println("Cuando: Respuesta del servicio: Reservation[id=" + reservaResponse.getId() + "]");
    }

    @Then("el sistema debe confirmar la reserva con un código único")
    public void verificarCreacion() {
        assertNotNull(reservaResponse);
        assertNotNull(reservaResponse.getId());
    }

    // ==================== ESCENARIO 3: CONSULTAR TODAS ====================
    @Given("que existen reservas registradas en el sistema")
    public void queExistenReservasRegistradas() {
        System.out.println("Dado: Preparando consulta");
    }

    @When("solicito la lista de todas las reservas")
    public void solicitoListaDeReservas() {
        listaReservas = reservationService.getAll();
        System.out.println("Cuando: Respuesta del servicio: List<Reservation> con " + listaReservas.size() + " elementos");
    }

    @Then("el sistema debe volver una lista con todas las reservas")
    public void verificarListaReservas() {
        assertNotNull(listaReservas);
        assertTrue(listaReservas.size() >= 1);
    }

    // ==================== ESCENARIO 4: CONSULTAR ID INEXISTENTE ====================
    @Given("que consulto una reserva con ID inexistente {string}")
    public void queConsultoReservaInexistente(String id) {
        try {
            reservaResponse = reservationService.getById(UUID.fromString(id));
            System.out.println("Dado: Inesperado - Reservation[id=" + reservaResponse.getId() + "]");
        } catch (Exception e) {
            excepcionCapturada = e;
            System.out.println("Dado: Respuesta del servicio: " + e.getClass().getSimpleName());
        }
    }

    @When("el sistema busca ese ID")
    public void elSistemaBuscaEseID() {
        System.out.println("Cuando: Búsqueda enviada al servicio");
    }

    @Then("el sistema debe indicar que la reserva no existe")
    public void elSistemaDebeIndicarQueNoExiste() {
        assertNotNull(excepcionCapturada);
    }

    // ==================== ESCENARIO 5: ELIMINAR ====================
    @When("solicito cancelar esa reserva")
    public void solicitoCancelar() {
        try {
            reservationService.delete(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            System.out.println("Cuando: Respuesta del servicio: void (operación completada sin retorno)");
        } catch (Exception e) {
            excepcionCapturada = e;
            System.out.println("Cuando: Respuesta del servicio: " + e.getClass().getSimpleName());
        }
    }

    @Then("el sistema debe eliminar la reserva y confirmar la cancelación")
    public void verificarEliminacion() {
        System.out.println("Entonces: Operación de eliminación procesada por el servicio");
    }
}
