package ec.edu.ups.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ListarCarritoAdminView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JButton btnBuscar;
    private JPanel panelSecundario;
    private JTable table1;
    private JButton btnCarrito;

    private DefaultTableModel modelo;

    public ListarCarritoAdminView() {
        super("Listado Carritos Admin", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(400, 400);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Usuario"};
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);
    }
    //falta cargar datos de la tabla

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
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getBtnCarrito() {
        return btnCarrito;
    }

    public void setBtnCarrito(JButton btnCarrito) {
        this.btnCarrito = btnCarrito;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTable getTable1() {
        return table1;
    }

    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    public JPanel getPanelSecundario() {
        return panelSecundario;
    }

    public void setPanelSecundario(JPanel panelSecundario) {
        this.panelSecundario = panelSecundario;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }
}
