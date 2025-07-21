package ec.edu.ups.vista;

import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 * Vista interna para la gestión de cuentas de administrador.
 * <p>
 * Esta clase extiende {@link JInternalFrame} y proporciona una interfaz
 * para listar, buscar, eliminar y modificar datos de usuarios desde la
 * perspectiva de un administrador.</p>
 *
 * <ul>
 *   <li>Permite listar todos los usuarios en una tabla.</li>
 *   <li>Buscar un usuario por cédula.</li>
 *   <li>Eliminar un usuario seleccionado.</li>
 *   <li>Modificar el nombre de usuario o su contraseña.</li>
 *   <li>Soporta internacionalización de textos mediante {@link MensajeInternacionalizacionHandler}.</li>
 * </ul>
 */
public class CuendaAdminView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTable table1;
    private JButton btnListar;
    private JButton btnEliminar;
    private JTextField textField1;
    private JButton btnBuscar;
    private JButton btnModificarContra;
    private JButton btnModificarNom;
    private JLabel lblNombre;
    private JLabel tituloVentana;

    private final DefaultTableModel modelo;
    private MensajeInternacionalizacionHandler mih;
    /**
     * Constructor por defecto que inicializa la ventana interna.
     * <p>
     * Configura el título, tamaño, iconos de los botones y modelo de la tabla.</p>
     */
    public CuendaAdminView() {
        super("Cuenta administrador", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(850, 500);

        setIconoEscalado(btnEliminar, "imagenes/icono_eliminar.png");
        setIconoEscalado(btnBuscar, "imagenes/imagen_iconoBuscar - Copy.png");
        setIconoEscalado(btnModificarContra, "imagenes/modificarDatos.png");
        setIconoEscalado(btnListar, "imagenes/icono_listar.png");

        modelo = new DefaultTableModel();
        Object[] columnas = {"Usuario"};
        modelo.setColumnIdentifiers(columnas);
        table1.setModel(modelo);
    }
    /**
     * Carga y escala un icono desde recursos para un botón.
     *
     * @param boton el {@link JButton} al que se asignará el icono
     * @param ruta  ruta del recurso de imagen dentro del classpath
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
     * Asigna el manejador de internacionalización y actualiza los textos.
     *
     * @param mih instancia de {@link MensajeInternacionalizacionHandler}
     */
    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }
    /**
     * Actualiza todos los textos de la interfaz según el idioma actual.
     */
    private void actualizarTextos() {
        setTitle(mih.get("sesionAdmin.titulo"));
        tituloVentana.setText(mih.get("sesionAdmin.titulo"));
        lblNombre.setText(mih.get("agregarP.txtNombre"));
        btnListar.setText(mih.get("listarP.btnListar"));
        btnBuscar.setText(mih.get("listarP.btnBuscar"));
        btnEliminar.setText(mih.get("eliminarP.btnEliminar"));
        btnModificarContra.setText(mih.get("sesionAdmin.btn.modifContra"));

        DefaultTableModel modelo = (DefaultTableModel) table1.getModel();
        modelo.setColumnIdentifiers(new Object[]{
                mih.get("login.txtUsuario"), mih.get("registrar.txtNombre"),
                mih.get("registrar.txtApellido"), mih.get("registrar.txtFechaN"),
                mih.get("registrar.txtEmail"), mih.get("registrar.txtTelefono")
        });

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }
    /**
     * Carga una lista de usuarios en la tabla, limpiando previamente su contenido.
     *
     * @param usuarios lista de {@link Usuario} a mostrar
     */
    public void cargarUsuarios(List<Usuario> usuarios) {
        modelo.setRowCount(0);
        for (Usuario user:usuarios) {
            modelo.addRow(new Object[]{
                    user.getCedula(), user.getNombre(),
                    user.getApellido(), user.getFechaNacimiento(),
                    user.getEmail(), user.getTelefono()
            });
        }
    }
    /**
     * Muestra un cuadro de diálogo con un mensaje informativo.
     *
     * @param mensaje texto a desplegar en el diálogo
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTable getTable1() {
        return table1;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JButton getBtnModificarContra() {
        return btnModificarContra;
    }

}
