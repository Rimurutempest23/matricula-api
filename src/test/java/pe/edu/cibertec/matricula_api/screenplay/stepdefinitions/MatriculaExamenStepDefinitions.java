package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import pe.edu.cibertec.matricula_api.screenplay.questions.LaRespuesta;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarPagos;
import pe.edu.cibertec.matricula_api.screenplay.task.CotizarCurso;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesionAlumno;
import pe.edu.cibertec.matricula_api.screenplay.task.MatricularAlumno;
import pe.edu.cibertec.matricula_api.screenplay.task.PagarMatricula;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class MatriculaExamenStepDefinitions {

    @And("inicia sesion como alumna {string}")
    public void iniciaSesion(String codigo) {
        Actor actor = theActorInTheSpotlight();
        actor.remember("codigoAlumno", codigo);
        actor.attemptsTo(IniciarSesionAlumno.conCodigo(codigo));
    }

    @When("cotiza el curso {string} para la alumna {string}")
    public void cotizaElCurso(String curso, String alumno) {
        theActorInTheSpotlight().attemptsTo(CotizarCurso.para(alumno, curso));
    }

    @When("matricula a {string} en el curso {string}")
    public void matriculaAlCurso(String alumno, String curso) {
        Actor actor = theActorInTheSpotlight();
        actor.attemptsTo(MatricularAlumno.en(alumno, curso));
        if (SerenityRest.lastResponse().statusCode() == 201) {
            actor.remember("idMatricula", SerenityRest.lastResponse().jsonPath().getLong("id"));
        }
    }

    @When("paga esa matricula con {string}")
    public void pagaLaMatricula(String metodo) {
        theActorInTheSpotlight().attemptsTo(PagarMatricula.con(metodo));
    }

    @When("consulta sus pagos con metodo {string}")
    public void consultaSusPagos(String metodo) {
        String alumno = theActorInTheSpotlight().recall("codigoAlumno");
        theActorInTheSpotlight().attemptsTo(ConsultarPagos.de(alumno, metodo));
    }

    @And("la cotizacion devuelve {int} creditos")
    public void validaCreditos(int creditos) {
        theActorInTheSpotlight().should(seeThat(
                actor -> SerenityRest.lastResponse().jsonPath().getInt("creditos"), equalTo(creditos)));
    }

    @And("el subtotal cotizado es {double}")
    public void validaSubtotal(double subtotal) {
        theActorInTheSpotlight().should(seeThat(
                actor -> SerenityRest.lastResponse().jsonPath().getDouble("subtotal"), equalTo(subtotal)));
    }

    @And("el descuento cotizado es {int} por ciento")
    public void validaDescuento(int descuento) {
        theActorInTheSpotlight().should(seeThat(
                actor -> SerenityRest.lastResponse().jsonPath().getInt("descuento"), equalTo(descuento)));
    }

    @And("el total cotizado es {double}")
    public void validaTotal(double total) {
        theActorInTheSpotlight().should(seeThat(
                actor -> SerenityRest.lastResponse().jsonPath().getDouble("total"), equalTo(total)));
    }

    @And("el monto de la matricula es {double}")
    public void validaMontoMatricula(double monto) {
        theActorInTheSpotlight().should(seeThat(actor -> SerenityRest.lastResponse().jsonPath().getDouble("monto"), equalTo(monto)));
    }

    @And("el estado de la matricula es {string}")
    public void validaEstadoMatricula(String estado) {
        theActorInTheSpotlight().should(seeThat(LaRespuesta.campo("estado"), equalTo(estado)));
    }

    @And("la fecha de pago tiene valor")
    public void validaFechaPago() {
        theActorInTheSpotlight().should(seeThat(
                actor -> SerenityRest.lastResponse().jsonPath().getString("fechaPago"),
                not(isEmptyOrNullString())));
    }

    @And("el pago consultado pertenece a la matricula creada")
    public void validaPagoDeMatricula() {
        theActorInTheSpotlight().should(seeThat(actor -> pagoDeLaMatricula(actor) != null, is(true)));
    }

    @And("el pago registra el metodo {string}")
    public void validaMetodoPago(String metodo) {
        theActorInTheSpotlight().should(seeThat(
                actor -> pagoDeLaMatricula(actor).get("metodo").toString(), equalTo(metodo)));
    }

    @And("el numero de operacion tiene el formato {string}")
    public void validaNumeroOperacion(String formato) {
        String patron = formato.replace("000000", "[0-9]{6}");
        patron = "^" + patron + "$";
        theActorInTheSpotlight().should(seeThat(
                actor -> pagoDeLaMatricula(actor).get("numeroOperacion").toString(),
                matchesPattern(patron)));
    }

    private Map<String, Object> pagoDeLaMatricula(Actor actor) {
        Long idEsperado = actor.recall("idMatricula");
        List<Map<String, Object>> pagos = SerenityRest.lastResponse().jsonPath().getList("$");
        return pagos.stream()
                .filter(pago -> ((Number) pago.get("idMatricula")).longValue() == idEsperado)
                .findFirst()
                .orElse(null);
    }
}




