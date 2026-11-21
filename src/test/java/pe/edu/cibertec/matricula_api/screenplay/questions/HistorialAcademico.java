package pe.edu.cibertec.matricula_api.screenplay.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

import java.util.List;
import java.util.Map;

public class HistorialAcademico {

    public static Question<Double> notaDelCurso(String codigoCurso) {
        return Question.about("la nota de " + codigoCurso + " en el historial")
                .answeredBy(actor -> ((Number) curso(codigoCurso).get("nota")).doubleValue());
    }

    public static Question<String> condicionDelCurso(String codigoCurso) {
        return Question.about("la condicion de " + codigoCurso + " en el historial")
                .answeredBy(actor -> curso(codigoCurso).get("condicion").toString());
    }

    public static Question<Integer> creditosAprobados() {
        return Question.about("los creditos aprobados del historial")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getInt("creditosAprobados"));
    }

    public static Question<Double> promedioPonderado() {
        return Question.about("el promedio ponderado del historial")
                .answeredBy(actor -> SerenityRest.lastResponse().jsonPath().getDouble("promedioPonderado"));
    }

    private static Map<String, Object> curso(String codigoCurso) {
        List<Map<String, Object>> cursos = SerenityRest.lastResponse().jsonPath().getList("cursos");
        return cursos.stream()
                .filter(item -> codigoCurso.equals(item.get("codigoCurso")))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No aparece " + codigoCurso + " en el historial"));
    }
}
