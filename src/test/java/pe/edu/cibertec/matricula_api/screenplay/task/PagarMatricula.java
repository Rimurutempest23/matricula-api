package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

public class PagarMatricula implements Task {

    private final String metodo;

    private PagarMatricula(String metodo) {
        this.metodo = metodo;
    }

    public static PagarMatricula con(String metodo) {
        return new PagarMatricula(metodo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Long idMatricula = actor.recall("idMatricula");
        actor.attemptsTo(
                Patch.to("/api/matriculas/" + idMatricula + "/pagar")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .auth().oauth2(actor.recall("tokenMatricula"))
                                .body(Map.of("metodo", metodo)))
        );
    }
}
