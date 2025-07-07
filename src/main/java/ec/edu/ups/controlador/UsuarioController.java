    package ec.edu.ups.controlador;

    import ec.edu.ups.dao.CarritoDAO;
    import ec.edu.ups.dao.PreguntaSeguridadDAO;
    import ec.edu.ups.dao.UsuarioDAO;
    import ec.edu.ups.dao.impl.PreguntaSeguridadDAOMemoria;
    import ec.edu.ups.modelo.PreguntaSeguridad;
    import ec.edu.ups.modelo.RespuestaDeSeguridad;
    import ec.edu.ups.modelo.Usuario;
    import ec.edu.ups.util.IdiomaUsado;
    import ec.edu.ups.util.MensajeInternacionalizacionHandler;
    import ec.edu.ups.vista.*;

    import javax.swing.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Date;
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
        private PreguntaSeguridadDAO preguntaDAO = new PreguntaSeguridadDAOMemoria();
        private List<RespuestaDeSeguridad> respuestasSeguridadTemporales = Collections.emptyList();

        public UsuarioController(UsuarioDAO usuarioDAO, CarritoDAO carritoDAO, LoginView loginView,
                                 RegistrarUsuarioView registrarUsuarioView) {
            this.usuarioDAO = usuarioDAO;
            this.carritoDAO = carritoDAO;
            this.loginView = loginView;
            this.registrarUsuarioView = registrarUsuarioView;
            this.usuario = null;

            this.mih = new MensajeInternacionalizacionHandler("es", "EC");

            //Cada vez que cambie el combo de idiomas en LoginView se actualiza el mih
            loginView.getComboBoxIdioma().addActionListener(e -> {
                IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
            });
            configurarEventosIniciales();
        }

        private void configurarEventosIniciales() {
            loginView.getBtnIniciarSesion().addActionListener(e -> autenticar());
            loginView.getBtnRegistrarse().addActionListener(e -> mostrarRegistrarse());
            loginView.getBtnRecuperarContra().addActionListener(e -> recuperarContrasenia());
            registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());
        }

        private void mostrarRegistrarse() {
            IdiomaUsado sel = (IdiomaUsado) loginView.getComboBoxIdioma().getSelectedItem();
            this.mih = new MensajeInternacionalizacionHandler(
                    sel.getLocale().getLanguage(), sel.getLocale().getCountry()
            );
            registrarUsuarioView.setMih(mih);
            registrarUsuarioView.actualizarTextos();
            registrarUsuarioView.setVisible(true);
        }

        private void abrirPreguntasDeSeguridadCompletas() {
            //muestran 10 preguntas del dao
            List<PreguntaSeguridad> todas = preguntaDAO.listarTodas();
            PreguntasSeguridadView dlg = new PreguntasSeguridadView(todas, mih, 3);
            dlg.setVisible(true);

            //se filtran las preguntas contestadas
            if (dlg.isSubmitted()) {
                List<RespuestaDeSeguridad> todasResps = dlg.getRespuestas();
                respuestasSeguridadTemporales = todasResps.stream().filter(r -> !r.getRespuesta().isBlank()).collect(Collectors.toList());
            } else {
                respuestasSeguridadTemporales = Collections.emptyList();
            }
        }

        private void registrarUsuario() {
            String username = registrarUsuarioView.getTxtUsuario().getText().trim();
            String passwd   = new String(registrarUsuarioView.getPasswordField1().getPassword()).trim();
            String nombre   = registrarUsuarioView.getTextField1().getText().trim();
            String apellido = registrarUsuarioView.getTextField2().getText().trim();
            String email    = registrarUsuarioView.getTextField3().getText().trim();
            String fechaStr = registrarUsuarioView.getTextField4().getText().trim();
            String telefono = registrarUsuarioView.getTextField5().getText().trim();

            if (username.isEmpty() || passwd.isEmpty() || nombre.isEmpty() ||
                    apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || fechaStr.isEmpty()) {
                registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensaje.campos"));
                return;
            }

            //se valida fecha
            Date fechaNacimiento;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                fechaNacimiento = sdf.parse(fechaStr);
            } catch (ParseException ex) {
                registrarUsuarioView.mostrarMensaje("mensaje.fecha");
                return;
            }

            //valida usuario unico
            if (usuarioDAO.buscarPorUsername(username) != null) {
                registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensaje.usuarioExiste"));
                return;
            }

            abrirPreguntasDeSeguridadCompletas();
            //se comprueba que se hayan llenado 3 preguntas
            if (respuestasSeguridadTemporales.size() != 3) {
                registrarUsuarioView.mostrarMensaje(mih.get("preguntaS.error.Responder"));
                return;
            }
            //se crea el usuario y se guardan las respuestas
            Usuario nuevo = new Usuario(username, passwd, USUARIO,nombre, apellido, fechaNacimiento, email, telefono);
            nuevo.setRespuestasSeguridad(respuestasSeguridadTemporales);
            usuarioDAO.crear(nuevo);

            registrarUsuarioView.mostrarMensaje(mih.get("registrar.mensajeExito"));
            registrarUsuarioView.limpiarCampos();
            respuestasSeguridadTemporales = Collections.emptyList();
        }

        private void recuperarContrasenia() {
            String username = JOptionPane.showInputDialog(
                    loginView,
                    mih.get("registrar.txtUsuario")
            );
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
                PreguntasSeguridadView dlg = new PreguntasSeguridadView(lista, mih, 1);
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
            int ok = JOptionPane.showConfirmDialog(loginView, pf,mih.get("recuperarC.actualizarC"), JOptionPane.OK_CANCEL_OPTION);
            if (ok != JOptionPane.OK_OPTION) return;

            String nueva = new String(pf.getPassword()).trim();
            if (nueva.isEmpty()) {
                loginView.mostrarMensaje(mih.get("recuperarC.contraVacia"));
                return;
            }

            u.setContrasenia(nueva);
            usuarioDAO.actualizar(u);
            loginView.mostrarMensaje(mih.get("recuperarC.mensajeExito"));
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

            cuentaAdminView = new CuendaAdminView();
            cuentaAdminView.setMensajeHandler(mih);
            principalView.getjDesktopPane().add(cuentaAdminView);

            // menú Cuenta de usuario
            principalView.getMenuItemCuentaUsuario().addActionListener(e -> abrirCuentaUsuario());
            // menu Listar Usuarios (solo admin)
            principalView.getMenuItemListarUsuarios().addActionListener(e -> abrirCuentaAdmin());

            configurarEventosCuenta();
            configurarEventosCuentaAdmin();

            principalView.getMenuItemSalir().addActionListener(e -> {
                System.exit(0);
            });
            //cierro principal y muestro login
            principalView.getMenuItemSalirALogin().addActionListener(e -> {
                principalView.dispose();
                loginView.setVisible(true);
            });
            principalView.setVisible(true);
        }

        public void abrirCuentaUsuario() {
            cuentaUsuarioView.getTextUsua().setText(usuario.getUsuario());
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
            cuentaUsuarioView.getCambiarButton().addActionListener(e -> cambiarContrasenia());
            cuentaUsuarioView.getBtnEliminarCuenta().addActionListener(e -> eliminarCuenta());
            cuentaUsuarioView.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
            cuentaUsuarioView.getBtnActualizar().addActionListener(e -> actualizarDatosUsuario());
        }

        private void cambiarContrasenia() {
            JPasswordField pf = new JPasswordField();

            int ok = JOptionPane.showConfirmDialog(cuentaUsuarioView,pf,mih.get("sesionUsuario.mensaje.contrasenia"),
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (ok == JOptionPane.OK_OPTION) {
                String nueva = new String(pf.getPassword()).trim();
                if (nueva.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            cuentaUsuarioView,
                            mih.get("sesionUsuario.mensajeError.contraseniaV"),
                            mih.get("sesionUsuario.titulo.mensaje"),
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                usuario.setContrasenia(nueva);
                usuarioDAO.actualizar(usuario);
                JOptionPane.showMessageDialog(cuentaUsuarioView,mih.get("sesionUsuario.mensajeExito.modif"));
            }
        }

        private void eliminarCuenta() {
            int confirm = JOptionPane.showConfirmDialog(cuentaUsuarioView,
                        mih.get("sesionUsuario.mensajeComprobacion"),
                        mih.get("sesionUsuario.mensajeComprobacion.titulo"),
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

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaNacimiento = sdf.parse(fechaStr);

                for (Usuario u : usuarioDAO.listarTodos()) {
                    if (!u.getUsuario().equals(usuario.getUsuario()) && u.getEmail().equalsIgnoreCase(correo)) {
                        cuentaUsuarioView.mostrarMensaje("El correo electrónico ya está en uso por otro usuario.");
                        return;
                    }
                }
                // Actualizar los datos
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setTelefono(telefono);
                usuario.setEmail(correo);
                usuario.setFechaNacimiento(fechaNacimiento);

                usuarioDAO.actualizar(usuario);
                cuentaUsuarioView.mostrarMensaje(mih.get("sesionU.txtExito"));

            } catch (ParseException ex) {
                cuentaUsuarioView.mostrarMensaje(mih.get("mensaje.fecha"));
            }
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
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensajeIngreso"));
                return;
            }
            Usuario u = usuarioDAO.buscarPorUsername(nombre);
            if (u == null) {
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensaje.usuarioNo"));
                cuentaAdminView.cargarUsuarios(List.of());
            } else {
                cuentaAdminView.cargarUsuarios(List.of(u.getUsuario()));
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

        private void modificarNombreUsuario() {
            int idx = cuentaAdminView.getTable1().getSelectedRow();
            if (idx < 0) {
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.seleccU.modif"));
                return;
            }
            String actual = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
            String prompt = mih.get("sesionA.nuevoNom") + ":";
            String nuevo = (String) JOptionPane.showInputDialog(cuentaAdminView,prompt,prompt,JOptionPane.PLAIN_MESSAGE,
                    null,null,actual);
            if (nuevo != null && !nuevo.trim().isEmpty()) {
                if (usuarioDAO.buscarPorUsername(nuevo) != null) {
                    cuentaAdminView.mostrarMensaje(mih.get("sesionA.nomUso"));
                    return;
                }
                Usuario u = usuarioDAO.buscarPorUsername(actual);
                u.setUsuario(nuevo);
                usuarioDAO.actualizar(u);
                abrirCuentaAdmin();
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.mensaje.nomModif"));
            }
        }

        private void modificarContraseniaUsuario() {
            int idx = cuentaAdminView.getTable1().getSelectedRow();
            if (idx < 0) {
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.seleccUsu.contra"));
                return;
            }
            String nom = (String) cuentaAdminView.getTable1().getValueAt(idx, 0);
            String title = mih.get("sesionA.mensajeNueva.contra") + ": " + nom;
            JPasswordField pf = new JPasswordField();
            int ok = JOptionPane.showConfirmDialog(cuentaAdminView,pf,title,JOptionPane.OK_CANCEL_OPTION);

            if (ok == JOptionPane.OK_OPTION) {
                String nueva = new String(pf.getPassword()).trim();
                if (nueva.isEmpty()) {
                    cuentaAdminView.mostrarMensaje(mih.get("sesionA.contraVacia"));
                    return;
                }
                Usuario u = usuarioDAO.buscarPorUsername(nom);
                u.setContrasenia(nueva);
                usuarioDAO.actualizar(u);
                cuentaAdminView.mostrarMensaje(mih.get("sesionA.contr.modif"));
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

        public MensajeInternacionalizacionHandler getMih() {
            return mih;
        }

        public void setMih(MensajeInternacionalizacionHandler mih) {
            this.mih = mih;
        }
    }
