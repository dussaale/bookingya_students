@reservas
Feature: Gestión de reservas
  Como huésped del hotel quiero realizar, consultar y
  modificar mis reservas para asegurar mi estancia.

  #Creación de una reserva - DONE
  #Actualización de una reserva existente - DONE
  #Obtención de una reserva por ID - DONE
  #Eliminación (cancelación) de una reserva


  Scenario: Crear una reserva de forma exitosa
    Given que deseo reservar para el huésped con ID "ae040f15-0aca-4d77-9e9b-854dce590b16"
    And la habitación "b8fcfbb1-2b02-4ca5-9403-d2f01ec8a166" está disponible
    When envío una solicitud de reserva desde "2026-05-10" hasta "2026-05-12" para 2 personas
    Then el sistema responde con un estado 200

  Scenario: Modificar la fecha de salida de una reserva
    Given que tengo la reserva anterior previamente creada
    When actualizo la fecha de salida al "2026-05-20"
    Then el sistema confirma la actualización con un estado 200

  Scenario: Intentar reservar una habitación que supera la capacidad permitida
    Given que tengo la reserva anterior previamente creada
    When actualizo la fecha de salida al "2026-05-20"
    Then el sistema confirma la actualización con un estado 200

  Scenario: Consultar los detalles de una reserva existente
    Given que la habitación "b8fcfbb1-2b02-4ca5-9403-d2f01ec8a166" tiene capacidad máxima de 2
    When envío una solicitud de reserva para 5 personas
    Then el mensaje de error indica "guestsCount exceeds room capacity"

  Scenario: Cancelar una reserva existente
    Given que deseo cancelar la reserva anterior previamente creada
    When solicito la eliminación de la reserva
    Then la reserva ya no debe existir en el sistema