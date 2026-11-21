package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarSesion implements Task {

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/auth/refresh")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(Map.of("refreshToken", actor.recall("refreshOriginal"))))
        );

        if (SerenityRest.lastResponse().statusCode() == 200) {
            actor.remember("tokenAccesoRenovado",
                    SerenityRest.lastResponse().jsonPath().getString("accessToken"));
            actor.remember("refreshRenovado",
                    SerenityRest.lastResponse().jsonPath().getString("refreshToken"));
        }
    }
}