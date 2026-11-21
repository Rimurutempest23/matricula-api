package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class MatricularAlumno implements Task {

    private final String alumno;
    private final String curso;

    private MatricularAlumno(String alumno, String curso) {
        this.alumno = alumno;
        this.curso = curso;
    }

    public static MatricularAlumno en(String alumno, String curso) {
        return new MatricularAlumno(alumno, curso);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("/api/matriculas")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .auth().oauth2(actor.recall("tokenMatricula"))
                                .body(Map.of("codigoAlumno", alumno, "codigoCurso", curso)))
        );
    }
}
