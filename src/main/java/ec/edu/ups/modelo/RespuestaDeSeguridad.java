package ec.edu.ups.modelo;

//guarda la respuesta que el usuario responde a una pregunta concreta
public class RespuestaDeSeguridad {
    private final PreguntaSeguridad pregunta;
    private final String respuesta;

    public RespuestaDeSeguridad(PreguntaSeguridad pregunta, String respuesta) {
        this.pregunta  = pregunta;
        this.respuesta = respuesta;
    }

    //getters
    public PreguntaSeguridad getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    @Override
    public String toString() {
        return "RespuestaSeguridad{" +
                "pregunta=" + pregunta.getClave() +
                ", respuesta='" + respuesta + '\'' +
                '}';
    }
}
