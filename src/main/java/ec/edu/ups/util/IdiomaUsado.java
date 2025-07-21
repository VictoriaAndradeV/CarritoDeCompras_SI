package ec.edu.ups.util;

import java.util.Locale;
/**
 * Representa una opción de idioma para la interfaz de usuario, asociando
 * un {@link Locale} con un nombre legible.
 * <p>
 * Se utiliza para poblar componentes de selección de idioma (por ejemplo,
 * un JComboBox), de modo que el usuario vea el nombre del idioma
 * pero internamente se trabaje con el correspondiente Locale.
 * </p>
 */
public class IdiomaUsado {
    /** Locale asociado a esta opción de idioma. */
    private final Locale locale;
    /** Nombre legible del idioma, mostrado en la interfaz. */
    private final String nombreIdioma;
    /**
     * Construye una nueva opción de idioma.
     *
     * @param locale        Objeto Locale que identifica la configuración regional.
     * @param nombreIdioma  Nombre del idioma que se mostrará en la UI.
     */
    public IdiomaUsado(Locale locale, String nombreIdioma) {
        this.locale = locale;
        this.nombreIdioma = nombreIdioma;
    }
    /**
     * @return Locale asociado a este idioma.
     */
    public Locale getLocale() {
        return locale;
    }
    /**
     * Devuelve el nombre del idioma. Se utiliza, por ejemplo, en JComboBox
     * para mostrar la opción.
     *
     * @return Nombre legible del idioma.
     */
    @Override
    public String toString() {
        return nombreIdioma;
    }
}
