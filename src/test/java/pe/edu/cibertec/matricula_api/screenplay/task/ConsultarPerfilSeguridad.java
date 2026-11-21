package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarPerfilSeguridad implements Task {

    private final String llaveToken;

    private ConsultarPerfilSeguridad(String llaveToken) {
        this.llaveToken = llaveToken;
    }

    public static ConsultarPerfilSeguridad conToken(String llaveToken) {
        return new ConsultarPerfilSeguridad(llaveToken);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/auth/perfil")
                        .with(request -> request.header("Authorization", "Bearer " + actor.recall(llaveToken)))
        );
    }
}