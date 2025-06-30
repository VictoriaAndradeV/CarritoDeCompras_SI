package ec.edu.ups.util;

import java.util.Locale;

public class IdiomaUsado {
    private final Locale locale;
    private final String nombreIdioma;

    public IdiomaUsado(Locale locale, String nombreIdioma) {
        this.locale = locale;
        this.nombreIdioma = nombreIdioma;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getNombreIdioma() {
        return nombreIdioma;
    }

    @Override
    public String toString() {
        return nombreIdioma;
    }
}
