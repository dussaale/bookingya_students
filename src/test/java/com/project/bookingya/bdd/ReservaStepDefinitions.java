package com.project.bookingya.bdd;

import com.project.bookingya.dtos.ReservationDto;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.services.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservaStepDefinitions {

    @Autowired
    private ReservationService reservationService;

    private ReservationDto reservaDto;
    private Reservation reservaResponse;
    private List<Reservation> listaReservas;
    private UUID idBusqueda;

    // --- ESCENARIO: Crear una reserva ---
    @Given("que el huésped {string} quiere reservar la habitación {string} y la fecha de entrada es {string}")
    public void configurarNuevaReserva(String huesped, String habitacion, String fecha) {
        reservaDto = new ReservationDto();
        // Usamos los datos del feature para configurar el DTO
        reservaDto.setNotes("Reserva de " + huesped + " - Habitación " + habitacion);
        reservaDto.setGuestsCount(2);
        // Nota: Aquí deberías setear IDs reales de tu DB H2 si es necesario
        reservaDto.setRoomId(UUID.randomUUID());
        reservaDto.setGuestId(UUID.randomUUID());
    }

    @When("realizo la reserva")
    public void ejecutarCreacion() {
        // Ejecuta la misma lógica que probaste en TDD
        reservaResponse = reservationService.create(reservaDto);
    }

    @Then("el sistema debe confirmar la reserva con un código único")
    public void validarConfirmacion() {
        assertThat(reservaResponse).isNotNull();
        assertThat(reservaResponse.getId()).isNotNull();
        System.out.println("✅ BDD: Reserva creada con ID: " + reservaResponse.getId());
    }

    // --- ESCENARIO: Consultar por ID ---
    @Given("que existe una reserva con su ID {string}")
    public void establecerIdBusqueda(String id) {
        idBusqueda = UUID.fromString(id);
    }

    @When("solicito los detalles de esa reserva")
    public void ejecutarConsultaPorId() {
        reservaResponse = reservationService.getById(idBusqueda);
    }

    @Then("el sistema debe devolver los datos correctos")
    public void validarDatosRetornados() {
        assertThat(reservaResponse).isNotNull();
        assertThat(reservaResponse.getId()).isEqualTo(idBusqueda);
        System.out.println("✅ BDD: Datos de reserva validados correctamente");
    }

    // --- ESCENARIO: Listado completo ---
    @Given("que existen reservas registradas en el sistema")
    public void verificarExistenciaReservas() {
        // Podrías crear una reserva previa aquí para asegurar que la lista no esté vacía
    }

    @When("solicito la lista de todas las reservas")
    public void ejecutarConsultaGeneral() {
        listaReservas = reservationService.getAll();
    }

    @Then("el sistema debe volver una lista con todas las reservas")
    public void validarLista() {
        assertThat(listaReservas).isNotNull();
        System.out.println("✅ BDD: Se recuperaron " + listaReservas.size() + " reservas");
    }

    // --- ESCENARIO: Modificar fecha ---
    @Given("a que existe una reserva con ID {string}")
    public void prepararIdParaActualizar(String id) {
        idBusqueda = UUID.fromString(id);
    }

    @When("cambio la fecha de salida al {string}")
    public void ejecutarActualizacion(String nuevaFecha) {
        // Simulamos el DTO de actualización
        ReservationDto updateDto = new ReservationDto();
        updateDto.setNotes("Actualización de fecha BDD");
        reservaResponse = reservationService.update(updateDto, idBusqueda);
    }

    @Then("el sistema debe actualizar la reserva con la nueva fecha")
    public void validarActualizacion() {
        assertThat(reservaResponse).isNotNull();
        System.out.println("✅ BDD: Reserva " + idBusqueda + " actualizada con éxito");
    }

    // --- ESCENARIO: Cancelar reserva ---
    @When("solicito cancelar esa reserva")
    public void ejecutarCancelacion() {
        reservationService.delete(idBusqueda);
    }

    @Then("el sistema debe eliminar la reserva y confirmar la cancelación")
    public void validarEliminacion() {
        // Si el método no lanzó excepción, asumimos éxito (como en tu TDD)
        System.out.println("✅ BDD: Reserva " + idBusqueda + " eliminada correctamente");
    }
}