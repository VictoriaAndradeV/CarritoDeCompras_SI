package ec.edu.ups.vista;

import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Locale;
/**
 * Vista interna para la cuenta de usuario.
 *
 * Permite al usuario:
 * - Ver y editar sus datos personales (cédula, contraseña,
 *   nombre, apellido, teléfono, correo y fecha de nacimiento).
 * - Cambiar el idioma de la interfaz.
 * - Cerrar sesión.
 * - Eliminar su cuenta.
 */
public class CuentaUsuarioView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField textNombre;
    private JButton cambiarButton;
    private JButton btnEliminarCuenta;
    private JButton btnCerrarSesion;
    private JButton btnActualizar;
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;
    private JTextField textApe;
    private JTextField textTelefo;
    private JTextField textFechaN;
    private JTextField textEmail;
    private JTextField textUsua;
    private JTextField textContra;
    private JComboBox<IdiomaUsado> comboBoxIdioma;
    private JLabel lblIdioma;
    private JLabel lblNombre;
    private JLabel lblApellido;
    private JLabel lblTelefono;
    private JLabel lblFechaN;
    private JLabel lblEmail;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Construye la vista de cuenta de usuario.
     * <p>
     * Inicializa el frame, configura los iconos de botones,
     * el combobox de idiomas y las validaciones de campos.</p>
     */
    public CuentaUsuarioView() {
        super("Cuenta de Usuario", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(750, 600);

        idiomaComboBox();

        comboBoxIdioma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                IdiomaUsado sel = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
                actualizarTextos();
            }
        });

        setIconoEscalado(cambiarButton, "imagenes/modificarDatos.png", 25, 25);
        setIconoEscalado(btnEliminarCuenta, "imagenes/icono_eliminar.png", 25, 25);
        setIconoEscalado(btnCerrarSesion, "imagenes/cerrarSesion.png", 25, 25);
        setIconoEscalado(btnActualizar, "imagenes/icono_actualizar.png", 25, 25);

        configurarValidaciones();
    }
    /**
     * Carga el icono escalado en un botón.
     *
     * @param boton botón al que aplicar el icono
     * @param ruta  ruta del recurso de imagen
     * @param ancho ancho deseado del icono
     * @param alto  alto deseado del icono
     */
    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                boton.setHorizontalTextPosition(SwingConstants.RIGHT);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen" + ruta + " → " + e.getMessage());
        }
    }

    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }

    private void actualizarTextos() {
        setTitle(mih.get("sesionU.titulo"));
        lblTitulo.setText(mih.get("sesionU.titulo"));
        lblIdioma.setText(mih.get("login.txtIdioma"));
        lblUsuario.setText(mih.get("login.txtUsuario"));
        lblContrasenia.setText(mih.get("login.txtContrasenia"));
        lblNombre.setText(mih.get("registrar.txtNombre"));
        lblApellido.setText(mih.get("registrar.txtApellido"));
        lblTelefono.setText(mih.get("registrar.txtTelefono"));
        lblEmail.setText(mih.get("registrar.txtEmail"));
        lblFechaN.setText(mih.get("registrar.txtFechaN"));

        cambiarButton.setText(mih.get("sesionU.txtCambiar"));
        btnEliminarCuenta.setText(mih.get("sesionU.btnEliminar"));
        btnActualizar.setText(mih.get("sesionU.btnActualizar"));
        btnCerrarSesion.setText(mih.get("menu.salir.login"));

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    /**
     * Configura validaciones para cada campo al perder foco.
     * <p>
     * Se invocan los métodos de validación específicos que muestran
     * diálogos de error en caso necesario.</p>
     */
    public void configurarValidaciones() {
        // valida campo cédula
        textUsua.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCedula();
            }
        });

        // valida campo contraseña
        textContra.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarContrasenia();
            }
        });

        // valida campo nombre
        textNombre.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarNombre();
            }
        });

        // valida campo apellido
        textApe.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarApellido();
            }
        });

        // valida campo email
        textEmail.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarEmail();
            }
        });

        // valida campo teléfono
        textTelefo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarTelefono();
            }
        });
    }
    /**
     * Valida la cédula ingresada usando la lógica de {@link Usuario}.
     * Muestra un mensaje de error si la cédula es inválida.
     */
    private void validarCedula() {
        String ced = textUsua.getText().trim();
        if (ced.isEmpty()) return;
        try {
            new Usuario().setCedula(ced);
        } catch (ExcepcionCedula ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cédula inválida", JOptionPane.ERROR_MESSAGE);
            textUsua.setText("");
        }
    }
    /**
     * Valida la contraseña ingresada usando la lógica de {@link Usuario}.
     * Muestra un mensaje de error si la contraseña es inválida.
     */
    private void validarContrasenia() {
        String contra = textContra.getText().trim();
        if (contra.isEmpty()) return;
        try {
            new Usuario().setContrasenia(contra);
        } catch (ExcepcionContrasenia ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Contraseña inválida", JOptionPane.ERROR_MESSAGE);
            textContra.setText("");
        }
    }
    /**
     * Verifica que el nombre ingresado cumpla las reglas de formato de Usuario.
     * Si es inválido, muestra un diálogo con el error y limpia el campo.
     */
    private void validarNombre() {
        String nombre = textNombre.getText().trim();
        if (nombre.isEmpty()) return;
        try {
            new Usuario().setNombre(nombre);
        } catch (ExcepcionNomApe ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Nombre inválido", JOptionPane.ERROR_MESSAGE);
            textNombre.setText("");
        }
    }
    /**
     * Verifica que el apellido ingresado cumpla las reglas de formato de Usuario.
     * Si es inválido, muestra un diálogo con el error y limpia el campo.
     */
    private void validarApellido() {
        String apellido = textApe.getText().trim();
        if (apellido.isEmpty()) return;
        try {
            new Usuario().setApellido(apellido);
        } catch (ExcepcionNomApe ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Apellido inválido", JOptionPane.ERROR_MESSAGE);
            textApe.setText("");
        }
    }
    /**
     * Verifica que el correo ingresado sea válido según Usuario.
     * Si es inválido, muestra un diálogo con el error y limpia el campo.
     */
    private void validarEmail() {
        String correo = textEmail.getText().trim();
        if (correo.isEmpty()) return;
        try {
            new Usuario().setEmail(correo);
        } catch (ExcepcionCorreo ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Correo inválido", JOptionPane.ERROR_MESSAGE);
            textEmail.setText("");
        }
    }
    /**
     * Verifica que el número de teléfono ingresado cumpla el patrón permitido.
     * Si es inválido, muestra un diálogo con el error y limpia el campo.
     */
    private void validarTelefono() {
        String telefono = textTelefo.getText().trim();
        if (telefono.isEmpty()) return;
        try {
            new Usuario().setTelefono(telefono);
        } catch (ExcepcionTelefono ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Teléfono inválido", JOptionPane.ERROR_MESSAGE);
            textTelefo.setText("");
        }
    }
    /**
     * Inicializa comboBoxIdioma con los idiomas soportados y establece
     * el idioma por defecto.
     */
    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es", "EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en", "US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it", "IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JTextField getTextContra() {
        return textContra;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTextNombre() {
        return textNombre;
    }

    public JButton getCambiarButton() {
        return cambiarButton;
    }

    public JButton getBtnEliminarCuenta() {
        return btnEliminarCuenta;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public JTextField getTextApe() {
        return textApe;
    }

    public JTextField getTextTelefo() {
        return textTelefo;
    }

    public JTextField getTextFechaN() {
        return textFechaN;
    }

    public JTextField getTextEmail() {
        return textEmail;
    }

    public JTextField getTextUsua() {
        return textUsua;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }
}
