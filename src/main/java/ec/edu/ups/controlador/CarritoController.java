package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.*;
import ec.edu.ups.util.FormateadorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * Controlador encargado de gestionar la lógica de creación, edición y administración
 * de un carrito de compras para un usuario autenticado.
 * <p>
 * Coordina la interacción entre la vista de carrito ({@link CarritoView}),
 * los DAOs de carrito y usuario, y la gestión de productos.
 * Utiliza internacionalización a través de {@link MensajeInternacionalizacionHandler}
 * para mostrar mensajes traducidos.
 * </p>
 */
public class CarritoController {

    /** DAO para persistencia de carritos. */
    private final CarritoDAO carritoDAO;
    /** DAO para acceder a datos de usuario (no usado directamente aquí). */
    private final UsuarioDAO usuarioDAO;
    /** Vista principal de carrito con tabla e inputs. */
    private final CarritoView carritoView;
    /** DAO para acceder a productos existentes. */
    private final ProductoDAO productoDAO;
    /** Usuario actualmente autenticado que posee el carrito. */
    private final Usuario usuarioActual;
    /** Instancia de carrito en construcción. */
    private Carrito carrito;
    /** Manejador de mensajes internacionalizados. */
    private final MensajeInternacionalizacionHandler mih;

    /**
     * Constructor que inyecta dependencias y configura eventos en la vista.
     *
     * @param mih Handler para mensajes traducidos.
     * @param carritoDAO DAO para operaciones sobre carritos.
     * @param usuarioDAO DAO para operaciones sobre usuarios.
     * @param carritoView Vista de interfaz para el carrito.
     * @param productoDAO DAO para obtener productos.
     * @param usuarioActual Usuario autenticado dueño del carrito.
     */
    public CarritoController(MensajeInternacionalizacionHandler mih, CarritoDAO carritoDAO, UsuarioDAO usuarioDAO, CarritoView carritoView, ProductoDAO productoDAO,
                             Usuario usuarioActual) {
        this.mih = mih;
        this.carritoDAO = carritoDAO;
        this.usuarioDAO = usuarioDAO;
        this.carritoView = carritoView;
        this.productoDAO = productoDAO;
        this.usuarioActual = usuarioActual;
        this.carrito = new Carrito();
        configurarEventosEnVista();
    }
    /**
     * Registra los listeners para los botones de la vista de carrito.
     */
    private void configurarEventosEnVista() {
        carritoView.getBtnAnadir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                anadirProducto();
            }
        });

        carritoView.getGuardarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                guardarCarrito();
            }
        });

        carritoView.getCancelarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarCarrito();
            }
        });

        carritoView.getLimpiarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });

        carritoView.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarItemDelCarrito();
            }
        });

        carritoView.getBtnActualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaModificarCantidad();
            }
        });
    }
    /**
     * Persiste el carrito actual, asigna el usuario y muestra confirmación.
     * Luego reinicia la vista y modelo.
     */
    private void guardarCarrito() {
        //asignar el usuario al carrito
        carrito.setUsuario(usuarioActual);
        carritoDAO.crear(carrito);
        carritoView.mostrarMensaje(mih.get("carrito.mensajeConfirmar"));
        // limpiamos
        DefaultTableModel m = (DefaultTableModel) carritoView.getTable1().getModel();
        m.setRowCount(0);
        carrito = new Carrito();
    }

    /**
     * Añade un producto al carrito según código y cantidad seleccionados.
     * Actualiza tabla y totales.
     */
    private void anadirProducto() {
        int codigo = Integer.parseInt(carritoView.getTxtCodigo().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        int cantidad = Integer.parseInt(carritoView.getComboBox1().getSelectedItem().toString());
        carrito.agregarProducto(producto, cantidad);

        cargarProductos();
        mostrarTotales();
    }
    /**
     * Carga los items del carrito en la tabla, formateando moneda según locale.
     */
    private void cargarProductos() {
        List<ItemCarrito> items = carrito.obtenerItems();
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0);  // limpiamos antes de recargar

        Locale locale = mih.getLocale();

        for (ItemCarrito item : items) {
            double precio = item.getProducto().getPrecio();
            int cantidad = item.getCantidad();
            double subtotal = precio * cantidad;

            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    FormateadorUtils.formatearMoneda(precio, locale),cantidad,
                    FormateadorUtils.formatearMoneda(subtotal, locale)
            });
        }
    }
    /**
     * Calcula y muestra subtotal, IVA y total en la vista.
     */
    private void mostrarTotales() {
        Locale locale = mih.getLocale();
        carritoView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularSubtotal(), locale));
        carritoView.getTxtIVA().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale));
        carritoView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale));
    }
    /**
     * Cancela la construcción del carrito, limpiando modelo y vista.
     */
    private void cancelarCarrito() {
        carrito = new Carrito(); //nuevo carrito vacio
        limpiarCampos();
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0); //Limpia tabla
    }
    /**
     * Limpia los campos de texto y resetea el combobox.
     */
    private void limpiarCampos() {
        carritoView.getTxtCodigo().setText("");
        carritoView.getTxtNombre().setText("");
        carritoView.getTxtPrecio().setText("");
        carritoView.getTxtSubtotal().setText("");
        carritoView.getTxtIVA().setText("");
        carritoView.getTxtTotal().setText("");
        carritoView.getComboBox1().setSelectedIndex(0);
    }
    /**
     * Elimina el item seleccionado de la tabla tras confirmación.
     */
    private void eliminarItemDelCarrito() {
        JTable tabla = carritoView.getTable1();
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(carritoView,
                    mih.get("carrito.mensajeConfirmar.eliminar"),mih.get("carrito.titulo"),
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                int codigo = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                carrito.eliminarItem(codigo);
                actualizarTabla();
                mostrarTotales();
                carritoView.mostrarMensaje(
                        mih.get("carrito.mensaje.Eliminado")
                );
            }
        } else {
            carritoView.mostrarMensaje(mih.get("carrito.mensaje.ItemNOSeleccionado"));
        }
    }
    /**
     * Refresca la tabla con los items actuales del carrito.
     */
    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0); // Limpiar tabla antes de volver a cargar

        List<ItemCarrito> items = carrito.obtenerItems();
        Locale locale = mih.getLocale();

        for (ItemCarrito item : items) {
            double precio = item.getProducto().getPrecio();
            int cantidad = item.getCantidad();
            double subtotal = precio * cantidad;

            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    FormateadorUtils.formatearMoneda(precio, locale),
                    cantidad,
                    FormateadorUtils.formatearMoneda(subtotal, locale)
            });
        }
    }
    /**
     * Muestra diálogo para modificar cantidad de un item y actualiza el carrito.
     */
    private void mostrarVentanaModificarCantidad() {
        JTable tabla = carritoView.getTable1();
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            int codigoProducto = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            //Crear ventana para modificar cantidad del producto
            JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(carritoView),
                                mih.get("carrito.ventanaModificar.titulo"),true);

            dialogo.setSize(250, 150);
            dialogo.setLocationRelativeTo(carritoView);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel label = new JLabel(mih.get("carrito.txtCantidad.modificar"));

            //ComboBox con num del 1 al 20
            JComboBox<Integer> comboCantidad = new JComboBox<>();
            for (int i = 1; i <= 20; i++) {
                comboCantidad.addItem(i);
            }

            JButton btnAceptar = new JButton(mih.get("carrito.modificar.btnActualizar"));

            btnAceptar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int nuevaCantidad = (int) comboCantidad.getSelectedItem();
                    carrito.actualizarCantidad(codigoProducto, nuevaCantidad);
                    actualizarTabla();
                    mostrarTotales();
                    dialogo.dispose(); //cerrar ventana
                    carritoView.mostrarMensaje(mih.get("carrito.mensajeExito.modificar"));
                }
            });
            panel.add(label);
            panel.add(comboCantidad);
            panel.add(Box.createVerticalStrut(10));
            panel.add(btnAceptar);

            dialogo.add(panel);
            dialogo.setVisible(true);
        } else {
            carritoView.mostrarMensaje(mih.get("carrito.mensaje.ItemNOSeleccionado"));
        }
    }

    /**
     * Vincula eventos para listar, ver detalle y eliminar carritos del usuario en la vista de lista.
     */
    public void vincularListarCarritos(ListarCarritosView view, JDesktopPane desktop) {
        configurarEventoListar(view);
        configurarEventoDetalle(view, desktop);
        configurarEventoEliminar(view);
    }
    /**
     * Configura el botón Listar para recuperar carritos del usuario.
     */
    private void configurarEventoListar(ListarCarritosView view) {
        view.getBtnListar().setText(mih.get("listarP.btnListar"));
        view.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getCedula());
                if (lista.isEmpty()) {
                    view.mostrarMensaje(mih.get("listarC.mensajeError.vacio"));
                } else {
                    view.cargarDatos(lista);
                }
            }
        });
    }
    /**
     * Configura el botón Detalle para mostrar items de un carrito seleccionado.
     */
    private void configurarEventoDetalle(ListarCarritosView view, JDesktopPane desktop) {
        view.getBtnDetalle().setText(mih.get("listarC.usuario.detalle"));
        view.getBtnDetalle().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = view.getTable1().getSelectedRow();
                if (fila < 0) {
                    view.mostrarMensaje(mih.get("listarC.mensajeSelecc"));
                    return;
                }
                int codigo = (int) view.getTable1().getValueAt(fila, 0);
                Carrito c = carritoDAO.buscarPorCodigo(codigo);
                if (c == null) {
                    view.mostrarMensaje(mih.get("listarC.mensajeError.noEnc"));
                    return;
                }
                DetalleCarritoUserView detView = new DetalleCarritoUserView();
                detView.setMensajeHandler(mih);
                desktop.add(detView);
                detView.setVisible(true);
                detView.cargarDatos(c.obtenerItems());
                vincularDetalle(detView, c);
            }
        });
    }
    /**
     * Configura el botón Eliminar para borrar un carrito seleccionado.
     */
    private void configurarEventoEliminar(ListarCarritosView view) {
        view.getBtnEliminar().setText(mih.get("eliminarP.btnEliminar"));
        view.getBtnEliminar().addActionListener(evt -> {
            int fila = view.getTable1().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje(mih.get("listarC.mensajeEliminar"));
                return;
            }
            int codigo = (int) view.getTable1().getValueAt(fila, 0);
            carritoDAO.eliminar(codigo);
            view.mostrarMensaje(mih.get("listarC.mensajeExito"));
            //actualiza  la liusta
            List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getCedula());
            view.cargarDatos(lista);
        });
    }
    /**
     * Vincula modificar y eliminar items en la vista de detalle de carrito.
     */
    public void vincularDetalle(DetalleCarritoUserView view, Carrito carrito) {
        configurarEventoModificarDetalle(view, carrito);
        configurarEventoEliminarDetalle(view, carrito);
    }
    /** Modificar cantidad de un item seleccionado. */
    private void configurarEventoModificarDetalle(DetalleCarritoUserView view, Carrito carrito) {
        view.getBtnModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = view.getTablaDetalles().getSelectedRow();
                if (fila < 0) {
                    view.mostrarMensaje(mih.get("detalleC.mensaje.Selecc"));
                    return;
                }
                int codigoProd = obtenerCodigoDeDetalle(view, fila);
                String prompt = mih.get("carrito.txtCantidad.modificar");
                String input = JOptionPane.showInputDialog(view, prompt);
                if (input != null) {
                    procesarNuevaCantidad(input, carrito, view);
                }
            }
        });
    }
    /** Eliminar un item desde la vista de detalle. */
    private void configurarEventoEliminarDetalle(DetalleCarritoUserView view, Carrito carrito) {
        view.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = view.getTablaDetalles().getSelectedRow();
                if (fila < 0) {
                    view.mostrarMensaje(mih.get("detalleC.mensaje.Selecc"));
                    return;
                }
                int codigoProd = obtenerCodigoDeDetalle(view, fila);
                carrito.eliminarItem(codigoProd);
                carritoDAO.limpiar(carrito);
                view.cargarDatos(carrito.obtenerItems());
            }
        });
    }
    /** Extrae el código de producto de una fila en detalle. */
    private int obtenerCodigoDeDetalle(DetalleCarritoUserView view, int fila) {
        return (int) view.getTablaDetalles().getValueAt(fila, 0);
    }
    /** Procesa y valida la nueva cantidad ingresada. */
    private void procesarNuevaCantidad(String cantidadColocada, Carrito carrito, DetalleCarritoUserView view) {
        try {
            int nuevaCant = Integer.parseInt(cantidadColocada);
            carrito.actualizarCantidad(obtenerCodigoDeDetalle(view, view.getTablaDetalles().getSelectedRow()), nuevaCant);
            carritoDAO.limpiar(carrito);
            view.cargarDatos(carrito.obtenerItems());
        } catch (NumberFormatException ex) {
            view.mostrarMensaje(mih.get("detalleC.mensajeError.cantidad"));
        }
    }

    /**
     * Configura eventos para listar carritos de cualquier usuario en modo administrador.
     */
    public void configurarEventosListarCarritoAdmin(ListarCarritoAdminView view,JDesktopPane desktop) {
        cargarUsuariosInicial(view);
        configurarEventoBuscarUsuario(view);
        configurarEventoListarCarrito(view, desktop);
    }
    /** Carga inicialmente todos los usuarios en la vista de admin. */
    private void cargarUsuariosInicial(ListarCarritoAdminView view) {
        List<Usuario> lista = usuarioDAO.listarTodos();
        List<String> nombres = new ArrayList<>();
        for (Usuario u : lista) {
            nombres.add(u.getCedula());
        }
        view.cargarUsuarios(nombres);
    }
    /** Configura la búsqueda de usuarios en vista admin. */
    private void configurarEventoBuscarUsuario(ListarCarritoAdminView view) {
        view.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = view.getTxtNombre().getText().trim();
                if (nombre.isEmpty()) {
                    view.mostrarMensaje(mih.get("listarU.admin.mensajeError.vacio"));
                    return;
                }
                Usuario encontrado = usuarioDAO.buscarPorUsername(nombre);
                if (encontrado == null) {
                    view.mostrarMensaje(mih.get("listarU.admin.mensajeError.usuario"));
                    view.cargarUsuarios(new ArrayList<>());
                } else {
                    List<String> uno = new ArrayList<>();
                    uno.add(encontrado.getCedula());
                    view.cargarUsuarios(uno);
                }
            }
        });
    }
    /**
     * Configura el listado de carritos de un usuario seleccionado en modo admin.
     */
    private void configurarEventoListarCarrito(ListarCarritoAdminView view, JDesktopPane desktop) {
        view.getBtnCarrito().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int fila = view.getTable1().getSelectedRow();
                if (fila < 0) {
                    view.mostrarMensaje(mih.get("listarU.admin.mensajeSelecc"));
                    return;
                }
                String username = view.getTable1().getValueAt(fila, 0).toString();
                List<Carrito> carritos = carritoDAO.listarPorUsuario(username);
                if (carritos.isEmpty()) {
                    view.mostrarMensaje(mih.get("listarU.admin.mensajeError.noCarrito"));
                    return;
                }
                //Se crea la ventana de listar carritos
                ListaCarriADMIN listaCarriAdmin = new ListaCarriADMIN();
                listaCarriAdmin.setMensajeHandler(mih);
                desktop.add(listaCarriAdmin);
                listaCarriAdmin.cargarDatos(carritos);
                listaCarriAdmin.setVisible(true);
            }

        });
    }
}


