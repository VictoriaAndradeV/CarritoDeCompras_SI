package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.ExcepcionProductoCodigo;
import ec.edu.ups.util.ExcepcionProductoNombre;
import ec.edu.ups.util.ExcepcionProductoPrecio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Implementación en memoria de {@link ProductoDAO}.
 * Mantiene una lista de productos y permite operaciones básicas:
 * crear, buscar, actualizar, eliminar y listar.
 *
 * En el constructor se precargan algunos productos de ejemplo,
 * capturando posibles errores de validación.
 *
 * @author Victoria
 */
public class ProductoDAOMemoria implements ProductoDAO {
    /** Lista interna que almacena los productos. */
    private List<Producto> productos;
    /**
     * Inicializa el DAO y añade algunos productos de prueba.
     */
    public ProductoDAOMemoria() {
        productos = new ArrayList<>();

        try {
            crear(new Producto(123, "Manzana", 0.25));
            crear(new Producto(456, "Pera", 0.20));
            crear(new Producto(789, "Chocolate Lindt", 2.50));
            crear(new Producto(159, "Leche entera", 0.50));
        }  catch (ExcepcionProductoCodigo e) {
            System.err.println(e.getMessage());
        } catch (ExcepcionProductoNombre e) {
            System.err.println(e.getMessage());
        } catch (ExcepcionProductoPrecio e) {
            System.err.println(e.getMessage());
        }
    }
    /**
     * Añade un nuevo producto a la lista.
     *
     * @param producto instancia de {@link Producto} a guardar
     */
    @Override
    public void crear(Producto producto) {
        productos.add(producto);
    }
    /**
     * Busca un producto por su código único.
     *
     * @param codigo código del producto
     * @return el producto encontrado, o null si no existe
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Devuelve todos los productos cuyo nombre comienza con el texto dado.
     *
     * @param nombre prefijo para filtrar productos
     * @return lista de productos coincidentes
     */
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
    /**
     * Sustituye los datos de un producto existente.
     *
     * @param producto instancia de {@link Producto} con el mismo código y datos nuevos
     */
    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                break;
            }
        }
    }
    /**
     * Elimina el producto identificado por el código.
     *
     * @param codigo código del producto a eliminar
     */
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
    /**
     * Devuelve una copia de la lista de todos los productos.
     *
     * @return lista de productos registrados
     */
    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}