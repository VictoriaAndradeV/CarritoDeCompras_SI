package ec.edu.ups.controlador;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.LoginView;
import ec.edu.ups.vista.RegistrarUsuarioView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioController {

    private Usuario usuario;
    private final UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private RegistrarUsuarioView registrarUsuarioView;

    public UsuarioController(UsuarioDAO usuarioDAO, LoginView loginView) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.usuario = null;
        configurarEventosEnVistas();
    }

    private void configurarEventosEnVistas(){
        loginView.getBtnIniciarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

        loginView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!registrarUsuarioView.isVisible()) {
                    registrarUsuarioView.setVisible(true);
                }
            }
        });
    }

    public void setRegistrarUsuarioView(RegistrarUsuarioView registrarUsuarioView) {
        this.registrarUsuarioView = registrarUsuarioView;
        configurarEventosRegistrarUsuario();
    }

    private void configurarEventosRegistrarUsuario() {
        registrarUsuarioView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String username = registrarUsuarioView.getTxtUsuario().getText().trim();
        String contrasenia = registrarUsuarioView.getPasswordField1().getText().trim();
        Rol rol = (Rol) registrarUsuarioView.getComboBoxRol().getSelectedItem();


        if (username.isEmpty() || contrasenia.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("Todos los campos son obligatorios.");
            return;
        }

        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("El nombre de usuario ya existe.");
            return;
        }

        Usuario nuevo = new Usuario(username, contrasenia, rol);
        usuarioDAO.crear(nuevo);
        registrarUsuarioView.mostrarMensaje("Usuario registrado exitosamente.");
        registrarUsuarioView.limpiarCampos();
    }



    private void autenticar(){
        String username = loginView.getTxtUsuario().getText();
        String contrasenia = loginView.getTxtContrasenia().getText();

        usuario = usuarioDAO.autenticar(username, contrasenia);
        if(usuario == null){
            loginView.mostrarMensaje("Usuario o contraseña incorrectos.");
        }else{
            loginView.dispose();
        }
    }

    public Usuario getUsuarioAutenticado(){
        return usuario;
    }


}
