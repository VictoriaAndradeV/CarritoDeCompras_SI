package ec.edu.ups;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.*;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                //iniciar sesion
                UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();
                LoginView loginView = new LoginView();
                loginView.setVisible(true);

                UsuarioController usuarioController = new UsuarioController(usuarioDAO, loginView);

                loginView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e){
                        Usuario usuarioAuntenticado = usuarioController.getUsuarioAutenticado();
                        if(usuarioAuntenticado != null){
                            PrincipalView principalView = new PrincipalView();
                            CarritoView carritoView = new CarritoView();

                            ProductoDAO productoDAO = new ProductoDAOMemoria();
                            CarritoDAOMemoria carritoDAO = new CarritoDAOMemoria();
                            ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                            ProductoListaView productoListaView = new ProductoListaView();

                            ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, carritoView);
                            CarritoController carritoController = new CarritoController(carritoDAO,carritoView, productoDAO);

                            EliminarProductoView eliminarProductoView = new EliminarProductoView(productoController);
                            ModificarProductoView modificarProductoView = new ModificarProductoView(productoController);

                            productoController.setModificarProductoView(modificarProductoView);
                            productoController.setEliminarProductoView(eliminarProductoView);

                            principalView.getjDesktopPane().add(productoAnadirView);
                            principalView.getjDesktopPane().add(productoListaView);
                            principalView.getjDesktopPane().add(eliminarProductoView);
                            principalView.getjDesktopPane().add(modificarProductoView);
                            principalView.getjDesktopPane().add(carritoView);


                            // menu crear producto
                            principalView.getMenuItemCrearProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!productoAnadirView.isVisible()){
                                        productoAnadirView.setVisible(true);
                                        //principalView.getjDesktopPane().add(productoAnadirView);
                                    }
                                }
                            });

                            // menu buscar producto
                            principalView.getMenuItemBuscarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!productoListaView.isVisible()){
                                        productoListaView.setVisible(true);
                                        //principalView.getjDesktopPane().add(productoListaView);
                                    }
                                }
                            });

                            // menu eliminar produc
                            principalView.getMenuItemEliminarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!eliminarProductoView.isVisible()) {
                                        eliminarProductoView.setVisible(true);
                                        //principalView.getjDesktopPane().add(eliminarProductoView);
                                    }
                                }
                            });

                            // menu modificar producto
                            principalView.getMenuItemActualizarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!modificarProductoView.isVisible()) {
                                        modificarProductoView.setVisible(true);
                                        //principalView.getjDesktopPane().add(modificarProductoView);
                                    }
                                }
                            });

                            principalView.getMenuItemCrearCarrito().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!carritoView.isVisible()){
                                        carritoView.setVisible(true);
                                        //principalView.getjDesktopPane().add(carritoView);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}