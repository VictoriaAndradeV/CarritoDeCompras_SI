package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 * Ventana para buscar y modificar productos.
 * Permite al usuario:
 * - Buscar productos por nombre.
 * - Ver los resultados en una tabla.
 * - Cambiar el nombre y el precio de un producto seleccionado.
 * También cambia los textos al idioma seleccionado (internacionalización).
 */
public class ModificarProductoView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtNombreBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JLabel lblPrice;
    private JButton btnModificar;
    private JLabel tituloModificar;
    private JLabel lblNombre;
    private DefaultTableModel modelo;
    private final ProductoController productoController;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Crea la ventana para modificar productos.
     *
     * @param productoController el controlador que maneja los productos.
     */
    public ModificarProductoView(ProductoController productoController) {
        this.productoController = productoController;
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(500, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        setIconoEscalado(btnBuscar, "imagenes/imagen_iconoBuscar - Copy.png", 25, 25);
        setIconoEscalado(btnModificar, "imagenes/modificarDatos.png", 25, 25);

        // Modelo provisional de la tabla
        modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new Object[]{"Codigo", "Nombre", "Precio"});
        tblProductos.setModel(modelo);
    }
    /**
     * Asigna un icono a un botón, redimensionando la imagen.
     */
    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen " + ruta + " → " + e.getMessage());
        }
    }
    /**
     * Recibe el objeto que se encarga de traducir los textos y actualiza la vista.
     */
    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }
    /**
     * Cambia los textos de la vista según el idioma.
     */
    private void actualizarTextos() {
        setTitle(mih.get("modificarP.titulo"));
        tituloModificar.setText(mih.get("modificarP.titulo"));
        lblNombre.setText(mih.get("modificarP.txtNombre"));
        lblPrice.setText(mih.get("modificarP.txtPrecio"));
        btnBuscar.setText(mih.get("modificarP.btnBuscar"));
        btnModificar.setText(mih.get("modificarP.btnModificar"));
        // columnas de la tabla
        modelo.setColumnIdentifiers(new Object[]{
                mih.get("modificarP.colCodigo"),
                mih.get("modificarP.colNombre"),
                mih.get("modificarP.colPrecio")
        });
    }
    /**
     * Muestra un mensaje emergente.
     */
    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    /**
     * Llena la tabla con una lista de productos.
     */
    public void cargarTabla(List<Producto> lista) {
        modelo.setRowCount(0);

        if (mih == null) {
            throw new IllegalStateException("MensajeInternacionalizacionHandler no ha sido inicializado.");
        }

        for (Producto p : lista) {
            String precioFormateado = FormateadorUtils.formatearMoneda(p.getPrecio(), mih.getLocale());
            modelo.addRow(new Object[]{p.getCodigo(), p.getNombre(), precioFormateado});
        }
    }
    //getters y setters
    public JTable getTblProductos() {
        return tblProductos;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtNombreBuscar() {
        return txtNombreBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
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

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }
}