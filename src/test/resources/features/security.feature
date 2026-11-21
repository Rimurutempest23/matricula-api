@pregunta2
Feature: Seguridad de la sesión
  Como usuario de CIBERTEC
  Quiero comprobar cómo se protegen mis sesiones y mis permisos
  Para confirmar que los accesos se cierran y se limitan cuando corresponde

  Scenario: Renovar un refresh token lo deja inutilizable
    Given que Ana consulta la API de matricula
    And inicia sesion con el usuario "i202610001" y clave "Cibertec2026"
    When renueva la sesion con el refresh token original
    Then el codigo de respuesta es 200
    And la respuesta cumple el esquema "token.json"
    When reutiliza el refresh token original
    Then el codigo de respuesta es 401
    And el codigo de error es "REFRESH_REUTILIZADO"
    And la respuesta de error cumple el esquema "error.json"
    When consulta su perfil con el access token renovado
    Then el codigo de respuesta es 401
    And el codigo de error es "TOKEN_INVALIDO"
    And la respuesta de error cumple el esquema "error.json"

  Scenario: Cerrar sesión invalida el access token actual
    Given que Ana consulta la API de matricula
    And inicia sesion con el usuario "i202610001" y clave "Cibertec2026"
    When cierra su sesion
    Then el codigo de respuesta es 204
    When consulta su perfil con el mismo access token
    Then el codigo de respuesta es 401
    And el codigo de error es "TOKEN_INVALIDO"
    And la respuesta de error cumple el esquema "error.json"

  Scenario: Jorge queda bloqueado despues de cinco claves incorrectas
    Given que Jorge consulta la API de matricula
    When Jorge intenta iniciar sesion con esta secuencia
      | usuario     | clave            | status | codigo                 |
      | i202610004  | ClaveIncorrecta1 | 401    | CREDENCIALES_INVALIDAS |
      | i202610004  | ClaveIncorrecta2 | 401    | CREDENCIALES_INVALIDAS |
      | i202610004  | ClaveIncorrecta3 | 401    | CREDENCIALES_INVALIDAS |
      | i202610004  | ClaveIncorrecta4 | 401    | CREDENCIALES_INVALIDAS |
      | i202610004  | ClaveIncorrecta5 | 423    | CUENTA_BLOQUEADA       |
    When intenta ingresar con la clave correcta para "i202610004"
    Then el codigo de respuesta es 423
    And el codigo de error es "CUENTA_BLOQUEADA"
    And la respuesta de error cumple el esquema "error.json"

  Scenario: Un alumno no puede listar la nómina completa
    Given que Ana consulta la API de matricula
    And inicia sesion con el usuario "i202610001" y clave "Cibertec2026"
    When intenta listar todos los alumnos
    Then el codigo de respuesta es 403
    And el codigo de error es "ACCESO_DENEGADO"
    And la respuesta de error cumple el esquema "error.json"