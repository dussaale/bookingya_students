@reservas
Feature: Gestión de reservas

  Scenario: Crear una reserva con mis datos personales
    Given que el huésped "Alejandro Dussan" quiere reservar la habitación "601" y la fecha de entrada es "2026-05-03"
    When realizo la reserva
    Then el sistema debe confirmar la reserva con un código único

  Scenario: Consultar una reserva existente por su ID
    Given que existe una reserva con su ID "550e8400-e29b-41d4-a716-446655440000"
    When solicito los detalles de esa reserva
    Then el sistema debe devolver los datos correctos

  Scenario: Obtener el listado completo de reservas
    Given que existen reservas registradas en el sistema
    When solicito la lista de todas las reservas
    Then el sistema debe volver una lista con todas las reservas

  Scenario: Modificar la fecha de la salida de una reserva
    Given a que existe una reserva con ID "1030675224"
    When cambio la fecha de salida al "2026-05-07"
    Then el sistema debe actualizar la reserva con la nueva fecha

  Scenario: Cancelar una reserva existente
    Given que existe una reserva con su ID "1030675224"
    When solicito cancelar esa reserva
    Then el sistema debe eliminar la reserva y confirmar la cancelación