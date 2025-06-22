package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.CarritoView;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final CarritoView carritoView;
    private final ProductoDAO productoDAO;
    private Carrito carrito;

    public CarritoController(CarritoDAO carritoDAO, CarritoView carritoView, ProductoDAO productoDAO) {
        this.carritoDAO = carritoDAO;
        this.carritoView = carritoView;
        this.productoDAO = productoDAO;
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
    }

    private void guardarCarrito() {
        carritoDAO.crear(carrito);
        carritoView.mostrarMensaje("Carrito creado correctamente");
        System.out.println(carritoDAO.listarTodos());
        limpiarCampos();

        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0);
        carrito = new Carrito(); //creamos un nuevo carrito al presionar guardar
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

    private void buscarProducto() {
        String codTxt = carritoView.getTxtCodigo().getText().trim();
        if (!codTxt.matches("\\d+")) {
            carritoView.mostrarMensaje("Código NO valido");
            return;
        }

        int codigo = Integer.parseInt(codTxt);
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto != null) {
            carritoView.getTxtNombre().setText(producto.getNombre());
            carritoView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
        } else {
            carritoView.mostrarMensaje("Producto no encontrado");
        }
    }

    private void cancelarCarrito() {
        carrito = new Carrito(); // nuevo carrito vacio
        limpiarCampos();
        DefaultTableModel modelo = (DefaultTableModel) carritoView.getTable1().getModel();
        modelo.setRowCount(0); // Limpia tabla
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

    public List<ItemCarrito> obtenerItemsDelCarrito() {
        return carrito.obtenerItems();
    }

}
