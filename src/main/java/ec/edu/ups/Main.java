package ec.edu.ups;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.*;
import ec.edu.ups.modelo.Rol;

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
                CarritoDAO carritoDAO = new CarritoDAOMemoria();

                LoginView loginView = new LoginView();
                RegistrarUsuarioView registrarUsuarioView = new RegistrarUsuarioView();

                UsuarioController usuarioController = new UsuarioController(usuarioDAO,carritoDAO,loginView,
                                                                            registrarUsuarioView);
                loginView.setVisible(true);

                loginView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e){
                        Usuario usuarioAuntenticado = usuarioController.getUsuarioAutenticado();
                        if(usuarioAuntenticado != null){
                            PrincipalView principalView = usuarioController.getPrincipalView();  // ya tiene listeners

                            CarritoView carritoView = new CarritoView();
                            ListarCarritosView listarCarritosView = new ListarCarritosView();
                            ListarCarritoAdminView listarCAdmin = new ListarCarritoAdminView();

                            ProductoDAO productoDAO = new ProductoDAOMemoria();
                            ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                            ProductoListaView productoListaView = new ProductoListaView();

                            ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, carritoView);
                            CarritoController carritoController = new CarritoController(carritoDAO, usuarioDAO,carritoView, productoDAO, usuarioAuntenticado);

                            EliminarProductoView eliminarProductoView = new EliminarProductoView(productoController);
                            ModificarProductoView modificarProductoView = new ModificarProductoView(productoController);

                            productoController.setModificarProductoView(modificarProductoView);
                            productoController.setEliminarProductoView(eliminarProductoView);
                            carritoController.vincularListarCarritos(listarCarritosView, principalView.getjDesktopPane());
                            carritoController.configurarEventosListarCarritoAdmin(listarCAdmin, principalView.getjDesktopPane());

                            principalView.getjDesktopPane().add(productoAnadirView);
                            principalView.getjDesktopPane().add(productoListaView);
                            principalView.getjDesktopPane().add(eliminarProductoView);
                            principalView.getjDesktopPane().add(modificarProductoView);
                            principalView.getjDesktopPane().add(carritoView);
                            principalView.getjDesktopPane().add(listarCarritosView);
                            principalView.getjDesktopPane().add(listarCAdmin);

                            //ocultar vistas cuando ingresa un usuario
                            if(usuarioAuntenticado.getRol().equals(Rol.USUARIO)) {
                                principalView.deshabilitarMenusAdministrador();
                            } else{
                                principalView.deshabilitarMenusUsuario();
                            }

                            principalView.mostrarMensaje("Bienvenido: " + usuarioAuntenticado.getUsuario());

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
                                    }
                                }
                            });

                            // menu eliminar produc
                            principalView.getMenuItemEliminarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!eliminarProductoView.isVisible()) {
                                        eliminarProductoView.setVisible(true);
                                    }
                                }
                            });

                            // menu modificar producto
                            principalView.getMenuItemActualizarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!modificarProductoView.isVisible()) {
                                        modificarProductoView.setVisible(true);
                                    }
                                }
                            });

                            principalView.getMenuItemCrearCarrito().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!carritoView.isVisible()){
                                        carritoView.setVisible(true);
                                    }
                                }
                            });

                            principalView.getMenuItemListarMisCarritos().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!listarCarritosView.isVisible()){
                                        listarCarritosView.setVisible(true);
                                    }
                                }
                            });

                            principalView.getMenuItemListarCarritosPorUsuario().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if(!listarCAdmin.isVisible()){
                                        listarCAdmin.setVisible(true);
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