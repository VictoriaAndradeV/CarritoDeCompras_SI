package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
/**
 * Implementación en memoria de {@link UsuarioDAO}.
 * Guarda usuarios en una lista y ofrece operaciones básicas
 * de autenticación, creación, búsqueda, actualización y eliminación.
 *
 * En el constructor se cargan dos usuarios de ejemplo (Admin y Usuario)
 * usando fechas y manejadores de excepción para validar sus datos.
 */
public class UsuarioDAOMemoria implements UsuarioDAO {
    /** Lista interna que almacena los usuarios. */
    private final List<Usuario> usuarios;
    /**
     * Inicializa el DAO, crea la lista y carga dos usuarios de prueba:
     * uno con rol ADMINISTRADOR y otro con rol USUARIO.
     * Captura y muestra errores de formato o validación si ocurren.
     */
    public UsuarioDAOMemoria() {
        usuarios = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaAdmin = sdf.parse("2004-10-13");
            Date fechaUser = sdf.parse("2004-09-25");

            crear(new Usuario("0107242869", "Arbol*123", Rol.ADMINISTRADOR, "Victoria", "Andrade", fechaAdmin, "victoriaavchico@gmail.com", "0962301221"));
            crear(new Usuario("0102344850", "Arbol*123", Rol.USUARIO, "Dalyana", "matute", fechaUser, "dalyana@gmail.com", "0962301223"));
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        } catch (ExcepcionContrasenia e) {
            System.err.println(e.getMessage());
        } catch (ExcepcionCedula e2) {
            System.err.println(e2.getMessage());
        }catch (ExcepcionNomApe e3){
            System.err.println(e3.getMessage());
        }catch (ExcepcionCorreo e4) {
            System.err.println("Error de correo: " + e4.getMessage());
        }catch (ExcepcionTelefono e){
            System.err.println("Error de telefono: " + e.getMessage());
        }
    }
    /**
     * Busca un usuario por cédula y contraseña.
     *
     * @param cedula     cédula del usuario
     * @param contrasenia contraseña a verificar
     * @return usuario si coincide cédula y contraseña, o null si no
     */
    @Override
    public Usuario autenticar(String cedula, String contrasenia) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(cedula) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }
    /**
     * Agrega un usuario a la lista.
     *
     * @param usuario instancia de usuario a guardar
     */
    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
    }
    /**
     * Busca un usuario por cédula.
     *
     * @param cedula cédula del usuario a buscar
     * @return usuario encontrado, o null si no existe
     */
    @Override
    public Usuario buscarPorUsername(String cedula) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(cedula)) {
                return usuario;
            }
        }
        return null;
    }
    /**
     * Elimina un usuario de la lista comparando la cédula.
     *
     * @param cedula cédula del usuario a borrar
     */
    @Override
    public void eliminar(String cedula) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getCedula().equals(cedula)) {
                iterator.remove();
                break;
            }
        }
    }
    /**
     * Actualiza un usuario existente reemplazando la instancia en la lista.
     *
     * @param usuario usuario con datos actualizados
     */
    @Override
    public void actualizar(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuarioAux = usuarios.get(i);
            if (usuarioAux.getCedula().equals(usuario.getCedula())) {
                usuarios.set(i, usuario);
                break;
            }
        }
    }
    /**
     * Devuelve todos los usuarios almacenados.
     *
     * @return lista de usuarios
     */
    @Override
    public List<Usuario> listarTodos() {
        return usuarios;
    }

    /*@Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equals(rol)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }*/
}
