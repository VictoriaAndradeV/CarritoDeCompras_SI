package ec.edu.ups.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
/**
 * Controlador encargado de gestionar la lógica de creación, edición y administración
 * de un carrito de compras para un usuario autenticado.
 * <p>
 * Coordina la interacción entre la vista de carrito,
 * los DAOs de carrito, usuario y producto, y las vistas para listar carritos
 * tanto para usuario como para administrador.
 * Utiliza internacionalización mediante {@link MensajeInternacionalizacionHandler}
 * para mostrar mensajes traducidos.
 * </p>
 */
public class FormateadorUtils {
    /**
     * Formatea un valor numérico como moneda de acuerdo al locale proporcionado.
     *
     * @param cantidad Monto a formatear.
     * @param locale   Configuración regional que determina el símbolo de moneda y formato.
     * @return Cadena con el monto formateado, incluyendo el símbolo de moneda y dos decimales.
     */
    public static String formatearMoneda(double cantidad, Locale locale) {
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(locale);
        formatoMoneda.setMinimumFractionDigits(2);
        formatoMoneda.setMaximumFractionDigits(2);
        return formatoMoneda.format(cantidad);
    }
    /**
     * Formatea un objeto Date a cadena utilizando el estilo MEDIUM de fecha según el locale.
     *
     * @param fecha  Fecha a formatear.
     * @param locale Configuración regional que determina el formato de fecha.
     * @return Cadena con la fecha formateada.
     */
    public static String formatearFecha(Date fecha, Locale locale) {
        DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        return formato.format(fecha);
    }
    /**
     * Parsea una cadena de texto a objeto Date según el estilo MEDIUM y el locale.
     *
     * @param fechaStr Cadena con la fecha en formato MEDIUM.
     * @param locale   Configuración regional que determina el formato de parseo.
     * @return Objeto Date resultante del parseo.
     * @throws ParseException Si la cadena no coincide con el formato esperado.
     */
    public static Date parsearFecha(String fechaStr, Locale locale) throws ParseException {
        DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        return formato.parse(fechaStr);
    }
}
