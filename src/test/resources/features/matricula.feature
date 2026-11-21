Feature: Matrícula de Ana para el ciclo
  Como alumna de CIBERTEC
  Quiero cotizar y pagar mi curso, y confirmar qué pasa si intento repetir uno aprobado
  Para revisar el flujo de matrícula de mi fila

  Scenario: Ana cotiza Taller de Software antes de matricularse
    Given que Ana consulta la API de matricula
    And inicia sesion como alumna "i202610001"
    When cotiza el curso "TS5514" para la alumna "i202610001"
    Then el codigo de respuesta es 200
    And la respuesta cumple el esquema "cotizacion.json"
    And la cotizacion devuelve 4 creditos
    And el subtotal cotizado es 480.0
    And el descuento cotizado es 20 por ciento
    And el total cotizado es 384.0

  Scenario: Ana se matricula en Taller de Software y lo paga con YAPE
    Given que Ana consulta la API de matricula
    And inicia sesion como alumna "i202610001"
    When matricula a "i202610001" en el curso "TS5514"
    Then el codigo de respuesta es 201
    And la respuesta cumple el esquema "matricula.json"
    And el monto de la matricula es 384.0
    And el estado de la matricula es "REGISTRADA"
    When paga esa matricula con "YAPE"
    Then el codigo de respuesta es 200
    And la respuesta cumple el esquema "matricula.json"
    And el estado de la matricula es "PAGADA"
    And la fecha de pago tiene valor
    When consulta sus pagos con metodo "YAPE"
    Then el codigo de respuesta es 200
    And el pago consultado pertenece a la matricula creada
    And el pago registra el metodo "YAPE"
    And el numero de operacion tiene el formato "OP-000000"

  Scenario: Ana no puede volver a matricularse en LP5401 porque ya lo aprobo
    Given que Ana consulta la API de matricula
    And inicia sesion como alumna "i202610001"
    When matricula a "i202610001" en el curso "LP5401"
    Then el codigo de respuesta es 422
    And la respuesta cumple el esquema "error.json"
    And el codigo de error es "CURSO_APROBADO"
