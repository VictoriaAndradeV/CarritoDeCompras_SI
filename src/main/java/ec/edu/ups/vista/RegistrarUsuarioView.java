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
 * Vista para el registro de usuarios en el sistema.
 * Esta clase extiende JFrame y proporciona una interfaz gráfica
 * para que el usuario ingrese sus datos (cédula, contraseña,
 * nombre, apellido, correo, fecha de nacimiento, teléfono),
 * seleccione el idioma de la interfaz y realice validaciones
 * en cada campo al perder el foco.
 *
 */
public class RegistrarUsuarioView extends JFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JButton btnRegistrarse;
    private JPasswordField passwordField1;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;
    private JLabel lblRegistrarUsuario;
    private JLabel lblNombreUsuario;
    private JLabel lblApellido;
    private JLabel lblEmail;
    private JLabel lblFechaN;
    private JLabel lblTelefono;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JComboBox comboBoxIdioma;
    private JLabel lblIdioma;
    private JButton btnCancelar;
    private MensajeInternacionalizacionHandler mih;
    /**
     * Construye la vista de registro de usuario, configura la internacionalización,
     * los iconos, los eventos de cambio de idioma y las validaciones por campo.
     */
    public RegistrarUsuarioView() {
        setContentPane(panelPrincipal);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(550, 450);

        mih = new MensajeInternacionalizacionHandler("es", "EC");

        setIconoEscalado(btnRegistrarse, "imagenes/imagen_registrarse.png");
        setIconoEscalado(btnCancelar, "imagenes/imagen_cancelar.png");

        idiomaComboBox();
        actualizarTextos(); //actualiza los textos segun el idioma elegido

        //evento que cambia el idioma
        comboBoxIdioma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                IdiomaUsado seleccionado = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(seleccionado.getLocale().getLanguage(), seleccionado.getLocale().getCountry());
                actualizarTextos();
            }
        });

        configurarValidaciones();
    }
    /**
     * Configura los FocusListeners que validan cada campo cuando el usuario pierde el foco.
     */
    private void configurarValidaciones() {
        // valida campo cédula
        txtUsuario.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarCedula();
            }
        });

        // valida campo contraseña
        passwordField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarContrasenia();
            }
        });

        // valida campo nombre
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarNombre();
            }
        });

        // valida campo apellido
        textField2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarApellido();
            }
        });

        // valida campo email
        textField3.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarEmail();
            }
        });

        // valida campo teléfono
        textField5.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validarTelefono();
            }
        });
    }
    /**
     * Valida la cédula usando la lógica de la clase Usuario.
     * Si hay error, muestra un mensaje y limpia el campo.
     */
    private void validarCedula() {
        String ced = txtUsuario.getText().trim();
        if (ced.isEmpty()) return;
        try {
            new Usuario().setCedula(ced);
        } catch (ExcepcionCedula ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Cédula inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            txtUsuario.setText("");
        }

    }
    /**
     * Valida la contraseña mediante la lógica de Usuario.
     * En caso de excepción, muestra mensaje y limpia el campo.
     */
    private void validarContrasenia() {
        String contra = new String(passwordField1.getPassword()).trim();
        if (contra.isEmpty()) return;
        try {
            new Usuario().setContrasenia(contra);
        } catch (ExcepcionContrasenia ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Contraseña inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            passwordField1.setText("");
        }

    }
    /**
     * Valida el nombre del usuario.
     */
    private void validarNombre() {
        String nombre = textField1.getText().trim();
        if (nombre.isEmpty()) return;
        try {
            new Usuario().setNombre(nombre);
        } catch (ExcepcionNomApe ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Nombre inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            textField1.setText("");
        }
    }
    /**
     * Valida el apellido del usuario.
     */
    private void validarApellido() {
        String apellido = textField2.getText().trim();
        if (apellido.isEmpty()) return;
        try {
            new Usuario().setApellido(apellido);
        } catch (ExcepcionNomApe ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Apellido inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            textField2.setText("");
        }
    }
    /**
     * Valida el correo electrónico.
     */
    private void validarEmail() {
        String correo = textField3.getText().trim();
        if (correo.isEmpty()) return;
        try {
            new Usuario().setEmail(correo);
        } catch (ExcepcionCorreo ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Correo inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            textField3.setText("");
        }
    }
    /**
     * Valida el número de teléfono.
     */
    private void validarTelefono() {
        String telefono = textField5.getText().trim();
        if (telefono.isEmpty()) return;
        try {
            new Usuario().setTelefono(telefono);
        } catch (ExcepcionTelefono ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Teléfono inválido",
                    JOptionPane.ERROR_MESSAGE
            );
            textField5.setText("");
        }
    }
    /**
     * Inicializa el comboBox con las opciones de idioma soportadas.
     */
    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es","EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en","US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it","IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }
    /**
     * Actualiza todos los textos de la interfaz de acuerdo con el idioma seleccionado.
     */
    public void actualizarTextos() {
        setTitle(mih.get("registrar.titulo"));
        lblRegistrarUsuario.setText(mih.get("registrar.titulo"));
        lblIdioma.setText(mih.get("login.txtIdioma"));
        lblUsuario.setText(mih.get("registrar.txtUsuario"));
        lblContrasenia.setText(mih.get("registrar.txtContrasenia"));
        lblNombreUsuario.setText(mih.get("registrar.txtNombre"));
        lblApellido.setText(mih.get("registrar.txtApellido"));
        lblEmail.setText(mih.get("registrar.txtEmail"));
        lblFechaN.setText(mih.get("registrar.txtFechaN"));
        lblTelefono.setText(mih.get("registrar.txtTelefono"));
        btnRegistrarse.setText(mih.get("btn.registrar"));
    }

    private void setIconoEscalado(JButton boton, String ruta) {
        int ancho = 25;
        int alto = 25;
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen " + ruta + " → " + e.getMessage());
        }
    }
    /**
     * Muestra un mensaje genérico en un diálogo.
     *
     * @param mensaje texto a mostrar al usuario
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
    /**
     * Limpia todos los campos de entrada de la vista.
     */
    public void limpiarCampos() {
        txtUsuario.setText("");
        passwordField1.setText("");
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
    }

    //getters y setters

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public JTextField getTextField3() {
        return textField3;
    }

    public JTextField getTextField4() {
        return textField4;
    }

    public JTextField getTextField5() {
        return textField5;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public MensajeInternacionalizacionHandler getMih() {
        return mih;
    }

    public void setMih(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
    }
}
