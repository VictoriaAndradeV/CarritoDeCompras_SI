package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.CarritoView;
import ec.edu.ups.vista.DetallesCarritoUserView;
import ec.edu.ups.vista.ListarCarritosView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final CarritoView carritoView;
    private final ProductoDAO productoDAO;
    private final Usuario usuarioActual;
    private Carrito carrito;

    public CarritoController(CarritoDAO carritoDAO, CarritoView carritoView, ProductoDAO productoDAO,
                             Usuario usuarioActual) {
        this.carritoDAO = carritoDAO;
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
                carrito.actualizarCantidad(codigoProducto, nuevaCantidad); // método en modelo
                actualizarTabla();  //refrescar tabla
                mostrarTotales();
                dialogo.dispose(); //cerrar ventana
                carritoView.mostrarMensaje("Cantidad actualizada correctamente");
            });

            panel.add(label);
            panel.add(comboCantidad);
            panel.add(Box.createVerticalStrut(10)); // Espacio
            panel.add(btnAceptar);

            dialogo.add(panel);
            dialogo.setVisible(true);
        } else {
            carritoView.mostrarMensaje("Seleccione el item que desee modificar");
        }
    }

    public void vincularListarCarritos(ListarCarritosView view, JDesktopPane desktop) {
        // ya existente listener de Listar
        view.getBtnListar().addActionListener(evt -> {
            List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getUsuario());
            if (lista.isEmpty()) {
                view.mostrarMensaje("Aún no ha creado ningún carrito");
            } else {
                view.cargarDatos(lista);
            }
        });
        // nuevo listener de Detalle
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
            DetallesCarritoUserView detView = new DetallesCarritoUserView();
            desktop.add(detView);
            detView.setVisible(true);
            detView.cargarDatos(c.obtenerItems());
            vincularDetalle(detView, c);
        });
        view.getBtnEliminar().addActionListener(evt -> {
            int fila = view.getTable1().getSelectedRow();
            if (fila < 0) { //cuando no seleccionamos una fila de la tabla y aplastamos eliminar
                view.mostrarMensaje("Selecciona un carrito para eliminar");
                return;
            }
            //tomamos el codigo de la fila seleccionada para eliminar el carrito completo
            int codigo = (int) view.getTable1().getValueAt(fila, 0);
            carritoDAO.eliminar(codigo);
            view.mostrarMensaje("Carrito eliminado correctamente");
            //actualizamos la lista
            List<Carrito> lista = carritoDAO.listarPorUsuario(usuarioActual.getUsuario());
            view.cargarDatos(lista);
        });
    }

    private void vincularDetalle(DetallesCarritoUserView view, Carrito carrito) {
        //funcionamiento boton modificar del detalle de la solicitud
        view.getBtnModificar().addActionListener(evt -> {
            int fila = view.getTablaDetalles().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un producto");
                return;
            }
            int codigoProd = (int) view.getTablaDetalles().getValueAt(fila, 0);
            String input = JOptionPane.showInputDialog(view, "Nueva cantidad:");
            if (input != null) {
                try {
                    int nuevaCant = Integer.parseInt(input);
                    carrito.actualizarCantidad(codigoProd, nuevaCant);
                    carritoDAO.limpiar(carrito);
                    view.cargarDatos(carrito.obtenerItems());
                } catch (NumberFormatException ex) {
                    view.mostrarMensaje("Cantidad inválida");
                }
            }
        });

        //funcionamiento del boton eliminar
        view.getBtnEliminar().addActionListener(evt -> {
            int fila = view.getTablaDetalles().getSelectedRow();
            if (fila < 0) {
                view.mostrarMensaje("Seleccione un producto");
                return;
            }
            int codigoProd = (int) view.getTablaDetalles().getValueAt(fila, 0);
            carrito.eliminarItem(codigoProd);
            carritoDAO.limpiar(carrito);
            view.cargarDatos(carrito.obtenerItems());
        });
    }
}


