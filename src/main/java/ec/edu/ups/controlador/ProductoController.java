package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.EliminarProductoView;
import ec.edu.ups.vista.ModificarProductoView;
import ec.edu.ups.vista.ProductoAnadirView;
import ec.edu.ups.vista.ProductoListaView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoController {

    private final ProductoDAO productoDAO;
    private ProductoAnadirView productoAnadirView;
    private ProductoListaView productoListaView;
    private EliminarProductoView eliminarProductoView;
    private ModificarProductoView modificarProductoView;

    public ProductoController(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    //SETTERS de vistas -

    public void setProductoAnadirView(ProductoAnadirView productoAnadirView) {
        this.productoAnadirView = productoAnadirView;
        configurarEventosAnadir();
    }

    public void setProductoListaView(ProductoListaView productoListaView) {
        this.productoListaView = productoListaView;
        configurarEventosLista();
    }

    public void setEliminarProductoView(EliminarProductoView eliminarProductoView) {
        this.eliminarProductoView = eliminarProductoView;
        configurarEventosEliminar();
    }

    public void setModificarProductoView(ModificarProductoView modificarProductoView) {
        this.modificarProductoView = modificarProductoView;
        configurarEventosModificar();
    }

    public ProductoAnadirView getProductoAnadirView() {
        return productoAnadirView;
    }

    public ProductoListaView getProductoListaView() {
        return productoListaView;
    }

    public EliminarProductoView getEliminarProductoView() {
        return eliminarProductoView;
    }

    public ModificarProductoView getModificarProductoView() {
        return modificarProductoView;
    }

    //  EVENTOS ProductoAnadirView

    private void configurarEventosAnadir() {
        productoAnadirView.getBtnAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        productoAnadirView.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productoAnadirView.limpiarCampos();
            }
        });
    }

    private void guardarProducto() {
        int codigo = Integer.parseInt(productoAnadirView.getCampoCodigo().getText());
        String nombre = productoAnadirView.getCampoNombre().getText();
        double precio = Double.parseDouble(productoAnadirView.getCampoPrecio().getText());

        productoDAO.crear(new Producto(codigo, nombre, precio));
        productoAnadirView.mostrarMensaje("Producto guardado correctamente");
        productoAnadirView.limpiarCampos();
        productoAnadirView.mostrarProductos(productoDAO.listarTodos());
    }

    // EVENTOS ProductoListaView

    private void configurarEventosLista() {
        productoListaView.getBtnBuscar().addActionListener(e -> buscarProducto());
        productoListaView.getBtnListar().addActionListener(e -> listarProductos());
    }

    private void buscarProducto() {
        String nombre = productoListaView.getTxtBuscar().getText();
        List<Producto> productos = productoDAO.buscarPorNombre(nombre);
        productoListaView.cargarDatos(productos);
    }

    private void listarProductos() {
        List<Producto> productos = productoDAO.listarTodos();
        productoListaView.cargarDatos(productos);
    }

    // EVENTOS EliminarProductoView
    private void configurarEventosEliminar() {
        eliminarProductoView.getBuscarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorNombreParaEliminar();
            }
        });

        eliminarProductoView.getEliminarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarYEliminarProducto();
            }
        });
    }

    private void buscarProductoPorNombreParaEliminar() {
        String nombre = eliminarProductoView.getCampoNombre().getText().trim();

        if (nombre.isEmpty()) {
            eliminarProductoView.mostrarMensaje("Ingrese el nombre del producto por buscar");
            return;
        }

        List<Producto> productos = productoDAO.buscarPorNombre(nombre);

        if (productos.isEmpty()) {
            eliminarProductoView.mostrarMensaje("No se encontraron productos con ese nombre");
        }

        eliminarProductoView.cargarTabla(productos);
    }

    // ventana para confirmar si se desea eliminar el producto
    private void confirmarYEliminarProducto() {
        String codTxt = eliminarProductoView.getTxtCodigoEliminar().getText().trim();

        if (!codTxt.matches("\\d+")) {
            eliminarProductoView.mostrarMensaje("Código inválido");
            return;
        }

        int codigo = Integer.parseInt(codTxt);

        int opcion = JOptionPane.showConfirmDialog(
                eliminarProductoView,
                "¿Seguro de eliminar el producto con código " + codigo + "?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = eliminarProducto(codigo);
            if (eliminado) {
                eliminarProductoView.removerFila(codigo);
                eliminarProductoView.mostrarMensaje("Producto eliminado correctamente");
            } else {
                eliminarProductoView.mostrarMensaje("Producto NO encontrado");
            }
        }
    }

    // Metodo de eliminación directa desde DAO
    private boolean eliminarProducto(int codigo) {
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto != null) {
            productoDAO.eliminar(codigo);
            return true;
        }
        return false;
    }


    //  EVENTOS ModificarProductoView

    // EVENTOS ModificarProductoView
    private void configurarEventosModificar() {
        // Botón "Buscar"
        modificarProductoView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorNombreParaModificar();
            }
        });

        // Botón "Modificar"
        modificarProductoView.getBtnModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarYModificarProducto();
            }
        });
    }

    // Metodo para buscar productos por nombre y mostrarlos en la tabla
    private void buscarProductoPorNombreParaModificar() {
        String nombre = modificarProductoView.getTxtNombreBuscar().getText().trim();

        if (nombre.isEmpty()) {
            modificarProductoView.mostrarMensaje("Ingrese el nombre del producto por buscar");
            return;
        }

        List<Producto> productos = productoDAO.buscarPorNombre(nombre);

        if (productos.isEmpty()) {
            modificarProductoView.mostrarMensaje("No se encontraron productos con ese nombre");
        }

        modificarProductoView.cargarTabla(productos);
    }

    // Método para confirmar y realizar la modificación del producto
    private void confirmarYModificarProducto() {
        String nombreBuscar = modificarProductoView.getTxtNombreBuscar().getText().trim();
        String nuevoNombre = modificarProductoView.getTxtNombre().getText().trim();
        String nuevoPrecioTexto = modificarProductoView.getTxtPrecio().getText().trim();

        if (nombreBuscar.isEmpty() || nuevoNombre.isEmpty() || nuevoPrecioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(modificarProductoView, "Ingrese los datos del producto por modificar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nuevoPrecioTexto.matches("\\d+(\\.\\d+)?")) {
            JOptionPane.showMessageDialog(modificarProductoView, "Precio no válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                modificarProductoView,
                "¿Seguro de modificar el producto '" + nombreBuscar + "'?",
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            double nuevoPrecio = Double.parseDouble(nuevoPrecioTexto);
            boolean modificado = modificarProductoPorNombre(nombreBuscar, nuevoNombre, nuevoPrecio);

            if (modificado) {
                JOptionPane.showMessageDialog(modificarProductoView, "Producto modificado correctamente");
                limpiarCamposModificar();
            } else {
                JOptionPane.showMessageDialog(modificarProductoView, "Producto a modificar no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método auxiliar para limpiar los campos del formulario de modificación
    private void limpiarCamposModificar() {
        modificarProductoView.getTxtNombreBuscar().setText("");
        modificarProductoView.getTxtNombre().setText("");
        modificarProductoView.getTxtPrecio().setText("");
        modificarProductoView.getModelo().setRowCount(0);
    }

    // Método que realiza la modificación real en el DAO
    private boolean modificarProductoPorNombre(String nombreOriginal, String nuevoNombre, double nuevoPrecio) {
        List<Producto> productos = productoDAO.buscarPorNombre(nombreOriginal);
        if (!productos.isEmpty()) {
            Producto p = productos.get(0); // toma el primero encontrado
            p.setNombre(nuevoNombre);
            p.setPrecio(nuevoPrecio);
            productoDAO.actualizar(p);
            return true;
        }
        return false;
    }


    // ------------------- Utilidad pública -------------------

    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }
}
