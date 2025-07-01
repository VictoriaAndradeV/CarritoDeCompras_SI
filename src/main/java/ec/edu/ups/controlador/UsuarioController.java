package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.IdiomaUsado;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import static ec.edu.ups.modelo.Rol.USUARIO;

public class UsuarioController {

    private Usuario usuario;
    private final UsuarioDAO usuarioDAO;
    private final CarritoDAO carritoDAO;

    private final LoginView loginView;
    private final RegistrarUsuarioView registrarUsuarioView;
    private PrincipalView principalView;
    private CuentaUsuarioView cuentaUsuarioView;
    private CuendaAdminView cuentaAdminView;

    private MensajeInternacionalizacionHandler mih;

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
        // se lee el idioma tomado del combobox
        IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
        String lenguaje = sel.getLocale().getLanguage();
        String pais = sel.getLocale().getCountry();

        MensajeInternacionalizacionHandler handler = new MensajeInternacionalizacionHandler(lenguaje, pais);

        registrarUsuarioView.setMih(handler);
        registrarUsuarioView.actualizarTextos();

        if (!registrarUsuarioView.isVisible()) { //se muestra la ventana
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
            // se lee el idioma que eligió el usuario
            IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
            String lang = sel.getLocale().getLanguage();
            String country = sel.getLocale().getCountry();

            //Creamos el handler UNA SOLA VEZ para toda la sesion
            this.mih = new MensajeInternacionalizacionHandler(lang, country);

            //Cerramos el login y arrancamos la principal
            loginView.dispose();
            iniciarPrincipal();
        }
    }

    private void iniciarPrincipal() {
        principalView = new PrincipalView(mih);
        cuentaUsuarioView = new CuentaUsuarioView();
        cuentaAdminView = new CuendaAdminView();

        principalView.getjDesktopPane().add(cuentaUsuarioView);
        principalView.getjDesktopPane().add(cuentaAdminView);

        // menú Cuenta de usuario
        principalView.getMenuItemCuentaUsuario().addActionListener(e -> abrirCuentaUsuario());
        // menú Listar Usuarios (solo admin)
        principalView.getMenuItemListarUsuarios().addActionListener(e -> abrirCuentaAdmin());

        //principalView.setVisible(true);
        configurarEventosCuenta();
        configurarEventosCuentaAdmin();
        principalView.setVisible(true);
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
        cuentaUsuarioView.mostrarMensaje("Ingrese el nuevo nombre de usuario:");
        String nuevo = usuario.getUsuario();
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            if (usuarioDAO.buscarPorUsername(nuevo) != null) {
                cuentaUsuarioView.mostrarMensaje("El nombre de usuario ya existe.");
                return;
            }
            usuario.setUsuario(nuevo);
            usuarioDAO.actualizar(usuario);
            cuentaUsuarioView.getTxtNombreUsuario().setText(nuevo);
            cuentaUsuarioView.mostrarMensaje("Usuario actualizado exitosamente.");
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
                        "La contraseña no puede estar vacia",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            usuario.setContrasenia(nueva);
            usuarioDAO.actualizar(usuario);
            JOptionPane.showMessageDialog(
                    cuentaUsuarioView,
                    "Contraseña modificada"
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

    private void abrirCuentaAdmin() {
        // carga todos los usuarios inicialmente
        List<String> todos = usuarioDAO.listarTodos()
                .stream().map(Usuario::getUsuario)
                .collect(Collectors.toList());
        cuentaAdminView.cargarUsuarios(todos);
        cuentaAdminView.setVisible(true);
    }

    private void configurarEventosCuentaAdmin() {
        cuentaAdminView.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarUsuarios();
            }
        });

        cuentaAdminView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuario();
            }
        });
        cuentaAdminView.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarUsuario();
            }
        });

        cuentaAdminView.getBtnModificarNom().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarNombreUsuario();
            }
        });

        cuentaAdminView.getBtnModificarContra().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarContraseniaUsuario();
            }
        });

        cuentaAdminView.getBtnCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesionAdmin();
            }
        });
    }

    private void listarUsuarios() {
        List<String> lista = usuarioDAO.listarTodos()
                .stream().map(Usuario::getUsuario)
                .collect(Collectors.toList());
        cuentaAdminView.cargarUsuarios(lista);
    }

    private void buscarUsuario() {
        String nombre = cuentaAdminView.getTextField1().getText().trim();
        if (nombre.isEmpty()) {
            cuentaAdminView.mostrarMensaje("Ingrese nombre de usuario a buscar");
            return;
        }
        Usuario u = usuarioDAO.buscarPorUsername(nombre);
        if (u == null) {
            cuentaAdminView.mostrarMensaje("Usuario no encontrado");
            cuentaAdminView.cargarUsuarios(List.of());
        } else {
            cuentaAdminView.cargarUsuarios(List.of(u.getUsuario()));
        }
    }

    private void eliminarUsuario() {
        int idx = cuentaAdminView.getTable1().getSelectedRow();
        if (idx < 0) {
            cuentaAdminView.mostrarMensaje("Seleccione un usuario para eliminar");
            return;
        }
        String nom = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
        usuarioDAO.eliminar(nom);
        abrirCuentaAdmin();
        cuentaAdminView.mostrarMensaje("Usuario " + nom + " eliminado");
    }

    private void modificarNombreUsuario() {
        int idx = cuentaAdminView.getTable1().getSelectedRow();
        if (idx < 0) {
            cuentaAdminView.mostrarMensaje("Seleccione un usuario para modificar nombre");
            return;
        }
        String actual = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
        String nuevo = JOptionPane.showInputDialog(
                cuentaAdminView,
                "Nuevo nombre para " + actual + ":", actual
        );
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            if (usuarioDAO.buscarPorUsername(nuevo) != null) {
                cuentaAdminView.mostrarMensaje("Nombre ya en uso");
                return;
            }
            Usuario u = usuarioDAO.buscarPorUsername(actual);
            u.setUsuario(nuevo);
            usuarioDAO.actualizar(u);
            abrirCuentaAdmin();
            cuentaAdminView.mostrarMensaje("Nombre modificado");
        }
    }

    private void modificarContraseniaUsuario() {
        int idx = cuentaAdminView.getTable1().getSelectedRow();
        if (idx < 0) {
            cuentaAdminView.mostrarMensaje("Seleccione un usuario para modificar contraseña");
            return;
        }
        String nom = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(
                cuentaAdminView,
                pf,
                "Nueva contraseña para " + nom + ":",
                JOptionPane.OK_CANCEL_OPTION
        );
        if (ok == JOptionPane.OK_OPTION) {
            String nueva = new String(pf.getPassword()).trim();
            if (nueva.isEmpty()) {
                cuentaAdminView.mostrarMensaje("Contraseña no puede quedar vacía");
                return;
            }
            Usuario u = usuarioDAO.buscarPorUsername(nom);
            u.setContrasenia(nueva);
            usuarioDAO.actualizar(u);
            cuentaAdminView.mostrarMensaje("Contraseña modificada");
        }
    }

    private void cerrarSesionAdmin() {
        cuentaAdminView.dispose();
        principalView.dispose();
        loginView.setVisible(true);
    }

    public Usuario getUsuarioAutenticado() {
        return usuario;
    }

    //Permite acceder a la ventana principal creada internamente
    public PrincipalView getPrincipalView() {
        return principalView;
    }

}
