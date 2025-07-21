package ec.edu.ups.dao;

import ec.edu.ups.modelo.Usuario;

import java.util.List;
/**
 * Interfaz que define las operaciones básicas para gestionar usuarios dentro del sistema.
 * Se declaran los métodos para crear, buscar, actualizar, eliminar y listar usuarios,
 * así como autenticar su acceso.
 *
 * <p>Implementaciones concretas de esta interfaz pueden usar memoria, archivos o bases de datos
 * para guardar la información.</p>
 */
public interface UsuarioDAO {
    /**
     * Verifica las credenciales de un usuario.
     *
     * @param username Nombre de usuario ingresado (cédula)
     * @param contrasenia Contraseña ingresada
     * @return el usuario autenticado si las credenciales son correctas, o null si no
     */
    Usuario autenticar(String username, String contrasenia);
    /**
     * Guarda un nuevo usuario en el sistema.
     * @param usuario Objeto Usuario a crear
     */
    void crear(Usuario usuario);

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar
     * @return el usuario encontrado, o null si no existe
     */
    Usuario buscarPorUsername(String username);

    /**
     * Elimina un usuario existente según su nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a eliminar
     */
    void eliminar(String username);
    /**
     * Actualiza los datos de un usuario.
     *
     * @param usuario Usuario con los datos modificados
     */
    void actualizar(Usuario usuario);

    /**
     * Lista todos los usuarios registrados.
     *
     * @return lista con todos los usuarios
     */
    List<Usuario> listarTodos();
}
