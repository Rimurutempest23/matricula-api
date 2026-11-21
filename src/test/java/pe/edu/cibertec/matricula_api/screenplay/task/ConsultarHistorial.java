package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarHistorial implements Task {

    private final String codigoAlumno;

    private ConsultarHistorial(String codigoAlumno) {
        this.codigoAlumno = codigoAlumno;
    }

    public static ConsultarHistorial de(String codigoAlumno) {
        return new ConsultarHistorial(codigoAlumno);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/alumnos/" + codigoAlumno + "/historial")
                        .with(request -> request.header("Authorization", "Bearer " + actor.recall("tokenMatricula")))
        );
    }
}
