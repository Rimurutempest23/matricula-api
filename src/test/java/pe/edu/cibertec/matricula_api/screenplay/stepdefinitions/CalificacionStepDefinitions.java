package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import pe.edu.cibertec.matricula_api.screenplay.questions.HistorialAcademico;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.BuscarMatriculaDeRosa;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesionAlumno;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesionSeguridad;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

public class CalificacionStepDefinitions {

    @When("busca la matricula del curso {string} en periodo {string} con estado {string}")
    public void buscaMatricula(String curso, String periodo, String estado) {
        theActorInTheSpotlight().attemptsTo(BuscarMatriculaDeRosa.delCurso(curso, periodo, estado));
    }

    @And("inicia sesion como docente {string}")
    public void iniciaSesionComoDocente(String codigo) {
        theActorInTheSpotlight().attemptsTo(IniciarSesionSeguridad.con(codigo, "Cibertec2026"));
        theActorInTheSpotlight().should(seeThat(LaRespuesta.codigoDeEstado(), equalTo(200)));
    }

    @When("registra la nota {double} en la matricula consultada")
    public void registraNota(Double nota) {
        theActorInTheSpotlight().attemptsTo(CalificarMatricula.conNota(nota));
    }

    @And("la calificacion cumple el esquema {string}")
    public void calificacionCumpleEsquema(String esquema) {
        theActorInTheSpotlight().should(seeThatResponse("la matricula calificada cumple " + esquema,
                response -> response.body(matchesJsonSchemaInClasspath("schemas/" + esquema))));
    }

    @And("la nota de la matricula es {double}")
    public void validaNotaMatricula(Double nota) {
        theActorInTheSpotlight().should(seeThat(actor -> SerenityRest.lastResponse().jsonPath().getDouble("nota"),
                equalTo(nota)));
    }

    @When("consulta el historial de {string}")
    public void consultaHistorial(String codigoAlumno) {
        theActorInTheSpotlight().attemptsTo(ConsultarHistorial.de(codigoAlumno));
    }

    @And("el historial cumple el esquema {string}")
    public void historialCumpleEsquema(String esquema) {
        theActorInTheSpotlight().should(seeThatResponse("el historial cumple " + esquema,
                response -> response.body(matchesJsonSchemaInClasspath("schemas/" + esquema))));
    }

    @And("el historial muestra en {string} la nota {double} y condicion {string}")
    public void validaCursoHistorial(String curso, Double nota, String condicion) {
        Actor actor = theActorInTheSpotlight();
        actor.should(seeThat(HistorialAcademico.notaDelCurso(curso), equalTo(nota)));
        actor.should(seeThat(HistorialAcademico.condicionDelCurso(curso), equalTo(condicion)));
    }

    @And("el historial tiene {int} creditos aprobados")
    public void validaCreditosAprobados(int creditos) {
        theActorInTheSpotlight().should(seeThat(HistorialAcademico.creditosAprobados(), equalTo(creditos)));
    }

    @And("el promedio ponderado del historial es {double}")
    public void validaPromedio(double promedio) {
        theActorInTheSpotlight().should(seeThat(HistorialAcademico.promedioPonderado(), equalTo(promedio)));
    }
}
