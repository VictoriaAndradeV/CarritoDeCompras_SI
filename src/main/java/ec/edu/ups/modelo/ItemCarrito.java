package ec.edu.ups.modelo;

/**
 * Los items son los elementos de los que está
 * conformado un carrito
 */
public class ItemCarrito {
    private Producto producto;
    private int cantidad;
    /**
     * Construye un ítem de carrito con producto y cantidad.
     *
     * @param producto Producto a agregar.
     * @param cantidad Cantidad de unidades (>0).
     */
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
    /**
     * Asigna el producto del ítem.
     *
     * @param producto Nuevo producto.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    /**
     * Asigna la cantidad de unidades.
     *
     * @param cantidad Nueva cantidad.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    /**
     * Obtiene el producto asociado.
     *
     * @return Producto del ítem.
     */
    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }
    /**
     * Calcula el subtotal para este ítem (precio unitario × cantidad).
     *
     * @return Subtotal como double.
     */
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return producto.toString() + " x " + cantidad + " = $" + getSubtotal();
    }

}

