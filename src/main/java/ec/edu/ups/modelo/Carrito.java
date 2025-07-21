package ec.edu.ups.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que representa el carrito de compras de un usuario.
 * Aquí guardamos los productos que el usuario añade, calculamos totales
 * y manejamos la fecha de creación del carrito.
 */
public class Carrito {

    private final double IVA = 0.12;
    private static int contador = 1;// compartido entre todas las instancias, objetos
    //si no ponemos statico, siempre va a valer 1, pertenece a la clase no a la instancia
    //cuando creo un nuevo producto, un nuevo codigo se genera para el mismo.
    private int codigo;
    private Usuario usuario;

    private GregorianCalendar fechaCreacion;

    private List<ItemCarrito> items;
    /**
     * Al crear un carrito, le damos un código único, inicializamos la lista de ítems
     * y guardamos la fecha actual.
     */
    public Carrito() {
        codigo = contador++;
        items = new ArrayList<>();
        fechaCreacion = new GregorianCalendar();
    }
    /**
     * Añade un producto al carrito. Si ya existe, suma la cantidad.
     *
     * @param producto Producto a añadir
     * @param cantidad Cantidad a sumar
     */
    public void agregarProducto(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == producto.getCodigo()) {
                // Si ya está el producto, solo suma la cantidad
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // Si no existe, lo añade como nuevo
        items.add(new ItemCarrito(producto, cantidad));
    }
    /**
     * Devuelve la lista de ítems del carrito.
     */
    public List<ItemCarrito> obtenerItems() {
        return items;
    }
    /**
     * Elimina un ítem según el código de producto.
     *
     * @param codigoProducto Código del producto a quitar
     */
    public void eliminarItem(int codigoProducto) {
        items.removeIf(item -> item.getProducto().getCodigo() == codigoProducto);
    }
    /**
     * Cambia la cantidad de un producto ya presente en el carrito.
     *
     * @param codigoProducto Código del producto
     * @param nuevaCantidad   Nueva cantidad a asignar
     */
    public void actualizarCantidad(int codigoProducto, int nuevaCantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(nuevaCantidad);
                break;
            }
        }
    }
    /**
     * Calcula el subtotal sumando precio * cantidad de todos los ítems.
     */
    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }
    /**
     * Calcula el IVA sobre el subtotal.
     */
    public double calcularIVA() {
        double subtotal = calcularSubtotal();
        return subtotal * IVA;
    }
    /**
     * Ajusta la fecha de creación manualmente.
     *
     * @param dia  Día
     * @param mes  Mes (0-11)
     * @param año  Año
     */
    public void setCalender(int dia, int mes, int año){
        this.fechaCreacion.set(dia, mes, año);
    }
    /**
     * Reemplaza la lista de ítems completa.
     *
     * @param items Nueva lista de ítems
     */
    public void setItems(List<ItemCarrito> items){
        this.items = items;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }

    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Carrito{" +
                "IVA=" + IVA +
                ", codigo=" + codigo +
                ", fechaCreacion=" + fechaCreacion +
                ", items=" + items +
                '}';
    }
}
