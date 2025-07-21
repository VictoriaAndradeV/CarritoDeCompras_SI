package ec.edu.ups.modelo;

import ec.edu.ups.util.*;

/**
 * Representa un producto en el sistema de gestión de compras.
 *
 * <p>
 * Cada producto está identificado por un código único, posee un nombre y un precio.
 * Incluye validaciones de formato y rangos para cada campo, y utiliza un
 * manejador de internacionalización para mensajes de error.</p>
 *
 */
public class Producto {
    private int codigo;
    private String nombre;
    private double precio;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Constructor por defecto. Inicializa el manejador de internacionalización
     * con idioma español y región EC.
     */
    public Producto() {
        this.mih = new MensajeInternacionalizacionHandler("es", "EC");
    }

    public Producto(int codigo, String nombre, double precio) throws ExcepcionProductoCodigo, ExcepcionProductoNombre,
                    ExcepcionProductoPrecio{
        this.mih = new MensajeInternacionalizacionHandler("es", "EC");
        setCodigo(codigo);
        setNombre(nombre);
        setPrecio(precio);
    }
    /**
     * Valida y asigna el código del producto.
     *
     * @param codigo Código a asignar.
     * @throws ExcepcionProductoCodigo Si es ≤ 0 o > 9999.
     */
    public void setCodigo(int codigo) throws ExcepcionProductoCodigo {
        //se verifica que el codigo del producto sea POSITIVO
        if (codigo <= 0) {
            throw new ExcepcionProductoCodigo(mih.get("excepcion.produc.codigo"));
        }
        //LIMITE DE TAMAÑO DEL CODIGO
        if (codigo > 9999) {
            throw new ExcepcionProductoCodigo(mih.get("excepcion.produc.longi"));
        }
        this.codigo = codigo;
    }
    /**
     * Valida y asigna el nombre del producto.
     *
     * @param nombre Nombre a asignar.
     * @throws ExcepcionProductoNombre Si es nulo, vacío o contiene caracteres inválidos.
     */
    public void setNombre(String nombre) throws ExcepcionProductoNombre {
        if(nombre == null || nombre.isEmpty()) {
            throw new ExcepcionProductoNombre(mih.get("excepcion.produc.nom"));
        }
        String n = nombre.toLowerCase().trim();
        //\p{L} incluye (A–Z, a–z, á, é, ñ, ü, etc)
        if (!n.matches("^[\\p{L}0-9 ]+$")) {
            throw new ExcepcionProductoNombre(mih.get("excepcion.produc.carac"));
        }
        this.nombre = n;
    }
    /**
     * Valida y asigna el precio a partir de una cadena.
     *
     * @param precio Texto con formato decimal (hasta dos decimales).
     * @throws ExcepcionProductoPrecio Si es nulo, contiene coma, formato inválido o ≤ 0.
     */
    public void setPrecio(String precio) throws ExcepcionProductoPrecio {
        //cuando me da nulo o si no se registró un espacio vacio por ejemplo
        if (precio == null || precio.trim().isEmpty()) {
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.vacio"));
        }
        String s = precio.trim();

        if (s.contains(",")) {//no se permiten usos de comas
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.uso.punto"));
        }
        //^\d+ -> uno o más dígitos antes del punto
        //(?:\.\d{1,2})? verifica punto seguido despues de dos digitos
        if (!s.matches("^\\d+(?:\\.\\d{1,2})?$")) {
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.longitud"));
        }
        double valor; //convierte texto a numero
        try {
            valor = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.invalido"));
        }

        if (valor <= 0) {//valor debe ser >0
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.mayor"));
        }
        this.precio = valor;//le asigno valor al parametro, pero ahora es double
    }
    /**
     * Valida y asigna el precio como valor numérico.
     *
     * @param valor Precio a asignar.
     * @throws ExcepcionProductoPrecio Si es ≤ 0.
     */
    public void setPrecio(double valor) throws ExcepcionProductoPrecio {
        if (valor <= 0) {
            throw new ExcepcionProductoPrecio(mih.get("excepcion.precio.mayor"));
        }
        this.precio = valor;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }
}