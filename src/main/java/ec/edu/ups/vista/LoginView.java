package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import ec.edu.ups.util.*;

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

    public LoginView() {
        setContentPane(panelPrincipal);
        setTitle("Iniciar Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 400);

        //idioma por defecto
        mih = new MensajeInternacionalizacionHandler("es", "EC");

        setIconoEscalado(btnIniciarSesion, "imagenes/imagen_inicioSesion.png", 25, 25);
        setIconoEscalado(btnRegistrarse, "imagenes/imagen_registrarse.png", 25, 25);
        setIconoEscalado(btnRecuperarContra, "imagenes/imagen_guardarDatos.png", 25, 25);

        idiomaComboBox(); //inicializamos el combo box con los idiomas que deseamos

        actualizarTextos(); //asigna textos segun el idioma elegido

        //cada vez que se seleccione otro idioma del combo
        comboBoxIdioma.addActionListener(e -> {
            IdiomaUsado sel = (IdiomaUsado) comboBoxIdioma.getSelectedItem();

            //se va al boundle con el idioma y pais
            mih.setLenguaje(sel.getLocale().getLanguage(),sel.getLocale().getCountry());

            //nuevamente recarga la pantalla
            actualizarTextos();
        });
    }

    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
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

    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es","EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en","US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it","IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }

    private void actualizarTextos() {
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

    //getter - setter
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

    public void setBtnIniciarSesion(JButton btnIniciarSesion) {
        this.btnIniciarSesion = btnIniciarSesion;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JPasswordField getTxtContrasenia() {
        return txtContrasenia;
    }

    public void setTxtContrasenia(JPasswordField txtContrasenia) {
        this.txtContrasenia = txtContrasenia;
    }

    public JComboBox<IdiomaUsado> getComboBoxIdioma() {
        return comboBoxIdioma;
    }

    public void setComboBoxIdioma(JComboBox<IdiomaUsado> comboBoxIdioma) {
        this.comboBoxIdioma = comboBoxIdioma;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public JLabel getLblContrasenia() {
        return lblContrasenia;
    }

    public void setLblContrasenia(JLabel lblContrasenia) {
        this.lblContrasenia = lblContrasenia;
    }

    public JLabel getLblIdioma() {
        return lblIdioma;
    }

    public void setLblIdioma(JLabel lblIdioma) {
        this.lblIdioma = lblIdioma;
    }

    public JLabel getLblIniciarSesion() {
        return lblIniciarSesion;
    }

    public void setLblIniciarSesion(JLabel lblIniciarSesion) {
        this.lblIniciarSesion = lblIniciarSesion;
    }

    public JButton getBtnRecuperarContra() {
        return btnRecuperarContra;
    }

    public void setBtnRecuperarContra(JButton btnRecuperarContra) {
        this.btnRecuperarContra = btnRecuperarContra;
    }
}
