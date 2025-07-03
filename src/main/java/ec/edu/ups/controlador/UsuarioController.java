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
            List<PreguntaSeguridad> todas = preguntaDAO.listarTodas(); // 10 preguntas
            PreguntasSeguridadView dlg = new PreguntasSeguridadView(todas, mih);
            dlg.setVisible(true);
            if (dlg.isSubmitted()) {
                respuestasSeguridadTemporales = dlg.getRespuestas();
            } else {
                respuestasSeguridadTemporales = Collections.emptyList();
            }
        }

        private void registrarUsuario() {
            // 1) validar campos del formulario
            String username = registrarUsuarioView.getTxtUsuario().getText().trim();
            String passwd   = new String(registrarUsuarioView.getPasswordField1().getPassword()).trim();
            String nombre   = registrarUsuarioView.getTextField1().getText().trim();
            String apellido = registrarUsuarioView.getTextField2().getText().trim();
            String email    = registrarUsuarioView.getTextField3().getText().trim();
            String fechaStr = registrarUsuarioView.getTextField4().getText().trim();
            String telefono = registrarUsuarioView.getTextField5().getText().trim();

            if (username.isEmpty() || passwd.isEmpty() || nombre.isEmpty() ||
                    apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || fechaStr.isEmpty()) {
                registrarUsuarioView.mostrarMensaje("Todos los campos son obligatorios.");
                return;
            }

            // 2) validar fecha
            Date fechaNacimiento;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                fechaNacimiento = sdf.parse(fechaStr);
            } catch (ParseException ex) {
                registrarUsuarioView.mostrarMensaje("La fecha debe tener el formato yyyy-MM-dd.");
                return;
            }

            // 3) verificar usuario único
            if (usuarioDAO.buscarPorUsername(username) != null) {
                registrarUsuarioView.mostrarMensaje("El nombre de usuario ya existe.");
                return;
            }

            // 4) ahora sí abrimos las 10 preguntas
            abrirPreguntasDeSeguridadCompletas();
            int totalPreg = preguntaDAO.listarTodas().size(); // 10
            if (respuestasSeguridadTemporales.size() != totalPreg) {
                registrarUsuarioView.mostrarMensaje(
                        "Debes contestar las " + totalPreg + " preguntas de seguridad."
                );
                return;
            }

            // 5) crear el usuario y asociar respuestas
            Usuario nuevo = new Usuario(username, passwd, USUARIO,
                    nombre, apellido, fechaNacimiento, email, telefono);
            nuevo.setRespuestasSeguridad(respuestasSeguridadTemporales);
            usuarioDAO.crear(nuevo);

            registrarUsuarioView.mostrarMensaje("Usuario registrado exitosamente.");
            registrarUsuarioView.limpiarCampos();
            respuestasSeguridadTemporales = Collections.emptyList();
        }


        private void recuperarContrasenia() {
            //pedir usuario
            String username = JOptionPane.showInputDialog(
                    loginView,
                    mih.get("registrar.txtUsuario")
            );
            if (username == null || username.isBlank()) {
                return; //se cancela
            }

            //buscamos el usuario
            Usuario u = usuarioDAO.buscarPorUsername(username.trim());
            if (u == null) {
                loginView.mostrarMensaje(mih.get("recuperarC.error.usuaNo"));
                return;
            }

            //tomamos las respuestas de seguridad
            List<RespuestaDeSeguridad> guardadas = u.getRespuestasSeguridad();
            if (guardadas == null || guardadas.size() < 3) {
                loginView.mostrarMensaje(mih.get("recuperarC.error.respuestasIncom"));
                return;
            }

            //tomamos tres preguntas al azar
            Collections.shuffle(guardadas);
            List<PreguntaSeguridad> tresPreguntas = guardadas.stream()
                    .limit(3)
                    .map(RespuestaDeSeguridad::getPregunta)
                    .collect(Collectors.toList());

            PreguntasSeguridadView dlg = new PreguntasSeguridadView(tresPreguntas, mih);
            dlg.setVisible(true);
            if (!dlg.isSubmitted()) {
                return;
            }

            List<RespuestaDeSeguridad> dadas = dlg.getRespuestas();
            boolean todasBien = dadas.stream().allMatch(r ->
                    guardadas.stream().anyMatch(g ->
                            g.getPregunta().equals(r.getPregunta()) &&
                                    g.getRespuesta().equalsIgnoreCase(r.getRespuesta())
                    )
            );
            if (!todasBien) {
                loginView.mostrarMensaje(mih.get("recuperarC.error.respuestasIncorr"));
                return;
            }

            //validamos contraseña
            JPasswordField pf = new JPasswordField();
            int ok = JOptionPane.showConfirmDialog(
                    loginView, pf,
                    mih.get("recuperarC.actualizarC"),
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (ok != JOptionPane.OK_OPTION) return;
            String nueva = new String(pf.getPassword()).trim();
            if (nueva.isEmpty()) {
                loginView.mostrarMensaje(mih.get("recuperarC.contraVacia"));
                return;
            }

            //actualizamos la nueva contraseña
            u.setContrasenia(nueva);
            usuarioDAO.actualizar(u);
            loginView.mostrarMensaje(mih.get("recuperarC.mensajeExito"));
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
            cuentaUsuarioView.setMensajeHandler(mih);
            principalView.getjDesktopPane().add(cuentaUsuarioView);

            cuentaAdminView = new CuendaAdminView();
            cuentaAdminView.setMensajeHandler(mih);
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
            String nuevo = cuentaUsuarioView.getTxtNombreUsuario().getText().trim();
            if (nuevo.isEmpty()) {
                cuentaUsuarioView.mostrarMensaje(
                        mih.get("sesionUsuario.mensajeError.usuarioVacio")
                );
                return;
            }
            if (usuarioDAO.buscarPorUsername(nuevo) != null) {
                cuentaUsuarioView.mostrarMensaje(
                        mih.get("sesionUsuario.mensajeError.usuarioExis")
                );
                return;
            }
            //actualiza modelo y base datos
            usuario.setUsuario(nuevo);
            usuarioDAO.actualizar(usuario);
            //cambios en interfaz
            cuentaUsuarioView.getTxtNombreUsuario().setText(nuevo);
            cuentaUsuarioView.mostrarMensaje(
                    mih.get("sesionUsuario.mensajeExito")
            );
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
