package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ListarAlumnosSeguridad implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/alumnos")
                        .with(request -> request.header("Authorization", "Bearer " + actor.recall("tokenAccesoSeguridad")))
        );
    }
}