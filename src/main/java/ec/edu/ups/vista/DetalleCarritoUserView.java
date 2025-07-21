package ec.edu.ups.vista;

import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Locale;
/**
 * Vista interna para mostrar los detalles de los ítems en el carrito de un usuario.
 * <p>
 * Extiende {@link JInternalFrame} y permite listar, modificar y eliminar
 * ítems seleccionados del carrito de compras. Soporta internacionalización de textos
 * y formateo de precios según la configuración regional.</p>
 */
public class DetalleCarritoUserView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable tablaDetalles;
    private JButton btnEliminar;
    private JButton btnModificar;
    private JLabel tituloDetalle;

    private DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler mih;
    /**
     * Construye la vista de detalles del carrito.
     * <p>
     * Inicializa el frame, configura tamaño, iconos de botones y modelo de la tabla.
     */
    public DetalleCarritoUserView() {
        super("Detalle del carrito", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(500, 400);

        setIconoEscalado(btnEliminar, "imagenes/icono_eliminar.png", 25, 25);
        setIconoEscalado(btnModificar, "imagenes/modificarDatos.png", 25, 25);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio", "Cantidad", "Precio Total"};
        modelo.setColumnIdentifiers(columnas);
        tablaDetalles.setModel(modelo);
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
        setTitle(mih.get("detalleC.usuario.titulo"));
        tituloDetalle.setText(mih.get("detalleC.usuario.titulo"));
        btnEliminar.setText(mih.get("eliminarP.btnEliminar"));
        btnModificar.setText(mih.get("modificarP.btnModificar"));

        modelo.setColumnIdentifiers(new Object[]{
                mih.get("agregarP.txtCodigo"),
                mih.get("agregarP.txtNombre"),
                mih.get("agregarP.txtPrecio"),
                mih.get("carrito.colCantidad"),
                mih.get("listarC.colPrecio")
        });
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    /**
     * Carga los ítems del carrito en la tabla, formateando precios según locale.
     *
     * @param items lista de {@link ItemCarrito} a mostrar
     */
    public void cargarDatos(List<ItemCarrito> items) {
        modelo.setRowCount(0);
        Locale locale = (mih != null) ? mih.getLocale() : Locale.getDefault();

        for (ItemCarrito it : items) {
            String precioUnitario = FormateadorUtils.formatearMoneda(it.getProducto().getPrecio(), locale);
            String precioTotal = FormateadorUtils.formatearMoneda(it.getCantidad() * it.getProducto().getPrecio(), locale);

            modelo.addRow(new Object[]{
                    it.getProducto().getCodigo(),
                    it.getProducto().getNombre(),
                    precioUnitario,
                    it.getCantidad(),
                    precioTotal
            });
        }
    }


    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
    public JLabel getTituloDetalle() {
        return tituloDetalle;
    }

    public void setTituloDetalle(JLabel tituloDetalle) {
        this.tituloDetalle = tituloDetalle;
    }

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
