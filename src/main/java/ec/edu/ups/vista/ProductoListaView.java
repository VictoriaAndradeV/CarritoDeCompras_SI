package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaView extends JInternalFrame {

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JPanel panelPrincipal;
    private JPanel panelSup;
    private JButton btnListar;
    private JLabel txtNombre;
    private JButton btnLimpiar;
    private JLabel txtTitulo;
    private DefaultTableModel modelo;

    private MensajeInternacionalizacionHandler mih;

    public ProductoListaView() {
        setContentPane(panelPrincipal);
        setTitle("Listado de Productos");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(650, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio"};
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);
    }

    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(mih.get("listarP.titulo"));
        txtTitulo.setText(mih.get("listarP.titulo"));
        txtNombre.setText(mih.get("listarP.txtNombre"));
        btnBuscar.setText(mih.get("listarP.btnBuscar"));
        btnListar.setText(mih.get("listarP.btnListar"));
        btnLimpiar.setText(mih.get("listarP.btnLimpiar"));

        // columnas de la tabla
        Object[] columnas = {
                mih.get("listarP.colCodigo"),
                mih.get("listarP.colNombre"),
                mih.get("listarP.colPrecio")
        };
        modelo.setColumnIdentifiers(columnas);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void cargarDatos(List<Producto> listaProductos) {
        modelo.setNumRows(0);

        for (Producto producto : listaProductos) {
            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecio()
            };
            modelo.addRow(fila);
        }
    }

    //Getters y setters
    public JPanel getPanelSup() {
        return panelSup;
    }

    public void setPanelSup(JPanel panelSup) {
        this.panelSup = panelSup;
    }

    public JLabel getTxtTitulo() {
        return txtTitulo;
    }

    public void setTxtTitulo(JLabel txtTitulo) {
        this.txtTitulo = txtTitulo;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public void setTxtBuscar(JTextField txtBuscar) {
        this.txtBuscar = txtBuscar;
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

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public JLabel getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JLabel txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void setBtnLimpiar(JButton btnLimpiar) {
        this.btnLimpiar = btnLimpiar;
    }
}
