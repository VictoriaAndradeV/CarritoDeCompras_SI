package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.ProductoGestionarView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoControllerGestionar {

    private final ProductoDAO productoDAO;
    private final ProductoGestionarView productoGestionarView;

    public ProductoControllerGestionar(ProductoDAO productoDAO, ProductoGestionarView productoGestionarView) {
        this.productoDAO = productoDAO;
        this.productoGestionarView = productoGestionarView;
        configurarEventos();
    }

    private void configurarEventos() {
        productoGestionarView.getBtnBuscarEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParaEliminar();
            }
        });

        productoGestionarView.getBtnEliminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });

        productoGestionarView.getBtnBuscarModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarParaModificar();
            }
        });

        productoGestionarView.getBtnModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarProducto();
            }
        });
    }

    private void buscarParaEliminar() {
        int codigo = Integer.parseInt(productoGestionarView.getTxtCodigoEliminar().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto != null) {
            productoGestionarView.cargarDatos(List.of(producto));
        } else {
            productoGestionarView.mostrarMensaje("Producto no encontrado.");
        }
    }

    private void eliminarProducto() {
        int codigo = Integer.parseInt(productoGestionarView.getTxtCodigoEliminar().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto != null) {
            productoDAO.eliminar(codigo);
            productoGestionarView.mostrarMensaje("Producto eliminado correctamente.");
            productoGestionarView.limpiarCamposEliminar();
            productoGestionarView.cargarDatos(List.of());
        } else {
            productoGestionarView.mostrarMensaje("Producto no encontrado.");
        }
    }

    private void buscarParaModificar() {
        int codigo = Integer.parseInt(productoGestionarView.getTxtCodigoModificar().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto != null) {
            productoGestionarView.getTxtNuevoNombre().setText(producto.getNombre());
            productoGestionarView.getTxtNuevoPrecio().setText(String.valueOf(producto.getPrecio()));
        } else {
            productoGestionarView.mostrarMensaje("Producto no encontrado.");
        }
    }

    private void modificarProducto() {
        int codigo = Integer.parseInt(productoGestionarView.getTxtCodigoModificar().getText());
        String nuevoNombre = productoGestionarView.getTxtNuevoNombre().getText();
        double nuevoPrecio = Double.parseDouble(productoGestionarView.getTxtNuevoPrecio().getText());

        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto != null) {
            producto.setNombre(nuevoNombre);
            producto.setPrecio(nuevoPrecio);
            productoDAO.actualizar(producto);
            productoGestionarView.mostrarMensaje("Producto modificado correctamente.");
            productoGestionarView.limpiarCamposModificar();
        } else {
            productoGestionarView.mostrarMensaje("Producto no encontrado.");
        }
    }
}

