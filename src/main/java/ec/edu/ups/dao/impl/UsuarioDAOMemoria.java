package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOMemoria implements UsuarioDAO {
    private final List<Usuario> usuarios;

    public UsuarioDAOMemoria() {
        usuarios = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaAdmin = sdf.parse("2004-10-13");
            Date fechaUser = sdf.parse("2004-09-25");

            crear(new Usuario("admin", "1234", Rol.ADMINISTRADOR, "Victoria", "Andrade", fechaAdmin, "victoriaavchico@gmail.com", "0962301221"));
            crear(new Usuario("user", "1234", Rol.USUARIO, "Dalyana", "matute", fechaUser, "dalyanavchico@gmail.com", "0962301223"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario usuario : usuarios) {
            if(usuario.getUsuario().equals(username) && usuario.getContrasenia().equals(contrasenia)){
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String username) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsuario().equals(username)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        for(int i = 0; i < usuarios.size(); i++){
            Usuario usuarioAux = usuarios.get(i);
            if(usuarioAux.getUsuario().equals(usuario.getUsuario())){
                usuarios.set(i, usuario);
                break;
            }
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios;
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equals(rol)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}
