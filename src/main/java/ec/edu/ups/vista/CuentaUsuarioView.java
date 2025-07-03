package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

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
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;

    private MensajeInternacionalizacionHandler mih;

    public CuentaUsuarioView() {
        super("Cuenta de Usuario", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);
    }

    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }

    private void actualizarTextos() {
        // Título de la ventana
        setTitle(mih.get("sesionU.titulo"));
        lblTitulo.setText(mih.get("sesionU.titulo"));
        lblUsuario.setText(mih.get("login.txtUsuario"));
        lblContrasenia.setText(mih.get("login.txtContrasenia"));

        editarNombreButton.setText(mih.get("sesionU.txtEditar"));
        cambiarButton.setText(mih.get("sesionU.txtCambiar"));
        btnEliminarCuenta.setText(mih.get("sesionU.btnEliminar"));
        btnActualizar.setText(mih.get("sesionU.btnActualizar"));
        btnCerrarSesion.setText(mih.get("menu.salir.cerrarSesion"));

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }


    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }


    //getters y setters
    public JLabel getLblTitulo() {
        return lblTitulo;
    }

    public void setLblTitulo(JLabel lblTitulo) {
        this.lblTitulo = lblTitulo;
    }

    public JLabel getLblContrasenia() {
        return lblContrasenia;
    }

    public void setLblContrasenia(JLabel lblContrasenia) {
        this.lblContrasenia = lblContrasenia;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

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
