package ec.edu.ups.modelo;

public class PreguntaSeguridad {
    private final String clave;

    /**
     * @param clave la clave que usarás en el bundle, p.e. "security.q1"
     */
    public PreguntaSeguridad(String clave) {
        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La clave no puede estar vacía");
        }
        this.clave = clave;
    }

    /** @return la clave para pasar a mih.get(...) */
    public String getClave() {
        return clave;
    }

    @Override
    public String toString() {
        return clave;
    }

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
