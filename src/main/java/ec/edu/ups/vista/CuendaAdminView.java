package ec.edu.ups.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CuendaAdminView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton btnListar;
    private JButton btnEliminar;
    private JTextField textField1;
    private JButton btnBuscar;
    private JButton btnModificarContra;
    private JButton btnModificarNom;
    private JButton btnCerrarSesion;

    private DefaultTableModel modelo;

    public CuendaAdminView() {
        super("Cuenta administrador", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Usuario"};
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);
    }

    public void cargarUsuarios(java.util.List<String> usuarios) {
        modelo.setRowCount(0);
        for (String nombre : usuarios) {
            modelo.addRow(new Object[]{ nombre });
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public void setBtnCerrarSesion(JButton btnCerrarSesion) {
        this.btnCerrarSesion = btnCerrarSesion;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTable getTable1() {
        return table1;
    }

    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JButton getBtnModificarContra() {
        return btnModificarContra;
    }

    public void setBtnModificarContra(JButton btnModificarContra) {
        this.btnModificarContra = btnModificarContra;
    }

    public JButton getBtnModificarNom() {
        return btnModificarNom;
    }

    public void setBtnModificarNom(JButton btnModificarNom) {
        this.btnModificarNom = btnModificarNom;
    }
}
