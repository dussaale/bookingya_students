package com.project.bookingya.bdd;

import com.project.bookingya.tdd.ReservaServiceTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class ReservaStepDefinitions {


    @Given("que existe una reserva con su ID {string}")
    public void queExisteUnaReservaConSuID(String id) {
        System.out.println("Dado: Existe reserva con ID: " + id);
    }

    @When("solicito los detalles de esa reserva")
    public void solicitoLosDetallesDeEsaReserva() {
        System.out.println("Cuando: Solicitando detalles de la reserva");
    }

    @Then("el sistema debe devolver los datos correctos")
    public void elSistemaDebeDevolverLosDatosCorrectos() {
        System.out.println("Entonces: Datos correctos devueltos");
    }


    @Given("que el huésped {string} quiere reservar la habitación {string} y la fecha de entrada es {string}")
    public void queElHuespedQuiereReservarLaHabitacionYLaFecha(String huesped, String habitacion, String fecha) {
        ReservaServiceTest test = new ReservaServiceTest();
        test.testCreateReservation();
    }

    @When("realizo la reserva")
    public void realizoLaReserva() {
        System.out.println(" Cuando: Realizando la reserva");
    }

    @Then("el sistema debe confirmar la reserva con un código único")
    public void elSistemaDebeConfirmarLaReservaConUnCodigoUnico() {
        System.out.println("Entonces: Reserva confirmada con código único");
    }


    @Given("que existen reservas registradas en el sistema")
    public void queExistenReservasRegistradasEnElSistema() {
        System.out.println("Dado: Existen reservas registradas");
    }

    @When("solicito la lista de todas las reservas")
    public void solicitoLaListaDeTodasLasReservas() {
        System.out.println("Cuando: Solicitando lista de reservas");
    }

    @Then("el sistema debe volver una lista con todas las reservas")
    public void elSistemaDebeVolverUnaListaConTodasLasReservas() {
        System.out.println("Entonces: Lista de reservas devuelta");
    }


    @Given("a que existe una reserva con ID {string}")
    public void aQueExisteUnaReservaConID(String id) {
        System.out.println("Dado: Existe reserva con ID: " + id);
    }

    @When("cambio la fecha de salida al {string}")
    public void cambioLaFechaDeSalidaAl(String nuevaFecha) {
        System.out.println("Cuando: Cambiando fecha de salida a: " + nuevaFecha);
    }

    @Then("el sistema debe actualizar la reserva con la nueva fecha")
    public void elSistemaDebeActualizarLaReservaConLaNuevaFecha() {
        System.out.println("Entonces: Reserva actualizada");
    }


    // (Reutiliza el @Given del escenario 1: "que existe una reserva con su ID {string}")

    @When("solicito cancelar esa reserva")
    public void solicitoCancelarEsaReserva() {
        System.out.println("Cuando: Solicitando cancelación");
    }

    @Then("el sistema debe eliminar la reserva y confirmar la cancelación")
    public void elSistemaDebeEliminarLaReservaYConfirmarLaCancelacion() {
        System.out.println("Entonces: Reserva eliminada y cancelación confirmada");
    }
}