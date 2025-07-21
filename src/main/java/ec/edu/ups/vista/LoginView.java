package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import ec.edu.ups.util.*;
/**
 * Vista de inicio de sesión del sistema.
 * <p>
 * Extiende {@link JFrame} y proporciona elementos para que el usuario
 * ingrese sus credenciales, seleccione el idioma, inicie sesión,
 * se registre o recupere su contraseña.</p>
 */
public class LoginView extends JFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JPasswordField txtContrasenia;
    private JComboBox<IdiomaUsado> comboBoxIdioma;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;
    private JLabel lblIdioma;
    private JLabel lblIniciarSesion;
    private JButton btnRecuperarContra;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Construye la vista de login, inicializa componentes, idioma por defecto
     * y configura acciones e iconos.
     */
    public LoginView() {
        setContentPane(panelPrincipal);
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(550, 450);

        //idioma por defecto
        mih = new MensajeInternacionalizacionHandler("es", "EC");

        setIconoEscalado(btnIniciarSesion, "imagenes/imagen_inicioSesion.png", 25, 25);
        setIconoEscalado(btnRegistrarse, "imagenes/imagen_registrarse.png", 25, 25);
        setIconoEscalado(btnRecuperarContra, "imagenes/imagen_guardarDatos.png", 25, 25);

        idiomaComboBox(); //inicializamos el combo box con los idiomas que deseamos
        actualizarTextos(); //asigna textos segun el idioma elegido

        comboBoxIdioma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IdiomaUsado sel = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
                actualizarTextos();
            }

        });
    }
    /**
     * Configura icono escalado para un botón.
     *
     * @param boton botón al que asignar el icono
     * @param ruta  ruta del recurso de imagen en el classpath
     * @param ancho ancho deseado para el icono
     * @param alto  alto deseado para el icono
     */
    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es","EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en","US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it","IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }

    private void actualizarTextos() {
        setTitle(mih.get("login.titulo"));
        lblIniciarSesion.setText(mih.get("login.titulo"));
        lblIdioma.setText(mih.get("login.txtIdioma"));
        lblUsuario.setText(mih.get("login.txtUsuario"));
        lblContrasenia.setText(mih.get("login.txtContrasenia"));
        btnIniciarSesion.setText(mih.get("button.login"));
        btnRegistrarse.setText(mih.get("button.registrar"));
        btnRecuperarContra.setText(mih.get("button.olvido.contrasenia"));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
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

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public JPasswordField getTxtContrasenia() {
        return txtContrasenia;
    }

    public JComboBox<IdiomaUsado> getComboBoxIdioma() {
        return comboBoxIdioma;
    }

    public JButton getBtnRecuperarContra() {
        return btnRecuperarContra;
    }
}
