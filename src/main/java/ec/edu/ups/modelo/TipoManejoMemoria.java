package ec.edu.ups.modelo;
/**
 * Enums que define las diferentes opciones de manejo de datos en la aplicación,
 * indicando si los mismos se almacenan y procesan únicamente en memoria o se guardan en disco
 * mediante archivos de texto o binarios.
 *
 * <p>Se utiliza para configurar el modo de persistencia de las entidades del sistema
 * (por ejemplo, usuarios, productos, carritos).</p>
 */
public enum TipoManejoMemoria {
    /**
     * Los datos se gestionan completamente en memoria. No se realiza ninguna operación de
     * lectura o escritura en disco. La información es volátil.
     */
    MANEJO_MEMORIA,

    /**
     * Los datos se persisten en archivos de texto.
     */
    MANEJO_ARCHIVO_TEXTO,
    /**
     * Los datos se persisten en archivos binarios. Proporciona una mayor eficiencia en las
     * operaciones de lectura/escritura, su contenido no es legible directamente.
     */
    MANEJO_ARCHIVO_BINARIO;
}
