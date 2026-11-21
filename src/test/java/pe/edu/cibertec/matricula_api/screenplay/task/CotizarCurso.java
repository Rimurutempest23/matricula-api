package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.List;
import java.util.Map;

public class CotizarCurso implements Task {

    private final String alumno;
    private final String curso;

    private CotizarCurso(String alumno, String curso) {
        this.alumno = alumno;
        this.curso = curso;
    }

    public static CotizarCurso para(String alumno, String curso) {
        return new CotizarCurso(alumno, curso);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/matriculas/cotizacion")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .auth().oauth2(actor.recall("tokenMatricula"))
                                .body(Map.of("codigoAlumno", alumno, "cursos", List.of(curso))))
        );
    }
}
