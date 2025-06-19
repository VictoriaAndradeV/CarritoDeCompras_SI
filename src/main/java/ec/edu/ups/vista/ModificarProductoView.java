package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ModificarProductoView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtNombreBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnModificar;
    private DefaultTableModel modelo;
    private final ProductoController productoController;


    public ModificarProductoView(ProductoController productoController) {
        this.productoController = productoController;

        setContentPane(panelPrincipal);
        setTitle("Modificar Productos");
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

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }


    public void cargarTabla(List<Producto> lista) {
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    public JTextField getTxtNombreBuscar() {
        return txtNombreBuscar;
    }

    public void setTxtNombreBuscar(JTextField txtNombreBuscar) {
        this.txtNombreBuscar = txtNombreBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public void setTxtPrecio(JTextField txtPrecio) {
        this.txtPrecio = txtPrecio;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }
}