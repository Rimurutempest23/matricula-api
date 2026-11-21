package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import java.util.List;
import java.util.Map;

public class BuscarMatriculaDeRosa implements Task {

    private final String curso;
    private final String periodo;
    private final String estado;

    private BuscarMatriculaDeRosa(String curso, String periodo, String estado) {
        this.curso = curso;
        this.periodo = periodo;
        this.estado = estado;
    }

    public static BuscarMatriculaDeRosa delCurso(String curso, String periodo, String estado) {
        return new BuscarMatriculaDeRosa(curso, periodo, estado);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("/api/matriculas?periodo=" + periodo)
                        .with(request -> request.header("Authorization", "Bearer " + actor.recall("tokenMatricula")))
        );

        if (SerenityRest.lastResponse().statusCode() != 200) {
            throw new AssertionError("No se pudieron consultar las matriculas de Rosa");
        }

        List<Map<String, Object>> matriculas = SerenityRest.lastResponse().jsonPath().getList("$");
        Map<String, Object> encontrada = matriculas.stream()
                .filter(m -> curso.equals(m.get("codigoCurso")) && estado.equals(m.get("estado")))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "No se encontro la matricula " + curso + " con estado " + estado));
        actor.remember("idMatriculaCalificar", ((Number) encontrada.get("id")).longValue());
    }
}
