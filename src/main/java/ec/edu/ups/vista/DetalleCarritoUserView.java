package ec.edu.ups.vista;

import ec.edu.ups.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DetalleCarritoUserView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable tablaDetalles;
    private JButton btnEliminar;
    private JButton btnModificar;

    private DefaultTableModel modelo;

    public DetalleCarritoUserView() {
        super("Detalle del carrito", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 400);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio", "Cantidad", "Precio Total"};
        modelo.setColumnIdentifiers(columnas);
        tablaDetalles.setModel(modelo);
    }

    public void cargarDatos(List<ItemCarrito> items) {
        modelo.setRowCount(0);
        for (ItemCarrito it : items) {
            modelo.addRow(new Object[]{
                    it.getProducto().getCodigo(),
                    it.getProducto().getNombre(),
                    String.format("%.2f", it.getProducto().getPrecio()),
                    it.getCantidad(),
                    String.format("%.2f", it.getCantidad() * it.getProducto().getPrecio())
            });
        }
    }


    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JTable getTablaDetalles() {
        return tablaDetalles;
    }

    public void setTablaDetalles(JTable tablaDetalles) {
        this.tablaDetalles = tablaDetalles;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }
}
