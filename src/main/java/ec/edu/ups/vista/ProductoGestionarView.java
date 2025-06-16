package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoGestionar extends JFrame {

    private JPanel panelPrincipal;

    // Eliminar
    private JTextField textField1;
    private JButton buscarButton;
    private JTable table1;
    private JButton eliminarButton;
    private JLabel textoPrincipal;

    // Modificar
    private JTextField textField2;
    private JButton buscarButton1;
    private JTextField textField3;
    private JTextField textField4;
    private JButton modificarButton;
    private JLabel textoModificar;

    private DefaultTableModel modelo;

    public ProductoGestionar() {

        setContentPane(panelPrincipal);
        setTitle("Gestionar Productos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(730, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Inicializar modelo de tabla
        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio"};
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);
    }


    public JTextField getTxtCodigoEliminar() {
        return textField1;
    }

    public JButton getBtnBuscarEliminar() {
        return buscarButton;
    }

    public JButton getBtnEliminar() {
        return eliminarButton;
    }

    public JTable getTablaProductos() {
        return table1;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    // Modificar
    public JTextField getTxtCodigoModificar() {
        return textField2;
    }

    public JButton getBtnBuscarModificar() {
        return buscarButton1;
    }

    public JTextField getTxtNuevoNombre() {
        return textField3;
    }

    public JTextField getTxtNuevoPrecio() {
        return textField4;
    }

    public JButton getBtnModificar() {
        return modificarButton;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void cargarDatos(List<Producto> productos) {
        modelo.setRowCount(0);
        for (Producto producto : productos) {
            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecio()
            };
            modelo.addRow(fila);
        }
    }

    public void limpiarCamposEliminar() {
        textField1.setText("");
    }

    public void limpiarCamposModificar() {
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }
}
