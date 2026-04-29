@reservas
Feature: Gestión de reservas

  Scenario: Crear una reserva con mis datos personales
    Given que el huésped "Alejandro Dussan" quiere reservar la habitación "601" y la fecha de entrada es "2026-05-03"
    When realizo la reserva
    Then el sistema debe confirmar la reserva con un código único

  Scenario: Consultar una reserva existente por su ID
    Given que existe una reserva con su ID "1030675224"
    When solicito los detalles de esa reserva
    Then el sistema debe devolver los datos correctos

  Scenario: Obtener el listado completo de reservas
    Given que existen reservas registradas en el sistema
    When solicito la lista de todas las reservas
    Then el sistema debe volver una lista con todas las reservas

  Scenario: Consultar una reserva con ID inexistente
    Given que consulto una reserva con ID inexistente "00000000-0000-0000-0000-000000000000"
    When el sistema busca ese ID
    Then el sistema debe indicar que la reserva no existe

  Scenario: Cancelar una reserva existente
    Given que existe una reserva con su ID "1030675224"
    When solicito cancelar esa reserva
    Then el sistema debe eliminar la reserva y confirmar la cancelación