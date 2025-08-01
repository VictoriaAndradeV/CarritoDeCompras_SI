package ec.edu.ups.vista;
import ec.edu.ups.util.IdiomaUsado;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

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
    private MensajeInternacionalizacionHandler mih;

    public RegistrarUsuarioView() {
        setContentPane(panelPrincipal);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(550, 450);

        mih = new MensajeInternacionalizacionHandler("es", "EC");

        setIconoEscalado(btnRegistrarse, "imagenes/imagen_registrarse.png", 25, 25);

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
    }

    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es","EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en","US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it","IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }

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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

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
