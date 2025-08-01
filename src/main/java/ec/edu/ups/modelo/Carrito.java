package ec.edu.ups.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
//buscar carrito y listar carrito, eliminar carrito
public class Carrito {

    private final double IVA = 0.12;
    private static int contador = 1;// compartido entre todas las instancias, objetos
    //si no ponemos statico, siempre va a valer 1, pertenece a la clase no a la instancia
    //cuando creo un nuevo producto, un nuevo codigo se genera para el mismo.
    private int codigo;
    private Usuario usuario;

    private GregorianCalendar fechaCreacion;

    private List<ItemCarrito> items;

    public Carrito() {
        codigo = contador++;
        items = new ArrayList<>();
        fechaCreacion = new GregorianCalendar();
    }

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

    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    public void eliminarItem(int codigoProducto) {
        items.removeIf(item -> item.getProducto().getCodigo() == codigoProducto);
    }

    public void actualizarCantidad(int codigoProducto, int nuevaCantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(nuevaCantidad);
                break;
            }
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }

    public double calcularIVA() {
        double subtotal = calcularSubtotal();
        return subtotal * IVA;
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
