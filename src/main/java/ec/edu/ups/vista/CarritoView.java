package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 * Subventana interna que muestra y gestiona la interfaz de usuario para
 * crear y editar un carrito de compras.
 * <p>
 * Permite añadir productos por código y cantidad, visualizar el detalle
 * en una tabla con subtotales, IVA y total, y acciones para guardar,
 * cancelar, limpiar campos, actualizar o eliminar items.
 * Admite internacionalización de todos los textos mediante
 * {@link MensajeInternacionalizacionHandler}.
 * </p>
 */
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
    /**
     * Constructor que inicializa la vista, configura la tabla y los íconos.
     */
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

        setIconoEscalado(btnAnadir, "imagenes/agregar_datos.png");
        setIconoEscalado(buscarButton, "imagenes/imagen_iconoBuscar - Copy.png");
        setIconoEscalado(guardarButton, "imagenes/imagen_guardarInfo - Copy.png");
        setIconoEscalado(cancelarButton, "imagenes/icono_cancelar.png");
        setIconoEscalado(limpiarButton, "imagenes/icono_limpiar.png");
        setIconoEscalado(btnEliminar, "imagenes/icono_eliminar.png");
        setIconoEscalado(btnActualizar, "imagenes/icono_actualizar.png");
    }
    /**
     * Carga y escala una imagen como icono de un botón.
     *
     * @param boton Botón donde asignar el icono.
     * @param ruta  Ruta del recurso de imagen.
     */
    private void setIconoEscalado(JButton boton, String ruta) {
        final int ancho = 25;
        final int alto = 25;

        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                boton.setHorizontalTextPosition(SwingConstants.RIGHT);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen " + ruta + " → " + e.getMessage());
        }
    }
    /**
     * Inyecta el handler de mensajes y actualiza todos los textos de la UI.
     *
     * @param mih Handler de internacionalización.
     */
    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }
    /**
     * Actualiza los textos de etiquetas, botones y encabezados de columna
     * de acuerdo al idioma seleccionado.
     */
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
    /**
     * Carga los valores 1-20 en el combo de cantidad.
     */
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

    public JButton getBtnAnadir() {
        return btnAnadir;
    }

    public JButton getBuscarButton() {
        return buscarButton;
    }

    public JTextField getTxtSubtotal() {
        return txtSubtotal;
    }

    public JTextField getTxtIVA() {
        return txtIVA;
    }

    public JTextField getTxtTotal() {
        return txtTotal;
    }

    public JButton getGuardarButton() {
        return guardarButton;
    }

    public JButton getCancelarButton() {
        return cancelarButton;
    }

    public JTable getTable1() {
        return table1;
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

    public JComboBox getComboBox1() {
        return comboBox1;
    }
}
