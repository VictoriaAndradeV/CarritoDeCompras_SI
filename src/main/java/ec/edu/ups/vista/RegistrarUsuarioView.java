package ec.edu.ups.vista;

import ec.edu.ups.modelo.Rol;

import javax.swing.*;

public class RegistrarUsuarioView extends JFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JComboBox comboBoxRol;
    private JButton btnRegistrarse;
    private JPasswordField passwordField1;

    public RegistrarUsuarioView() {
        setContentPane(panelPrincipal);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(400, 250);


        comboBoxRol.removeAllItems();
        for (Rol rol : Rol.values()) {
            comboBoxRol.addItem(rol);
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        passwordField1.setText("");
        comboBoxRol.setSelectedIndex(0);
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

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public void setPasswordField1(JPasswordField passwordField1) {
        this.passwordField1 = passwordField1;
    }

    public JComboBox getComboBoxRol() {
        return comboBoxRol;
    }

    public void setComboBoxRol(JComboBox comboBoxRol) {
        this.comboBoxRol = comboBoxRol;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }
}
