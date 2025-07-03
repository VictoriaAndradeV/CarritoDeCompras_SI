package ec.edu.ups.vista;

import javax.swing.*;
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
        setSize(400, 300);

        //idioma por defecto
        mih = new MensajeInternacionalizacionHandler("es", "EC");

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
