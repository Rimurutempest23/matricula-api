package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

public class CerrarSesionSeguridad implements Task {

    private final String llaveToken;

    private CerrarSesionSeguridad(String llaveToken) {
        this.llaveToken = llaveToken;
    }

    public static CerrarSesionSeguridad conToken(String llaveToken) {
        return new CerrarSesionSeguridad(llaveToken);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/auth/logout")
                        .with(request -> request.header("Authorization", "Bearer " + actor.recall(llaveToken)))
        );
    }
}