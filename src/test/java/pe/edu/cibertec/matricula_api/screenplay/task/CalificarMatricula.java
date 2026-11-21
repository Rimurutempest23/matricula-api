package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class CalificarMatricula implements Task {

    private final Double nota;

    private CalificarMatricula(Double nota) {
        this.nota = nota;
    }

    public static CalificarMatricula conNota(Double nota) {
        return new CalificarMatricula(nota);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Long id = actor.recall("idMatriculaCalificar");
        actor.attemptsTo(
                Patch.to("/api/matriculas/" + id + "/nota")
                        .with(request -> request
                                .header("Authorization", "Bearer " + actor.recall("tokenAccesoSeguridad"))
                                .contentType(ContentType.JSON)
                                .body(Map.of("nota", nota)))
        );
    }
}
