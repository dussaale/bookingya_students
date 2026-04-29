package com.project.bookingya.bdd;

import com.project.bookingya.dtos.ReservationDto;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import net.serenitybdd.core.Serenity;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReservaStepDefinitions {

    private final String BASE_URL = "http://localhost:8080/api/reservation";
    private Response response;
    private ReservationDto reservationDto = new ReservationDto();
    private String idReservaActual;

    @Given("que deseo reservar para el huésped con ID {string}")
    public void setHuespedId(String guestId) {
        Serenity.setSessionVariable("idGuest").to(guestId);
        reservationDto.setGuestId(UUID.fromString(guestId));
    }

    @And("la habitación {string} está disponible")
    public void setHabitacionId(String roomId) {
        Serenity.setSessionVariable("idRoom").to(roomId);
        reservationDto.setRoomId(UUID.fromString(roomId));
    }

    @When("envío una solicitud de reserva desde {string} hasta {string} para {int} personas")
    public void enviarSolicitudReserva(String checkIn, String checkOut, Integer guests) {
        reservationDto.setCheckIn(LocalDateTime.parse(checkIn + "T14:00:00"));
        reservationDto.setCheckOut(LocalDateTime.parse(checkOut + "T11:00:00"));
        reservationDto.setGuestsCount(guests);

        response = given()
                .contentType(ContentType.JSON)
                .body(reservationDto)
                .when()
                .post(BASE_URL);

        Serenity.setSessionVariable("idReserv").to(response.jsonPath().getString("id"));
    }

    @Then("el sistema responde con un estado {int}")
    public void validarStatusCode(int code) {
        response.then().statusCode(code);
    }
    // --- ESCENARIO: Actualización ---

    @Given("que tengo la reserva anterior previamente creada")
    public void prepararIdParaUpdate() {
        this.idReservaActual = Serenity.sessionVariableCalled("idReserv");
    }

    @When("actualizo la fecha de salida al {string}")
    public void actualizarFechaSalida(String nuevaFecha) {
        // Preparamos el DTO de actualización
        ReservationDto updateDto = new ReservationDto();
        updateDto.setCheckOut(LocalDateTime.parse(nuevaFecha + "T11:00:00"));
        updateDto.setGuestId(UUID.fromString(Serenity.sessionVariableCalled("idGuest")));
        updateDto.setRoomId(UUID.fromString(Serenity.sessionVariableCalled("idRoom")));
        updateDto.setCheckIn(LocalDateTime.now().plusDays(1));
        updateDto.setGuestsCount(1);

        response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", idReservaActual)
                .body(updateDto)
                .when()
                .put(BASE_URL + "/{id}");
        System.out.println(response.jsonPath().prettyPrint());

    }

    @Then("el sistema confirma la actualización con un estado {int}")
    public void confirmarUpdate(int code) {
        response.then().statusCode(code);
    }


// --- ESCENARIO: Consulta por ID ---

    @When("solicito la información de la reserva por su ID")
    public void solicitarPorId() {
        response = given()
                .pathParam("id", idReservaActual)
                .when()
                .get(BASE_URL + "/{id}");
    }

    @Then("el ID coincide con la reserva anterior previamente creada")
    public void validarIdCoincide() {
        response.then().body("id", equalTo(idReservaActual));
    }

    // --- ESCENARIO: Fallo por Capacidad ---

    @Given("que la habitación {string} tiene capacidad máxima de {int}")
    public void setHabitacionConCapacidad(String roomId, Integer capacidad) {
        reservationDto.setRoomId(UUID.fromString(roomId));
    }

    @When("envío una solicitud de reserva para {int} personas")
    public void enviarSolicitudReservaCapacidadExcedida(Integer cantidadPersonas) {
        reservationDto.setGuestsCount(cantidadPersonas);

        reservationDto.setCheckIn(LocalDateTime.now().plusDays(1));
        reservationDto.setCheckOut(LocalDateTime.now().plusDays(3));
        reservationDto.setGuestId(UUID.fromString(Serenity.sessionVariableCalled("idGuest")));

        response = given()
                .contentType(ContentType.JSON)
                .body(reservationDto)
                .when()
                .post(BASE_URL);

        System.out.println("Respuesta del servicio (Validación Capacidad): " + response.asString());
    }

    @Then("el mensaje de error indica {string}")
    public void validarMensajeError(String mensaje) {
        response.then().body("error", equalTo(mensaje));
    }

    // --- ESCENARIO: Eliminación ---

    @Given("que deseo cancelar la reserva anterior previamente creada")
    public void prepararIdParaDelete() {
        this.idReservaActual = Serenity.sessionVariableCalled("idReserv");
    }

    @When("solicito la eliminación de la reserva")
    public void eliminarReserva() {
        response = given()
                .pathParam("id", idReservaActual)
                .when()
                .delete(BASE_URL + "/{id}");
    }

    @Then("la reserva ya no debe existir en el sistema")
    public void verificarInexistencia() {
        // Intentamos un GET y esperamos un 404
        given()
                .pathParam("id", idReservaActual)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(404);
    }
}