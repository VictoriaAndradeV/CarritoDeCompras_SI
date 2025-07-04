package ec.edu.ups.vista;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;

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
    private JButton btnSiguiente;
    private MensajeInternacionalizacionHandler mih;

    public RegistrarUsuarioView() {
        setContentPane(panelPrincipal);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(550, 400);

        setIconoEscalado(btnRegistrarse, "imagenes/imagen_registrarse.png", 25, 25);
    }

    public void actualizarTextos() {
        lblRegistrarUsuario.setText(mih.get("registrar.titulo"));
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
    public JButton getBtnSiguiente() {
        return btnSiguiente;
    }

    public void setBtnSiguiente(JButton btnSiguiente) {
        this.btnSiguiente = btnSiguiente;
    }

    public JLabel getLblNombreUsuario() {
        return lblNombreUsuario;
    }

    public void setLblNombreUsuario(JLabel lblNombreUsuario) {
        this.lblNombreUsuario = lblNombreUsuario;
    }

    public JLabel getLblApellido() {
        return lblApellido;
    }

    public void setLblApellido(JLabel lblApellido) {
        this.lblApellido = lblApellido;
    }

    public JLabel getLblEmail() {
        return lblEmail;
    }

    public void setLblEmail(JLabel lblEmail) {
        this.lblEmail = lblEmail;
    }

    public JLabel getLblFechaN() {
        return lblFechaN;
    }

    public void setLblFechaN(JLabel lblFechaN) {
        this.lblFechaN = lblFechaN;
    }

    public JLabel getLblTelefono() {
        return lblTelefono;
    }

    public void setLblTelefono(JLabel lblTelefono) {
        this.lblTelefono = lblTelefono;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    public JTextField getTextField2() {
        return textField2;
    }

    public void setTextField2(JTextField textField2) {
        this.textField2 = textField2;
    }

    public JTextField getTextField3() {
        return textField3;
    }

    public void setTextField3(JTextField textField3) {
        this.textField3 = textField3;
    }

    public JTextField getTextField4() {
        return textField4;
    }

    public void setTextField4(JTextField textField4) {
        this.textField4 = textField4;
    }

    public JTextField getTextField5() {
        return textField5;
    }

    public void setTextField5(JTextField textField5) {
        this.textField5 = textField5;
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

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public void setPasswordField1(JPasswordField passwordField1) {
        this.passwordField1 = passwordField1;
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

    public JLabel getLblRegistrarUsuario() {
        return lblRegistrarUsuario;
    }

    public void setLblRegistrarUsuario(JLabel lblRegistrarUsuario) {
        this.lblRegistrarUsuario = lblRegistrarUsuario;
    }

    public MensajeInternacionalizacionHandler getMih() {
        return mih;
    }

    public void setMih(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
    }
}
