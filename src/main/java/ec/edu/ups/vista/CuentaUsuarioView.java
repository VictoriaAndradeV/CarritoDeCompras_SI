package ec.edu.ups.vista;

import javax.swing.*;

public class CuentaUsuarioView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtNombreUsuario;
    private JButton editarNombreButton;
    private JTextField textField1;
    private JButton cambiarButton;
    private JButton btnEliminarCuenta;
    private JButton btnCerrarSesion;
    private JButton btnActualizar;

    public CuentaUsuarioView() {
        super("Cuenta de Usuario", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
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

    public JTextField getTxtNombreUsuario() {
        return txtNombreUsuario;
    }

    public void setTxtNombreUsuario(JTextField txtNombreUsuario) {
        this.txtNombreUsuario = txtNombreUsuario;
    }

    public JButton getEditarNombreButton() {
        return editarNombreButton;
    }

    public void setEditarNombreButton(JButton editarNombreButton) {
        this.editarNombreButton = editarNombreButton;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    public JButton getCambiarButton() {
        return cambiarButton;
    }

    public void setCambiarButton(JButton cambiarButton) {
        this.cambiarButton = cambiarButton;
    }

    public JButton getBtnEliminarCuenta() {
        return btnEliminarCuenta;
    }

    public void setBtnEliminarCuenta(JButton btnEliminarCuenta) {
        this.btnEliminarCuenta = btnEliminarCuenta;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public void setBtnCerrarSesion(JButton btnCerrarSesion) {
        this.btnCerrarSesion = btnCerrarSesion;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public void setBtnActualizar(JButton btnActualizar) {
        this.btnActualizar = btnActualizar;
    }
}
