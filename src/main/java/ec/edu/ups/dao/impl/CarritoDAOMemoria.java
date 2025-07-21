package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Implementación en memoria de {@link CarritoDAO} para gestionar instancias de {@link Carrito}.
 * <p>
 * Utiliza una lista interna para almacenar los carritos de compra.
 * Permite crear, buscar, actualizar, eliminar y listar carritos,
 * así como operaciones específicas por usuario.
 * </p>
 */
public class CarritoDAOMemoria implements CarritoDAO{
    private final List<Carrito> carritos;
    /**
     * Constructor que inicializa la lista de carritos vacía.
     */
    public CarritoDAOMemoria() {
        this.carritos = new ArrayList<Carrito>();
    }
    /**
     * Agrega un nuevo carrito a la lista.
     *
     * @param carrito Objeto {@link Carrito} a almacenar.
     */
    @Override
    public void crear(Carrito carrito) {
        carritos.add(carrito);
    }
    /**
     * Busca un carrito por su código único.
     *
     * @param codigo Identificador del carrito a buscar.
     * @return El carrito encontrado o {@code null} si no existe.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        for (Carrito carrito : carritos) {
            if (carrito.getCodigo() == codigo) {
                return carrito;
            }
        }
        return null;
    }
    /**
     * Actualiza el carrito existente con el mismo código,
     * reemplazando su contenido (por ejemplo, vaciándolo o modificándolo).
     *
     * @param carrito Carrito con código coincidente y nuevos datos.
     */
    @Override
    public void limpiar(Carrito carrito) {
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                break;
            }
        }
    }
    /**
     * Elimina el carrito identificado por el código dado.
     *
     * @param codigo Código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        Iterator<Carrito> iterator = carritos.iterator();
        while (iterator.hasNext()) {
            Carrito carrito = iterator.next();
            if (carrito.getCodigo() == codigo) {
                iterator.remove();
            }
        }
    }
    /**
     * Devuelve todos los carritos almacenados.
     *
     * @return Lista de {@link Carrito} en memoria.
     */
    @Override
    public List<Carrito> listarTodos() {
        return carritos;
    }
    /**
     * Lista todos los carritos asociados a un usuario específico.
     *
     * @param cedulaDeUsuario Cédula del usuario que posee los carritos.
     * @return Lista de carritos pertenecientes a ese usuario.
     */
    @Override
    public List<Carrito> listarPorUsuario(String cedulaDeUsuario) {
        List<Carrito> resultado = new ArrayList<>();
        for (Carrito c : carritos) {
            // c.getUsuario().getUsuario() devuelve el nombre de usuario
            if (c.getUsuario().getCedula().equals(cedulaDeUsuario)) {
                resultado.add(c);
            }
        }
        return resultado;
    }
    /**
     * Elimina todos los carritos asociados a un usuario.
     *
     * @param cedulaUsuario Cédula del usuario cuyos carritos se eliminarán.
     */
    @Override
    public void eliminarPorUsuario(String cedulaUsuario) {
        Iterator<Carrito> it = carritos.iterator();
        while (it.hasNext()) {
            Carrito c = it.next();
            if (c.getUsuario().getCedula().equals(cedulaUsuario)) {
                it.remove();
            }
        }
    }

}
