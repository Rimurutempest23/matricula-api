package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesionSeguridad;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPerfilSeguridad;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesionSeguridad;
import pe.edu.cibertec.matricula_api.screenplay.task.ListarAlumnosSeguridad;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarSesion;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class SeguridadStepDefinitions {

    @And("inicia sesion con el usuario {string} y clave {string}")
    public void iniciaSesion(String usuario, String clave) {
        theActorInTheSpotlight().attemptsTo(IniciarSesionSeguridad.con(usuario, clave));
    }

    @When("renueva la sesion con el refresh token original")
    public void renuevaConElRefreshOriginal() {
        theActorInTheSpotlight().attemptsTo(new RenovarSesion());
    }

    @When("reutiliza el refresh token original")
    public void reutilizaElRefreshOriginal() {
        theActorInTheSpotlight().attemptsTo(new RenovarSesion());
    }

    @When("consulta su perfil con el access token renovado")
    public void consultaPerfilConTokenRenovado() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfilSeguridad.conToken("tokenAccesoRenovado"));
    }

    @When("cierra su sesion")
    public void cierraSesion() {
        theActorInTheSpotlight().attemptsTo(CerrarSesionSeguridad.conToken("tokenAccesoSeguridad"));
    }

    @When("consulta su perfil con el mismo access token")
    public void consultaPerfilConElMismoToken() {
        theActorInTheSpotlight().attemptsTo(ConsultarPerfilSeguridad.conToken("tokenAccesoSeguridad"));
    }

    @When("Jorge intenta iniciar sesion con esta secuencia")
    public void intentaConEstaSecuencia(DataTable tabla) {
        Actor actor = theActorInTheSpotlight();
        List<Map<String, String>> intentos = tabla.asMaps(String.class, String.class);
        for (Map<String, String> intento : intentos) {
            actor.attemptsTo(IniciarSesionSeguridad.con(intento.get("usuario"), intento.get("clave")));
            int status = Integer.parseInt(intento.get("status"));
            String codigo = intento.get("codigo");
            validaError(status, codigo, "intento de acceso");
        }
    }

    @When("intenta ingresar con la clave correcta para {string}")
    public void intentaConClaveCorrecta(String usuario) {
        theActorInTheSpotlight().attemptsTo(IniciarSesionSeguridad.con(usuario, "Cibertec2026"));
    }

    @When("intenta listar todos los alumnos")
    public void intentaListarAlumnos() {
        theActorInTheSpotlight().attemptsTo(new ListarAlumnosSeguridad());
    }

    @And("la respuesta de error cumple el esquema {string}")
    public void respuestaErrorCumpleEsquema(String esquema) {
        theActorInTheSpotlight().should(
                seeThatResponse("la respuesta de error cumple " + esquema,
                        response -> response.body(matchesJsonSchemaInClasspath("schemas/" + esquema)))
        );
    }

    private void validaError(int status, String codigo, String descripcion) {
        Actor actor = theActorInTheSpotlight();
        actor.should(seeThat(LaRespuesta.codigoDeEstado(), equalTo(status)));
        actor.should(seeThat(LaRespuesta.campo("codigo"), equalTo(codigo)));
        actor.should(seeThatResponse(descripcion,
                response -> response.body(matchesJsonSchemaInClasspath("schemas/error.json"))));
    }
}