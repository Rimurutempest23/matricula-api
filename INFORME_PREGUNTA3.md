# Pregunta 3: Calificaciones e historial

## Datos y resultados esperados

Rosa Medina tiene dos calificaciones previas: LP5401, 15 con 4 creditos, y AS5402, 16 con 3 creditos. PI5620 aporta 6 creditos. El promedio ponderado considera los 13 creditos con nota:

- Con PI5620 = 10: `(15*4 + 16*3 + 10*6) / 13 = 168 / 13 = 12.92`. PI5620 queda DESAPROBADO y Rosa conserva 7 creditos aprobados.
- Con PI5620 = 12.5: `(15*4 + 16*3 + 12.5*6) / 13 = 183 / 13 = 14.08`. Como 12.5 es menor que la nota aprobatoria 13, PI5620 queda DESAPROBADO y los creditos aprobados siguen en 7.

La fila de 12.5 se envia como numero decimal JSON. La API conserva la fraccion porque la nota se recibe como `Double`; no debe convertirse a entero ni truncarse.

## Reglas de rechazo

- D0002 no dicta PI5620, por lo que la API debe responder 403 `ACCESO_DENEGADO`.
- PS5511 de Rosa esta en estado REGISTRADA, sin pago. Al intentar calificarla, la API debe responder 409 `MATRICULA_NO_PAGADA`.

El ID de matricula se consulta en `GET /api/matriculas` con el token de Rosa y se guarda en memoria del actor; no queda fijado en el feature.

## Nota sobre el codigo del docente

El enunciado escribe D00001; la semilla real del proyecto registra al docente como D0001. El feature usa el codigo existente en la API.
