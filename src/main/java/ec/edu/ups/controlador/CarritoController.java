package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final UsuarioDAO usuarioDAO;
    private final CarritoView carritoView;
    private final ProductoDAO productoDAO;
    private final Usuario usuarioActual;
    private Carrito carrito;

    public CarritoController(CarritoDAO carritoDAO, UsuarioDAO usuarioDAO, CarritoView carritoView, ProductoDAO productoDAO,
                             Usuario usuarioActual) {
        this.carritoDAO = carritoDAO;
        this.usuarioDAO = usuarioDAO;
        this.carritoView = carritoView;
        this.productoDAO = productoDAO;
        this.usuarioActual = usuarioActual;
        this.carrito = new Carrito();
        configurarEventosEnVista();
    }

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

    private void guardarCarrito() {
        //asignar el usuario al carrito
        carrito.setUsuario(usuarioActual);
        carritoDAO.crear(carrito);
        carritoView.mostrarMensaje("Carrito creado correctamente");
        // limpiamos
        DefaultTableModel m =
                (DefaultTableModel) carritoView.getTable1().getModel();
        m.setRowCount(0);
        carrito = new Carrito();
    }

    //se agregan los productos y su cantidad
    private void anadirProducto() {
        int codigo = Integer.parseInt(carritoView.getTxtCodigo().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        int cantidad = Integer.parseInt(carritoView.getComboBox1().getSelectedItem().toString());
        carrito.agregarProducto(producto, cantidad);

        cargarProductos();
        mostrarTotales();
    }

    private void cargarProductos() {
        List<ItemCarrito> items = carrito.obtenerItems();
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        //vamos a ir agregando a la tabla de carritoView
        for (ItemCarrito item : items) {
            modelo.addRow(new Object[]{item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    item.getProducto().getPrecio() * item.getCantidad()});
        }
    }

    private void mostrarTotales(){
        String subtotal = String.valueOf(carrito.calcularSubtotal());
        String iva = String.valueOf(carrito.calcularIVA());
        String total = String.valueOf(carrito.calcularTotal());

        carritoView.getTxtSubtotal().setText(subtotal);
        carritoView.getTxtIVA().setText(iva);
        carritoView.getTxtTotal().setText(total);
    }

    private void cancelarCarrito() {
        carrito = new Carrito(); //nuevo carrito vacio
        limpiarCampos();
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0); //Limpia tabla
    }

    private void limpiarCampos() {
        carritoView.getTxtCodigo().setText("");
        carritoView.getTxtNombre().setText("");
        carritoView.getTxtPrecio().setText("");
        carritoView.getTxtSubtotal().setText("");
        carritoView.getTxtIVA().setText("");
        carritoView.getTxtTotal().setText("");
        carritoView.getComboBox1().setSelectedIndex(0);
    }

    private void eliminarItemDelCarrito() {
        JTable tabla = carritoView.getTable1();
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            int confirmacion = JOptionPane.showConfirmDialog(
                    carritoView,
                    "¿Esta seguro de eliminar el item seleccionado?",
                    "Confirmar acción",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                int codigo = Integer.parseInt(tabla.getValueAt(fila, 0).toString());
                carrito.eliminarItem(codigo); // Metodo del modelo Carrito que elimina por código de producto
                actualizarTabla();            // Refresca la tabla
                mostrarTotales();            // Refresca los totales
                carritoView.mostrarMensaje("Item eliminado correctamente");
            }
        } else {
            carritoView.mostrarMensaje("Selecciona un item para eliminar");
        }
    }

    private void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0); //Limpiar tabla antes de volver a cargar

        List<ItemCarrito> items = carrito.obtenerItems();
        for (ItemCarrito item : items) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    item.getCantidad() * item.getProducto().getPrecio()
            });
        }
    }

    private void mostrarVentanaModificarCantidad() {
        JTable tabla = carritoView.getTable1();
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            int codigoProducto = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            // Crear ventana para modificar cantidad del producto
            JDialog dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(carritoView), "Modificar cantidad", true);
            dialogo.setSize(250, 150);
            dialogo.setLocationRelativeTo(carritoView);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel label = new JLabel("Nueva cantidad:");

            //ComboBox con num del 1 al 20
            JComboBox<Integer> comboCantidad = new JComboBox<>();
            for (int i = 1; i <= 20; i++) {
                comboCantidad.addItem(i);
            }

            JButton btnAceptar = new JButton("Actualizar");
            btnAceptar.addActionListener(ev -> {
                int nuevaCantidad = (int) comboCantidad.getSelectedItem();
                carrito.actualizarCantidad(codigoProducto, nuevaCantidad);
                actualizarTabla();
                mostrarTotales();
                dialogo.dispose(); //cerrar ventana
                carritoView.mostrarMensaje("Cantidad actualizada correctamente");
            });
            panel.add(label);
            panel.add(comboCantidad);
            panel.add(Box.createVerticalStrut(10));
            panel.add(btnAceptar);

            dialogo.add(panel);
            dialogo.setVisible(true);
        } else {
            carritoView.mostrarMensaje("Seleccione el item que desee modificar");
        }
    }
    //listar carritos del usuario, esto no hace admin
    public void vincularListarCarritos(ListarCarritosView view, JDesktopPane desktop) {
        configurarEventoListar(view);
        configurarEventoDetalle(view, desktop);
        configurarEventoEliminar(view);
    }

    private void configurarEventoListar(ListarCarritosView view) {
        view.getBtnListar().addActionListener(evt -> {
            List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getUsuario());
            if (lista.isEmpty()) {
                view.mostrarMensaje("Aún no ha creado ningún carrito");
            } else {
                view.cargarDatos(lista);
            }
        });
    }

    private void configurarEventoDetalle(ListarCarritosView view, JDesktopPane desktop) {
        view.getBtnDetalle().addActionListener(evt -> {
            int fila = view.getTable1().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un carrito");
                return;
            }
            int codigo = (int) view.getTable1().getValueAt(fila, 0);
            Carrito c = carritoDAO.buscarPorCodigo(codigo);
            if (c == null) {
                view.mostrarMensaje("Carrito no encontrado");
                return;
            }
            DetalleCarritoUserView detView = new DetalleCarritoUserView();
            desktop.add(detView);
            detView.setVisible(true);
            detView.cargarDatos(c.obtenerItems());
            vincularDetalle(detView, c);
        });
    }

    private void configurarEventoEliminar(ListarCarritosView view) {
        view.getBtnEliminar().addActionListener(evt -> {
            int fila = view.getTable1().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Selecciona un carrito para eliminar");
                return;
            }
            int codigo = (int) view.getTable1().getValueAt(fila, 0);
            carritoDAO.eliminar(codigo);
            view.mostrarMensaje("Carrito eliminado correctamente");
            //actualiza  la liusta
            List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getUsuario());
            view.cargarDatos(lista);
        });
    }

    public void vincularDetalle(DetalleCarritoUserView view, Carrito carrito) {
        configurarEventoModificarDetalle(view, carrito);
        configurarEventoEliminarDetalle(view, carrito);
    }

    private void configurarEventoModificarDetalle(DetalleCarritoUserView view, Carrito carrito) {
        view.getBtnModificar().addActionListener(evt -> {
            int fila = view.getTablaDetalles().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un producto");
                return;
            }
            int codigoProd = obtenerCodigoDeDetalle(view, fila);
            String input = JOptionPane.showInputDialog(view, "Nueva cantidad:");
            if (input != null) {
                procesarNuevaCantidad(input, carrito, view);
            }
        });
    }

    private void configurarEventoEliminarDetalle(DetalleCarritoUserView view, Carrito carrito) {
        view.getBtnEliminar().addActionListener(evt -> {
            int fila = view.getTablaDetalles().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un producto");
                return;
            }
            int codigoProd = obtenerCodigoDeDetalle(view, fila);
            carrito.eliminarItem(codigoProd);
            carritoDAO.limpiar(carrito);
            view.cargarDatos(carrito.obtenerItems());
        });
    }

    private int obtenerCodigoDeDetalle(DetalleCarritoUserView view, int fila) {
        return (int) view.getTablaDetalles().getValueAt(fila, 0);
    }

    private void procesarNuevaCantidad(String cantidadColocada, Carrito carrito, DetalleCarritoUserView view) {
        try {
            int nuevaCant = Integer.parseInt(cantidadColocada);
            carrito.actualizarCantidad(obtenerCodigoDeDetalle(view, view.getTablaDetalles().getSelectedRow()), nuevaCant);
            carritoDAO.limpiar(carrito);
            view.cargarDatos(carrito.obtenerItems());
        } catch (NumberFormatException ex) {
            view.mostrarMensaje("Cantidad inválida");
        }
    }

    //CARRITOS DEL ADMINISTRADOR
    public void configurarEventosListarCarritoAdmin(ListarCarritoAdminView view,JDesktopPane desktop) {
        cargarUsuariosInicial(view);
        configurarEventoBuscarUsuario(view);
        configurarEventoListarCarrito(view, desktop);
    }

    private void cargarUsuariosInicial(ListarCarritoAdminView view) {
        List<Usuario> lista = usuarioDAO.listarTodos();
        List<String> nombres = new ArrayList<>();
        for (Usuario u : lista) {
            nombres.add(u.getUsuario());
        }
        view.cargarUsuarios(nombres);
    }

    private void configurarEventoBuscarUsuario(ListarCarritoAdminView view) {
        view.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = view.getTxtNombre().getText().trim();
                if (nombre.isEmpty()) {
                    view.mostrarMensaje("Por favor ingrese un nombre de usuario");
                    return;
                }
                Usuario encontrado = usuarioDAO.buscarPorUsername(nombre);
                if (encontrado == null) {
                    view.mostrarMensaje("No existe el usuario buscado");
                    view.cargarUsuarios(new ArrayList<String>());
                } else {
                    List<String> uno = new ArrayList<>();
                    uno.add(encontrado.getUsuario());
                    view.cargarUsuarios(uno);
                }
            }
        });
    }

    private void configurarEventoListarCarrito(ListarCarritoAdminView view, JDesktopPane desktop) {
        view.getBtnCarrito().addActionListener(e -> {
            int fila = view.getTable1().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un usuario");
                return;
            }
            String username = view.getTable1().getValueAt(fila, 0).toString();
            List<Carrito> carritos = carritoDAO.listarPorUsuario(username);
            if (carritos.isEmpty()) {
                view.mostrarMensaje("Este usuario no tiene carritos");
                return;
            }
            //Se crea la ventana de listar carritos
            ListaCarriADMIN listaCarriAdmin = new ListaCarriADMIN();
            desktop.add(listaCarriAdmin);
            listaCarriAdmin.cargarDatos(carritos);
            listaCarriAdmin.setVisible(true);
        });
    }

}


