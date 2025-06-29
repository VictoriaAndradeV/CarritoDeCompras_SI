package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.CuentaUsuarioView;
import ec.edu.ups.vista.LoginView;
import ec.edu.ups.vista.PrincipalView;
import ec.edu.ups.vista.RegistrarUsuarioView;

import javax.swing.*;

import static ec.edu.ups.modelo.Rol.USUARIO;

public class UsuarioController {

    private Usuario usuario;
    private final UsuarioDAO usuarioDAO;
    private final CarritoDAO carritoDAO;

    private final LoginView loginView;
    private RegistrarUsuarioView registrarUsuarioView;
    private PrincipalView principalView;
    private CuentaUsuarioView cuentaUsuarioView;

    public UsuarioController(UsuarioDAO usuarioDAO, CarritoDAO carritoDAO, LoginView loginView,
                             RegistrarUsuarioView registrarUsuarioView) {
        this.usuarioDAO = usuarioDAO;
        this.carritoDAO = carritoDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuario = null;
        configurarEventosIniciales();
    }

    private void configurarEventosIniciales() {
        loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());
        loginView.getBtnRegistrarse().addActionListener(e -> mostrarRegistrarse());
        registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());
    }

    private void mostrarRegistrarse() {
        if (!registrarUsuarioView.isVisible()) {
            registrarUsuarioView.setVisible(true);
        }
    }

    private void registrarUsuario() {
        String username = registrarUsuarioView.getTxtUsuario().getText().trim();
        String contrasenia = new String(registrarUsuarioView.getPasswordField1().getPassword()).trim();

        if (username.isEmpty() || contrasenia.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("Todos los campos son obligatorios.");
            return;
        }
        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("El nombre de usuario ya existe.");
            return;
        }
        Usuario nuevo = new Usuario(username, contrasenia, USUARIO);
        usuarioDAO.crear(nuevo);
        registrarUsuarioView.mostrarMensaje("Usuario registrado exitosamente.");
        registrarUsuarioView.limpiarCampos();
    }

    private void autenticar() {
        String username = loginView.getTxtUsuario().getText().trim();
        String contrasenia = new String(loginView.getTxtContrasenia().getPassword()).trim();

        usuario = usuarioDAO.autenticar(username, contrasenia);
        if (usuario == null) {
            loginView.mostrarMensaje("Usuario o contraseña incorrectos.");
        } else {
            loginView.dispose();
            iniciarPrincipal();
        }
    }

    private void iniciarPrincipal() {
        principalView = new PrincipalView();
        cuentaUsuarioView = new CuentaUsuarioView();
        principalView.getjDesktopPane().add(cuentaUsuarioView);

        principalView.getMenuItemCuentaUsuario().addActionListener(e -> abrirCuentaUsuario());
    }

    private void abrirCuentaUsuario() {
        cuentaUsuarioView.getTxtNombreUsuario().setText(usuario.getUsuario());
        cuentaUsuarioView.getTextField1().setText("********");
        cuentaUsuarioView.setVisible(true);
        configurarEventosCuenta();
    }

    private void configurarEventosCuenta() {
        cuentaUsuarioView.getEditarNombreButton().addActionListener(e -> editarNombre());
        cuentaUsuarioView.getCambiarButton().addActionListener(e -> cambiarContrasenia());
        cuentaUsuarioView.getBtnEliminarCuenta().addActionListener(e -> eliminarCuenta());
        cuentaUsuarioView.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void editarNombre() {
        String nuevo = JOptionPane.showInputDialog(
                cuentaUsuarioView,
                "Ingrese nuevo nombre de usuario:",
                usuario.getUsuario()
        );
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            if (usuarioDAO.buscarPorUsername(nuevo) != null) {
                JOptionPane.showMessageDialog(
                        cuentaUsuarioView,
                        "El nombre ya está en uso.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            usuario.setUsuario(nuevo);
            usuarioDAO.actualizar(usuario);
            cuentaUsuarioView.getTxtNombreUsuario().setText(nuevo);
            JOptionPane.showMessageDialog(
                    cuentaUsuarioView,
                    "Nombre actualizado exitosamente."
            );
        }
    }

    private void cambiarContrasenia() {
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(
                cuentaUsuarioView,
                pf,
                "Ingrese nueva contraseña:",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (ok == JOptionPane.OK_OPTION) {
            String nueva = new String(pf.getPassword()).trim();
            if (nueva.isEmpty()) {
                JOptionPane.showMessageDialog(
                        cuentaUsuarioView,
                        "La contraseña no puede quedar vacía.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            usuario.setContrasenia(nueva);
            usuarioDAO.actualizar(usuario);
            JOptionPane.showMessageDialog(
                    cuentaUsuarioView,
                    "Contraseña cambiada exitosamente."
            );
        }
    }

    private void eliminarCuenta() {
        int confirm = JOptionPane.showConfirmDialog(
                cuentaUsuarioView,
                "Se eliminarán todos sus carritos. ¿Está seguro de eliminar su cuenta? ",
                "Confirmar eliminar cuenta",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            carritoDAO.eliminarPorUsuario(usuario.getUsuario());
            usuarioDAO.eliminar(usuario.getUsuario());
            principalView.dispose();
            loginView.setVisible(true);
        }
    }

    private void cerrarSesion() {
        cuentaUsuarioView.dispose();
        principalView.dispose();
        loginView.setVisible(true);
    }

    public Usuario getUsuarioAutenticado() {
        return usuario;
    }

}
