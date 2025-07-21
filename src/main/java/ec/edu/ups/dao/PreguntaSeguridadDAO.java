package ec.edu.ups.dao;

import ec.edu.ups.modelo.PreguntaSeguridad;

import java.util.List;
/**
 * Interfaz para gestionar las preguntas de seguridad del sistema.
 * Aquí definimos cómo obtener todas las preguntas disponibles
 * y cómo seleccionar las aleatorias
 *<p>
 * - listarTodas: devuelve todas las preguntas que hay en el banco.
 * - seleccionarAleatorias: toma un número y devuelve esa cantidad de preguntas
 *   elegidas al azar.</p>
 *
 */
public interface PreguntaSeguridadDAO {
    /**
     * Obtiene la lista completa de preguntas de seguridad.
     *
     * @return lista con todas las instancias de {@link PreguntaSeguridad}
     */
    List<PreguntaSeguridad> listarTodas(); //lista de preguntas

    /**
     * Selecciona aleatoriamente un conjunto de preguntas del banco.
     *
     * @param n cantidad de preguntas a seleccionar
     * @return lista con n preguntas elegidas al azar
     */
    List<PreguntaSeguridad> seleccionarAleatorias(int n);
}
