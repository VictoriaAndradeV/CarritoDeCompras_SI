package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarritoView extends JInternalFrame {
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAnadir;
    private JButton buscarButton;
    private JTextField txtSubtotal;
    private JTextField txtIVA;
    private JTextField txtTotal;
    private JButton guardarButton;
    private JButton cancelarButton;
    private JTable table1;
    private JPanel panelPrincipal;
    private JButton limpiarButton;
    private JComboBox comboBox1;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JLabel txtTitulo;
    private JLabel lblCodigo;
    private JLabel lblNombre;
    private JLabel lblPrecio;
    private JLabel lblCantidad;
    private JLabel lblSubtotal;
    private JLabel lblIVA;
    private JLabel lblTotal;

    private MensajeInternacionalizacionHandler mih;

    public CarritoView() {
        super("Carrito de Compras", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 500);

        DefaultTableModel modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio", "Cantidad", "Subtotal"};
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);
        cargarDatos();

        setIconoEscalado(btnAnadir, "imagenes/agregar_datos.png", 25, 25);
        setIconoEscalado(buscarButton, "imagenes/imagen_iconoBuscar - Copy.png", 25, 25);
        setIconoEscalado(guardarButton, "imagen_guardarInfo.png", 25, 25);
        setIconoEscalado(cancelarButton, "imagenes/icono_cancelar.png", 25, 25);
        setIconoEscalado(limpiarButton, "imagenes/icono_limpiar.png", 25, 25);
        setIconoEscalado(btnEliminar, "imagenes/icono_eliminar.png", 25, 25);
        setIconoEscalado(btnActualizar, "imagenes/icono_actualizar.png", 25, 25);
    }

    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                boton.setHorizontalTextPosition(SwingConstants.RIGHT);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen" + ruta + " → " + e.getMessage());
        }
    }

    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }

    private void actualizarTextos() {
        setTitle(mih.get("carrito.titulo"));
        txtTitulo.setText(mih.get("carrito.titulo"));
        lblCodigo.setText(mih.get("carrito.txtCodigo"));
        lblNombre.setText(mih.get("carrito.txtNombre"));
        lblPrecio.setText(mih.get("carrito.txtPrecio"));
        lblCantidad.setText(mih.get("carrito.txtCantidad"));
        lblSubtotal.setText(mih.get("carrito.txtSubtotal"));
        lblIVA.setText(mih.get("carrito.txtIVA"));
        lblTotal.setText(mih.get("carrito.txtTotal"));

        btnAnadir.setText(mih.get("carrito.btnAnadir"));
        buscarButton.setText(mih.get("carrito.btnBuscar"));
        guardarButton.setText(mih.get("carrito.btnGuardar"));
        cancelarButton.setText(mih.get("carrito.btnCancelar"));
        btnActualizar.setText(mih.get("carrito.btnActualizar"));
        btnEliminar.setText(mih.get("carrito.btnEliminar"));
        limpiarButton.setText(mih.get("carrito.btnLimpiar")); // reutiliza cancelar label

        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        modelo.setColumnIdentifiers(new Object[]{
                mih.get("carrito.colCodigo"),
                mih.get("carrito.colNombre"),
                mih.get("carrito.colPrecio"),
                mih.get("carrito.colCantidad"),
                mih.get("carrito.colSub")
        });

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void cargarDatos(){
        comboBox1.removeAllItems();
        for(int i = 0; i < 20; i++){
            comboBox1.addItem(String.valueOf(i + 1));
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public void setBtnActualizar(JButton btnActualizar) {
        this.btnActualizar = btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
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

    public JButton getBtnAnadir() {
        return btnAnadir;
    }

    public void setBtnAnadir(JButton btnAnadir) {
        this.btnAnadir = btnAnadir;
    }

    public JButton getBuscarButton() {
        return buscarButton;
    }

    public void setBuscarButton(JButton buscarButton) {
        this.buscarButton = buscarButton;
    }

    public JTextField getTxtSubtotal() {
        return txtSubtotal;
    }

    public void setTxtSubtotal(JTextField txtSubtotal) {
        this.txtSubtotal = txtSubtotal;
    }

    public JTextField getTxtIVA() {
        return txtIVA;
    }

    public void setTxtIVA(JTextField txtIVA) {
        this.txtIVA = txtIVA;
    }

    public JTextField getTxtTotal() {
        return txtTotal;
    }

    public void setTxtTotal(JTextField txtTotal) {
        this.txtTotal = txtTotal;
    }

    public JButton getGuardarButton() {
        return guardarButton;
    }

    public void setGuardarButton(JButton guardarButton) {
        this.guardarButton = guardarButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public void setCancelarButton(JButton cancelarButton) {
        this.cancelarButton = cancelarButton;
    }

    public JTable getTable1() {
        return table1;
    }

    public void setTable1(JTable table1) {
        this.table1 = table1;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getLimpiarButton() {
        return limpiarButton;
    }

    public void setLimpiarButton(JButton actualizarButton) {
        this.limpiarButton = actualizarButton;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
    }
}
