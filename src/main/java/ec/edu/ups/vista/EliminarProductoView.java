package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EliminarProductoView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable tblProductos;
    private JTextField txtCodigoEliminar;
    private JButton eliminarButton;
    private JButton buscarButton;
    private DefaultTableModel modelo;
    private JLabel textoPrincipal;
    private JTextField campoNombre;

    private final ProductoController productoC;

    public EliminarProductoView(ProductoController productoC) {
        this.productoC = productoC;

        setContentPane(panelPrincipal);
        setTitle("Eliminar Productos");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio"};
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);

    }

    public void cargarTabla(List<Producto> lista) {
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    public void removerFila(int codigo) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).toString().equals(String.valueOf(codigo))) {
                modelo.removeRow(i);
                break;
            }
        }
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    public JTextField getTxtCodigoEliminar() {
        return txtCodigoEliminar;
    }

    public void setTxtCodigoEliminar(JTextField txtCodigoEliminar) {
        this.txtCodigoEliminar = txtCodigoEliminar;
    }

    public JButton getEliminarButton() {
        return eliminarButton;
    }

    public void setEliminarButton(JButton eliminarButton) {
        this.eliminarButton = eliminarButton;
    }

    public JButton getBuscarButton() {
        return buscarButton;
    }

    public void setBuscarButton(JButton buscarButton) {
        this.buscarButton = buscarButton;
    }

    public JLabel getTextoPrincipal() {
        return textoPrincipal;
    }

    public void setTextoPrincipal(JLabel textoPrincipal) {
        this.textoPrincipal = textoPrincipal;
    }


}