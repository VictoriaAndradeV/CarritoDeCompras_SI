package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.util.FormateadorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 * {@code ProductoListaView} es una ventana interna (JInternalFrame) encargada de mostrar
 * un listado de productos en una tabla. Ofrece funcionalidad para buscar productos por nombre,
 * listar todos, limpiar la búsqueda y presentar mensajes al usuario.
 * <p>
 * Utiliza internacionalización para adaptar textos estáticos y formatea precios según la localidad.
 * </p>
 */
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
    /**
     * Construye la vista, configura el layout, tamaño, comportamiento y crea el modelo de tabla.
     */
    public ProductoListaView() {
        setContentPane(panelPrincipal);
        setTitle("Listado de Productos");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(650, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        setIconoEscalado(btnBuscar, "imagenes/imagen_iconoBuscar - Copy.png", 25, 25);
        setIconoEscalado(btnListar, "imagenes/icono_listar.png", 25, 25);
        setIconoEscalado(btnLimpiar, "imagenes/icono_limpiar.png", 25, 25);

        modelo = new DefaultTableModel();
        Object[] columnas = {"Codigo", "Nombre", "Precio"};
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);
    }
    /**
     * Asigna el manejador de mensajes para internacionalización y actualiza los textos de la UI.
     *
     * @param mih instancia de MensajeInternacionalizacionHandler configurada con idioma y país
     */
    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }
    /**
     * Actualiza todos los textos estáticos de la interfaz según las claves definidas en el handler.
     */
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
    /**
     * Carga un ícono escalado en un botón a partir de la ruta de recurso.
     *
     * @param boton botón donde se insertará el ícono
     * @param ruta  ruta del recurso de imagen en el classpath
     * @param ancho ancho deseado del ícono en píxeles
     * @param alto  alto deseado del ícono en píxeles
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

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    /**
     * Llena la tabla con los datos de la lista de productos proporcionada.
     * Formatea el precio según la localidad configurada.
     *
     * @param listaProductos lista de instancias de Producto a mostrar
     * @throws IllegalStateException si el manejador de mensajes no ha sido inicializado
     */
    public void cargarDatos(List<Producto> listaProductos) {
        modelo.setNumRows(0);
        if (mih == null) {
            throw new IllegalStateException("MensajeInternacionalizacionHandler no ha sido inicializado.");
        }
        for (Producto producto : listaProductos) {
            Object[] fila = {
                    producto.getCodigo(),
                    producto.getNombre(),
                    FormateadorUtils.formatearMoneda(producto.getPrecio(), mih.getLocale())
            };
            modelo.addRow(fila);
        }
    }

    //Getters y setters
    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
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
}
