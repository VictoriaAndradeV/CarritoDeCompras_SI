package ec.edu.ups.dao;

import ec.edu.ups.modelo.Producto;

import java.util.List;

/**
 * Interfaz para manejar las operaciones básicas sobre productos en la aplicación.
 * Crear, buscar, modificar y eliminar productos,
 * así como listar todos los que existen.
 */
public interface ProductoDAO {
    /**
     * Agrega un producto al sistema.
     *
     * @param producto Objeto producto con sus datos
     */
    void crear(Producto producto);
    /**
     * Busca un producto por su código.
     *
     * @param codigo Identificador único del producto
     * @return el producto correspondiente o null si no existe
     */
    Producto buscarPorCodigo(int codigo);
    /**
     * Busca productos cuyo nombre contenga el texto indicado.
     *
     * @param nombre Texto a buscar en el nombre de los productos
     * @return lista de productos que coinciden
     */
    List<Producto> buscarPorNombre(String nombre);
    /**
     * Actualiza la información de un producto existente.
     *
     * @param producto Producto con los cambios realizados
     */
    void actualizar(Producto producto);
    /**
     * Elimina un producto del sistema por su código.
     *
     * @param codigo Código del producto a eliminar
     */
    void eliminar(int codigo);
    /**
     * Devuelve todos los productos registrados.
     *
     * @return lista completa de productos
     */
    List<Producto> listarTodos();
}
