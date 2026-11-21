@pregunta3
Feature: Calificacion de matriculas e historial
  Como docente de CIBERTEC
  Quiero registrar notas y consultar su efecto en el historial academico
  Para confirmar las reglas de calificacion

  Scenario Outline: El docente del curso registra la nota y se actualiza el historial
    Given que Rosa consulta la API de matricula
    And inicia sesion como alumna "i202610005"
    When busca la matricula del curso "PI5620" en periodo "2026-2" con estado "PAGADA"
    And inicia sesion como docente "D0001"
    When registra la nota <nota> en la matricula consultada
    Then el codigo de respuesta es 200
    And la calificacion cumple el esquema "matricula.json"
    And la nota de la matricula es <nota>
    When inicia sesion como alumna "i202610005"
    And consulta el historial de "i202610005"
    Then el codigo de respuesta es 200
    And el historial cumple el esquema "historial.json"
    And el historial muestra en "PI5620" la nota <nota> y condicion "<condicion>"
    And el historial tiene <creditosAprobados> creditos aprobados
    And el promedio ponderado del historial es <promedio>

    Examples:
      | nota | condicion   | creditosAprobados | promedio |
      | 10.0 | DESAPROBADO | 7                 | 12.92    |
      | 12.5 | DESAPROBADO | 7                 | 14.08    |

  Scenario: Un docente ajeno al curso no puede registrar la nota
    Given que Rosa consulta la API de matricula
    And inicia sesion como alumna "i202610005"
    When busca la matricula del curso "PI5620" en periodo "2026-2" con estado "PAGADA"
    And inicia sesion como docente "D0002"
    When registra la nota 18.0 en la matricula consultada
    Then el codigo de respuesta es 403
    And el codigo de error es "ACCESO_DENEGADO"
    And la respuesta de error cumple el esquema "error.json"

  Scenario: No se puede registrar nota en una matricula que sigue impaga
    Given que Rosa consulta la API de matricula
    And inicia sesion como alumna "i202610005"
    When busca la matricula del curso "PS5511" en periodo "2026-2" con estado "REGISTRADA"
    And inicia sesion como docente "D0001"
    When registra la nota 18.0 en la matricula consultada
    Then el codigo de respuesta es 409
    And el codigo de error es "MATRICULA_NO_PAGADA"
    And la respuesta de error cumple el esquema "error.json"
