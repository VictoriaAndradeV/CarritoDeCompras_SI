package ec.edu.ups.dao;

import ec.edu.ups.modelo.Carrito;

import java.util.List;
/**
 * Interfaz para manejar el almacenamiento y listado de carritos de compras.
 * Aquí definimos cómo crear, buscar, eliminar y listar carritos,
 * tanto a nivel general como por usuario.
 */
public interface CarritoDAO {
    /**
     * Guarda un nuevo carrito en el sistema.
     *
     * @param carrito instancia de {@link Carrito} a crear
     */
    void crear(Carrito carrito);
    /**
     * Busca un carrito según su código único.
     *
     * @param codigo código del carrito
     * @return el carrito encontrado, o null si no existe
     */
    Carrito buscarPorCodigo(int codigo);
    /**
     * Elimina todos los ítems dentro del carrito, dejándolo vacío.
     *
     * @param carrito carrito al que se le limpian los contenidos
     */
    void limpiar(Carrito carrito);
    /**
     * Borra por completo el carrito identificado por código.
     *
     * @param codigo código del carrito a eliminar
     */
    void eliminar(int codigo);
    /**
     * Obtiene la lista de todos los carritos registrados.
     *
     * @return lista completa de carritos (uso administrativo)
     */
    List<Carrito> listarTodos(); //solo para admin
    /**
     * Recupera los carritos pertenecientes a un usuario.
     *
     * @param nombreDeUsuario nombre de usuario dueño de los carritos
     * @return lista de carritos para ese usuario
     */
    List<Carrito> listarPorUsuario(String nombreDeUsuario);
    /**
     * Elimina todos los carritos asociados a un usuario.
     *
     * @param nombreDeUsuario nombre de usuario cuyos carritos se borran
     */
    void eliminarPorUsuario(String nombreDeUsuario);
}
