package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.PreguntaSeguridadDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.PreguntaSeguridadDAOArchivoBinario;
import ec.edu.ups.dao.impl.PreguntaSeguridadDAOArchivoTexto;
import ec.edu.ups.dao.impl.PreguntaSeguridadDAOMemoria;
import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.modelo.RespuestaDeSeguridad;
import ec.edu.ups.modelo.TipoManejoMemoria;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.*;
import ec.edu.ups.vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static ec.edu.ups.modelo.Rol.USUARIO;
/**
 * Controlador que orquesta la lógica de autenticación, registro,
 * recuperación de contraseña y administración de cuentas de usuario
 * en la aplicación.
 * <p>
 * Interactúa con las vistas de login, registro y administración,
 * maneja la persistencia a través de {@link UsuarioDAO} y
 * coordina las preguntas de seguridad según el tipo de manejo de memoria.
 * </p>
 */
public class UsuarioController {

    private Usuario usuario;
    private final UsuarioDAO usuarioDAO;
    private final CarritoDAO carritoDAO;

    private final LoginView loginView;
    private final RegistrarUsuarioView registrarUsuarioView;
    private PrincipalView principalView;
    private CuentaUsuarioView cuentaUsuarioView;
    private CuendaAdminView cuentaAdminView;
    private TipoManejoMemoria tipoManejoMemoria;
    private String ruta;

    private MensajeInternacionalizacionHandler mih;

    private List<RespuestaDeSeguridad> respuestasSeguridadTemporales = Collections.emptyList();
    /**
     * Construye un controlador de usuario.
     *
     * @param usuarioDAO           DAO para persistencia de usuarios.
     * @param carritoDAO           DAO para persistencia de carritos.
     * @param loginView            Vista de login.
     * @param registrarUsuarioView Vista de registro de usuarios.
     * @param tipoMemoria          Tipo de manejo de persistencia.
     * @param ruta                 Ruta para almacenamiento en archivos.
     */
    public UsuarioController(UsuarioDAO usuarioDAO, CarritoDAO carritoDAO, LoginView loginView, RegistrarUsuarioView registrarUsuarioView, TipoManejoMemoria tipoMemoria, String ruta) {
        this.usuarioDAO = usuarioDAO;
        this.carritoDAO = carritoDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuario = null;
        this.tipoManejoMemoria = tipoMemoria;
        this.ruta = ruta;

        this.mih = new MensajeInternacionalizacionHandler("es", "EC");

        //Cada vez que cambie el combo de idiomas en LoginView se actualiza el mih
        loginView.getComboBoxIdioma().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
            }
        });
        configurarEventosIniciales();
    }

    private void configurarEventosIniciales() {
        //configura eventos del boton iniciar sesion
        loginView.getBtnIniciarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

        //BTN REGISTRARSE
        loginView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRegistrarse();
            }
        });

        //BTN RECUPERAR CONTRASEÑA
        loginView.getBtnRecuperarContra().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recuperarContrasenia();
            }
        });

        //BTN REGISTRARSE
        registrarUsuarioView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void mostrarRegistrarse() {
        IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
        this.mih = new MensajeInternacionalizacionHandler(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
        registrarUsuarioView.setMih(mih);
        registrarUsuarioView.actualizarTextos();
        registrarUsuarioView.setVisible(true);
        loginView.dispose();
    }

    private void abrirPreguntasDeSeguridadCompletas() {
        // Crear un DAO actualizado con el idioma actual
        PreguntaSeguridadDAO daoActual;
        switch (tipoManejoMemoria) {
            case MANEJO_ARCHIVO_BINARIO:
                daoActual = new PreguntaSeguridadDAOArchivoBinario(mih, ruta);
                break;
            case MANEJO_ARCHIVO_TEXTO:
                daoActual = new PreguntaSeguridadDAOArchivoTexto(mih, ruta);
                break;
            default:
                daoActual = new PreguntaSeguridadDAOMemoria(mih);
        }
        List<PreguntaSeguridad> todas = daoActual.listarTodas();

        // Mostrar la ventana con las preguntas traducidas
        PreguntasSeguridadView dlg = new PreguntasSeguridadView(todas, 3, mih);
        dlg.setVisible(true);

        if (dlg.isSubmitted()) {
            respuestasSeguridadTemporales = dlg.getRespuestas().stream().filter(r -> r.getRespuesta() != null && !r.getRespuesta().isBlank()).collect(Collectors.toList());
        } else {
            respuestasSeguridadTemporales = new ArrayList<>();
        }
    }

    private void registrarUsuario() {
        String cedula = registrarUsuarioView.getTxtUsuario().getText().trim();
        String contrasenia = new String(registrarUsuarioView.getPasswordField1().getPassword()).trim();
        String nombre = registrarUsuarioView.getTextField1().getText().trim();
        String apellido = registrarUsuarioView.getTextField2().getText().trim();
        String email = registrarUsuarioView.getTextField3().getText().trim();
        String fechaStr = registrarUsuarioView.getTextField4().getText().trim();
        String telefono = registrarUsuarioView.getTextField5().getText().trim();

        if (cedula.isEmpty() || contrasenia.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || fechaStr.isEmpty()) {
            registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensaje.campos"));
            return;
        }

        Date fechaNacimiento;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            fechaNacimiento = sdf.parse(fechaStr);
        } catch (ParseException ex) {
            registrarUsuarioView.mostrarMensaje(mih.get("mensaje.fecha"));
            return;
        }

        if (usuarioDAO.buscarPorUsername(cedula) != null) {
            registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensaje.usuarioExiste"));
            return;
        }

        abrirPreguntasDeSeguridadCompletas();
        if (respuestasSeguridadTemporales.size() != 3) {
            registrarUsuarioView.mostrarMensaje(mih.get("preguntaS.error.Responder"));
            return;
        }

        try {
            Usuario nuevo = new Usuario(cedula, contrasenia, USUARIO, nombre, apellido, fechaNacimiento, email, telefono);
            nuevo.setRespuestasSeguridad(respuestasSeguridadTemporales);
            usuarioDAO.crear(nuevo);

            registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensajeExito"));
            registrarUsuarioView.limpiarCampos();
            respuestasSeguridadTemporales = Collections.emptyList();
            registrarUsuarioView.dispose();
            loginView.setVisible(true);


        } catch (ExcepcionCedula e) {
            registrarUsuarioView.mostrarMensaje(e.getMessage());
        } catch (ExcepcionContrasenia e) {
            registrarUsuarioView.mostrarMensaje(e.getMessage());
        } catch (ExcepcionNomApe e) {
            registrarUsuarioView.mostrarMensaje(e.getMessage());
        } catch (ExcepcionCorreo e) {
            registrarUsuarioView.mostrarMensaje(e.getMessage());
        } catch (ExcepcionTelefono e) {
            registrarUsuarioView.mostrarMensaje(e.getMessage());
        }
    }

    private void recuperarContrasenia() {
        String username = JOptionPane.showInputDialog(loginView, mih.get("registrar.txtUsuario"));
        if (username == null || username.isBlank()) return;

        Usuario u = usuarioDAO.buscarPorUsername(username.trim());
        if (u == null) {
            loginView.mostrarMensaje(mih.get("recuperarC.error.usuaNo"));
            return;
        }

        List<RespuestaDeSeguridad> guardadas = u.getRespuestasSeguridad();
        if (guardadas == null || guardadas.size() < 3) {
            loginView.mostrarMensaje(mih.get("preguntaS.error.Responder"));
            return;
        }

        List<RespuestaDeSeguridad> copia = new ArrayList<>(guardadas);
        Collections.shuffle(copia); // desordenar

        boolean acceso = false;
        for (RespuestaDeSeguridad r : copia) {
            List<PreguntaSeguridad> lista = List.of(r.getPregunta());
            PreguntasSeguridadView dlg = new PreguntasSeguridadView(lista, 1, mih);
            dlg.setVisible(true);

            if (!dlg.isSubmitted()) continue;

            String respuestaUsuario = dlg.getRespuestas().get(0).getRespuesta().trim();
            if (respuestaUsuario.equalsIgnoreCase(r.getRespuesta().trim())) {
                acceso = true;
                break;
            }
        }

        if (!acceso) {
            loginView.mostrarMensaje(mih.get("recuperarC.error.respuestasIncorr"));
            return;
        }

        // permitir cambiar contraseña
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(loginView, pf, mih.get("recuperarC.actualizarC"), JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        String nueva = new String(pf.getPassword()).trim();
        try {
            u.setContrasenia(nueva);
            usuarioDAO.actualizar(u);
            loginView.mostrarMensaje(mih.get("recuperarC.mensajeExito"));
        } catch (ExcepcionContrasenia e) {
            // Se muestra el mensaje específico que lanzaste desde setContrasenia
            JOptionPane.showMessageDialog(loginView, e.getMessage(), mih.get("recuperarC.error.titulo"), // ejemplo: "Error al cambiar contraseña"
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void autenticar() {
        String username = loginView.getTxtUsuario().getText().trim();
        String contrasenia = new String(loginView.getTxtContrasenia().getPassword()).trim();

        usuario = usuarioDAO.autenticar(username, contrasenia);
        if (usuario == null) {
            loginView.mostrarMensaje(mih.get("registrar.mensajeError"));
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
        cuentaUsuarioView.setMensajeHandler(mih);
        principalView.getjDesktopPane().add(cuentaUsuarioView);
        cuentaUsuarioView.getTextContra().setEditable(false);

        cuentaAdminView = new CuendaAdminView();
        cuentaAdminView.setMensajeHandler(mih);
        principalView.getjDesktopPane().add(cuentaAdminView);

        // menú Cuenta de usuario
        principalView.getMenuItemCuentaUsuario().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCuentaUsuario();
            }
        });
        // menu Listar Usuarios (solo admin)
        principalView.getMenuItemListarUsuarios().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCuentaAdmin();
            }
        });

        configurarEventosCuenta();
        configurarEventosCuentaAdmin();

        principalView.getMenuItemSalir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //cierro principal y muestro login
        principalView.getMenuItemSalirALogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                principalView.dispose();
                loginView.setVisible(true);
            }

        });
        principalView.setVisible(true);
    }

    public void abrirCuentaUsuario() {
        cuentaUsuarioView.getTextUsua().setText(usuario.getCedula());
        cuentaUsuarioView.getTextContra().setText("********");
        cuentaUsuarioView.getTextNombre().setText(usuario.getNombre());
        cuentaUsuarioView.getTextApe().setText(usuario.getApellido());
        cuentaUsuarioView.getTextTelefo().setText(usuario.getTelefono());
        cuentaUsuarioView.getTextEmail().setText(usuario.getEmail());

        Date fecha = usuario.getFechaNacimiento();
        if (fecha != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cuentaUsuarioView.getTextFechaN().setText(sdf.format(fecha));
        } else {
            cuentaUsuarioView.getTextFechaN().setText("");
        }

        cuentaUsuarioView.setVisible(true);
        cuentaUsuarioView.toFront();
    }

    private void configurarEventosCuenta() {
        cuentaUsuarioView.getCambiarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarContrasenia();
            }
        });

        cuentaUsuarioView.getBtnEliminarCuenta().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCuenta();
            }
        });
        cuentaUsuarioView.getBtnCerrarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
        cuentaUsuarioView.getBtnActualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarDatosUsuario();
            }
        });
    }

    private void cambiarContrasenia() {
        JPasswordField pf = new JPasswordField();

        int ok = JOptionPane.showConfirmDialog(cuentaUsuarioView, pf, mih.get("sesionUsuario.mensaje.contrasenia"), JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            String nueva = new String(pf.getPassword()).trim();

            try {
                usuario.setContrasenia(nueva);
                usuarioDAO.actualizar(usuario);
                JOptionPane.showMessageDialog(cuentaUsuarioView, mih.get("sesionUsuario.mensajeExito.modif"));
            } catch (ExcepcionContrasenia e) {
                JOptionPane.showMessageDialog(cuentaUsuarioView, e.getMessage(), //esto obtiene el mensaje especifico de excepcion
                        mih.get("sesionUsuario.titulo.mensaje"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarCuenta() {
        int confirm = JOptionPane.showConfirmDialog(cuentaUsuarioView, mih.get("sesionUsuario.mensajeComprobacion"), mih.get("sesionUsuario.mensajeComprobacion.titulo"), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            carritoDAO.eliminarPorUsuario(usuario.getCedula());
            usuarioDAO.eliminar(usuario.getCedula());
            principalView.dispose();
            loginView.setVisible(true);
        }
    }

    private void cerrarSesion() {
        cuentaUsuarioView.dispose();
        principalView.dispose();
        loginView.setVisible(true);
    }

    private void actualizarDatosUsuario() {
        String nombre = cuentaUsuarioView.getTextNombre().getText().trim();
        String apellido = cuentaUsuarioView.getTextApe().getText().trim();
        String telefono = cuentaUsuarioView.getTextTelefo().getText().trim();
        String correo = cuentaUsuarioView.getTextEmail().getText().trim();
        String fechaStr = cuentaUsuarioView.getTextFechaN().getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || fechaStr.isEmpty()) {
            cuentaUsuarioView.mostrarMensaje(mih.get("registrar.mensaje.campos"));
            return;
        }
        // Primero validamos y parseamos la fecha
        Date fechaNacimiento;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            fechaNacimiento = sdf.parse(fechaStr);
        } catch (ParseException ex) {
            cuentaUsuarioView.mostrarMensaje(mih.get("mensaje.fecha"));
            return;
        }

        // Verificar unicidad de correo entre otros usuarios
        for (Usuario u : usuarioDAO.listarTodos()) {
            if (!u.getCedula().equals(usuario.getCedula()) && u.getEmail().equalsIgnoreCase(correo)) {
                cuentaUsuarioView.mostrarMensaje("El correo electrónico ya está en uso por otro usuario.");
                return;
            }
        }

        try {
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setEmail(correo);
            usuario.setFechaNacimiento(fechaNacimiento);

            usuarioDAO.actualizar(usuario);
            cuentaUsuarioView.mostrarMensaje(mih.get("sesionU.txtExito"));

        } catch (ExcepcionNomApe | ExcepcionTelefono | ExcepcionCorreo e1) {
            cuentaUsuarioView.mostrarMensaje(e1.getMessage());
        }
    }

    private void abrirCuentaAdmin() {
        // carga todos los usuarios inicialmente
        List<Usuario> todos = usuarioDAO.listarTodos();
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
        cuentaAdminView.getBtnModificarContra().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarContraseniaUsuario();
            }
        });
    }

    private void listarUsuarios() {
        List<Usuario> lista = usuarioDAO.listarTodos();
        cuentaAdminView.cargarUsuarios(lista);
    }

    private void buscarUsuario() {
        String nombre = cuentaAdminView.getTextField1().getText().trim();
        if (nombre.isEmpty()) {
            cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensajeIngreso"));
            return;
        }
        Usuario u = usuarioDAO.buscarPorUsername(nombre);
        if (u == null) {
            cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensaje.usuarioNo"));
            cuentaAdminView.cargarUsuarios(List.of());
        } else {
            List<Usuario> todos = new ArrayList<>();
            todos.add(u);
            cuentaAdminView.cargarUsuarios(todos);
        }
    }

    private void eliminarUsuario() {
        int idx = cuentaAdminView.getTable1().getSelectedRow();
        if (idx < 0) {
            cuentaAdminView.mostrarMensaje(mih.get("sesionA.seleccioneU"));
            return;
        }
        String nom = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
        usuarioDAO.eliminar(nom);
        abrirCuentaAdmin();
        cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensajeConf.usuaEli"));
    }

    //modificar contrasenia del usuario (ADMINISTRADOR)
    private void modificarContraseniaUsuario() {
        //obtiene fila seleccionada
        int fila = cuentaAdminView.getTable1().getSelectedRow();
        if (fila < 0) {
            cuentaAdminView.mostrarMensaje(mih.get("sesionA.seleccUsu.contra"));
            return;
        }
        String nom = (String) cuentaAdminView.getTable1().getValueAt(fila, 0);
        String title = mih.get("sesionA.mensajeNueva.contra") + ": " + nom;
        JPasswordField pf = new JPasswordField();
        int ok = JOptionPane.showConfirmDialog(cuentaAdminView, pf, title, JOptionPane.OK_CANCEL_OPTION);

        if (ok == JOptionPane.OK_OPTION) {
            String nueva = new String(pf.getPassword()).trim();
            if (nueva.isEmpty()) {
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.contraVacia"));
                return;
            }
            Usuario u = usuarioDAO.buscarPorUsername(nom);

            try {
                u.setContrasenia(nueva); // aquí puede lanzar la excepción personalizada
                usuarioDAO.actualizar(u);
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.contr.modif"));
            } catch (ExcepcionContrasenia e) {
                cuentaAdminView.mostrarMensaje("CONTRASE;A INCORRECTA ");
            }
        }
    }

    public Usuario getUsuarioAutenticado() {
        return usuario;
    }

    //Permite acceder a la ventana principal creada internamente
    public PrincipalView getPrincipalView() {
        return principalView;
    }

    public MensajeInternacionalizacionHandler getMih() {
        return mih;
    }

    public void setMih(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
    }
}
