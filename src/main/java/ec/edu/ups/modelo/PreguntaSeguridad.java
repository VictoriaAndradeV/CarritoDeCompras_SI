package ec.edu.ups.modelo;

/**
 * Representa una pregunta de seguridad utilizada para recuperar contraseña
 *
 * <p>
 * Cada instancia contiene una clave que nos ayuda a identificar la pregunta
 * en (mih). La clave no puede ser nula ni vacía.
 * </p>
 */
public class PreguntaSeguridad {
    private final String clave;
    /**
     * Crea una nueva pregunta de seguridad.
     *
     * @param clave Clave que identifica la pregunta en el manejador de mensajes.
     * @throws IllegalArgumentException Si la clave es nula o está en blanco.
     */
    public PreguntaSeguridad(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La clave no puede estar vacía");
        }
        this.clave = clave;
    }

    /**
     * Obtiene la clave asociada a la pregunta de seguridad.
     *
     * @return Clave para usar en mih.get(...).
     */
    public String getClave() {
        return clave;
    }

    @Override
    public String toString() {
        return clave;
    }
    /**
     * Compara esta pregunta con otro objeto.
     *
     * @param o -> Objeto a comparar
     * @return true si ambos son instancias de PreguntaSeguridad y tienen la misma clave.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreguntaSeguridad)) return false;
        PreguntaSeguridad that = (PreguntaSeguridad) o;
        return clave.equals(that.clave);
    }

    @Override
    public int hashCode() {
        return clave.hashCode();
    }
}
