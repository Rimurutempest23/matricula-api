package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarPagos implements Task {

    private final String alumno;
    private final String metodo;

    private ConsultarPagos(String alumno, String metodo) {
        this.alumno = alumno;
        this.metodo = metodo;
    }

    public static ConsultarPagos de(String alumno, String metodo) {
        return new ConsultarPagos(alumno, metodo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/pagos?alumno=" + alumno + "&metodo=" + metodo)
                        .with(request -> request.auth().oauth2(actor.recall("tokenMatricula")))
        );
    }
}
