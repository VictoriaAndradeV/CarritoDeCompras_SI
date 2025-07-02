package ec.edu.ups.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MensajeInternacionalizacionHandler {
    private ResourceBundle bundle;
    private Locale locale;

     //Constructor: carga el ResourceBundle para el lenguaje y país indicados.
     public MensajeInternacionalizacionHandler(String lenguaje, String pais) {
         this.locale = new Locale(lenguaje, pais);
         this.bundle = ResourceBundle.getBundle("mensajes", locale);
         System.out.println(">> Bundle cargado: baseName='mensajes', locale=" + locale
                 + ", contiene modificarP.titulo? "
                 + bundle.containsKey("modificarP.titulo"));
     }

    /*
     * Al llamar a getString(key), Java busca en el archivo .properties
     *  la línea que empiece con key=
     *  y devuelve lo que haya a la derecha del signo =

     *  Si el .properties no contiene esa clave, getString
        lanza una excepción de tipo MissingResourceException
     */
    public String get(String key) { //"puerta de acceso" a los bundles
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "[" + key + "]";
        }
    }

     //Cambia el idioma/país del handler y recarga el ResourceBundle.
    public void setLenguaje(String lenguaje, String pais) {
        this.locale = new Locale(lenguaje, pais);
        this.bundle = ResourceBundle.getBundle("mensajes", locale);
    }

    public Locale getLocale() {
        return locale;
    }
}
