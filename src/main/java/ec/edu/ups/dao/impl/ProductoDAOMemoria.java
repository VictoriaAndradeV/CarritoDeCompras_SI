package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOMemoria implements ProductoDAO {
    private List<Producto> productos;

    public ProductoDAOMemoria() {
        productos = new ArrayList<Producto>();
        crear(new Producto(123, "Manzana", 0.25));
        crear(new Producto(456, "Pera", 0.20));
        crear(new Producto(789, "Chocolate Lindt", 2.50));
        crear(new Producto(159, "Leche entera", 0.50));
    }

    @Override
    public void crear(Producto producto) {
        productos.add(producto);
    }

    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    //metodo para buscar el producto
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().startsWith(nombre)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                break;
            }
        }
    }

    @Override
    public void eliminar(int codigo) {
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
            }
        }
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}