package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class IniciarSesionSeguridad implements Task {

    private final String usuario;
    private final String clave;

    private IniciarSesionSeguridad(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public static IniciarSesionSeguridad con(String usuario, String clave) {
        return new IniciarSesionSeguridad(usuario, clave);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/auth/login")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(Map.of("username", usuario, "password", clave)))
        );

        if (SerenityRest.lastResponse().statusCode() == 200) {
            actor.remember("tokenAccesoSeguridad",
                    SerenityRest.lastResponse().jsonPath().getString("accessToken"));
            actor.remember("refreshOriginal",
                    SerenityRest.lastResponse().jsonPath().getString("refreshToken"));
        }
    }
}