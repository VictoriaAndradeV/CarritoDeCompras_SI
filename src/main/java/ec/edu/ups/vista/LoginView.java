package ec.edu.ups.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
        comboBoxIdioma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                IdiomaUsado seleccionado = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(seleccionado.getLocale().getLanguage(), seleccionado.getLocale().getCountry());
                actualizarTextos();
            }
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
