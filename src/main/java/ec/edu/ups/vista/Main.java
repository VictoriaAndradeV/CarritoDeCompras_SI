package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                PrincipalView principalView = new PrincipalView();
                ProductoDAO productoDAO = new ProductoDAOMemoria();
                ProductoController productoController = new ProductoController(productoDAO);

                // vistas instanciadas
                ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                ProductoListaView productoListaView = new ProductoListaView();
                EliminarProductoView eliminarProductoView = new EliminarProductoView(productoController);
                ModificarProductoView modificarProductoView = new ModificarProductoView(productoController);

                // Configurar el controlador
                productoController.setProductoAnadirView(productoAnadirView);
                productoController.setProductoListaView(productoListaView);
                productoController.setEliminarProductoView(eliminarProductoView);
                productoController.setModificarProductoView(modificarProductoView);

                // menu crear producto
                principalView.getMenuItemCrearProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!productoAnadirView.isVisible()){
                            productoAnadirView.setVisible(true);
                            principalView.getjDesktopPane().add(productoAnadirView);
                        }
                    }
                });

                // menu buscar producto
                principalView.getMenuItemBuscarProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!productoListaView.isVisible()){
                            productoListaView.setVisible(true);
                            principalView.getjDesktopPane().add(productoListaView);
                        }
                    }
                });

                // menu eliminar produc
                principalView.getMenuItemEliminarProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!eliminarProductoView.isVisible()) {
                            eliminarProductoView.setVisible(true);
                            principalView.getjDesktopPane().add(eliminarProductoView);
                        }
                    }
                });

                // menu modificar producto
                principalView.getMenuItemActualizarProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!modificarProductoView.isVisible()) {
                            modificarProductoView.setVisible(true);
                            principalView.getjDesktopPane().add(modificarProductoView);
                        }
                    }
                });
            }
        });
    }
}