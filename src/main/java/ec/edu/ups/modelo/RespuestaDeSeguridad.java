package ec.edu.ups.modelo;

/**
 * Representa la respuesta que un usuario proporciona a una pregunta de seguridad,
 * asociando la pregunta original con la respuesta dada.
 *
 * <p>Esta clase almacena tanto la instancia de {@link PreguntaSeguridad} como
 * la cadena de texto que el usuario introdujo para cada pregunta. Se utiliza
 * cuando el usuario desea recuperar su contraseña, y validar que sus respuestas
 * coinicidan con las ingresadas.</p>
 */
public class RespuestaDeSeguridad {
    private final PreguntaSeguridad pregunta;
    /**
     * Texto de la respuesta proporcionada por el usuario.
     */
    private final String respuesta;
    /**
     * Construye una nueva respuesta de seguridad.
     * @param pregunta Pregunta de seguridad asociada (no puede ser null).
     * @param respuesta Texto de la respuesta, lo ingresado por el usuario
     */
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
