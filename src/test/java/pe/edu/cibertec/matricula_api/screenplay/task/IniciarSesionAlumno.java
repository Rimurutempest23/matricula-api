package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class IniciarSesionAlumno implements Task {

    private final String codigo;

    private IniciarSesionAlumno(String codigo) {
        this.codigo = codigo;
    }

    public static IniciarSesionAlumno conCodigo(String codigo) {
        return new IniciarSesionAlumno(codigo);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/auth/login")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(Map.of("username", codigo, "password", "Cibertec2026")))
        );
        String token = SerenityRest.lastResponse().jsonPath().getString("accessToken");
        actor.remember("tokenMatricula", token);
    }
}
