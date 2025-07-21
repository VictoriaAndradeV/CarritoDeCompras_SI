package ec.edu.ups;

import ec.edu.ups.controlador.MemoriaController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.dao.impl.*;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.*;
import ec.edu.ups.vista.*;
import ec.edu.ups.modelo.Rol;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                //iniciar sesion
                MemoriaController memoriaController = new MemoriaController();

                UsuarioDAO usuarioDAO;
                CarritoDAO carritoDAO;
                ProductoDAO productoDAO;

                switch (memoriaController.getMemoria()) {
                    case MANEJO_ARCHIVO_TEXTO:
                        usuarioDAO   = new UsuarioDAOArchivoTexto(memoriaController.getRuta());
                        carritoDAO   = new CarritoDAOArchivoTexto(memoriaController.getRuta());
                        productoDAO  = new ProductoDAOArchivoTexto(memoriaController.getRuta());
                        break;
                    case MANEJO_ARCHIVO_BINARIO:
                        usuarioDAO   = new UsuarioDAOArchivoBinario(memoriaController.getRuta());
                        carritoDAO   = new CarritoDAOArchivoBinario(memoriaController.getRuta());
                        productoDAO  = new ProductoDAOArchivoBinario(memoriaController.getRuta());
                        break;
                    default:
                        usuarioDAO   = new UsuarioDAOMemoria();
                        carritoDAO   = new CarritoDAOMemoria();
                        productoDAO  = new ProductoDAOMemoria();
                        break;
                }

                String adminCedula = "0107242869";
                try {
                    if (usuarioDAO.buscarPorUsername(adminCedula) == null) {
                        Usuario admin = new Usuario(
                                adminCedula,
                                "Arbol*12",
                                Rol.ADMINISTRADOR,
                                "Victoria",
                                "Andrade",
                                new Date(2004, 10, 13),
                                "victoria@gmail.com",
                                "0962301221"
                        );
                        usuarioDAO.crear(admin);
                    }
                } catch (ExcepcionCedula | ExcepcionContrasenia |
                         ExcepcionNomApe | ExcepcionCorreo |
                         ExcepcionTelefono e) {
                    // aquí puedes hacer log o re‑lanzar, según convenga
                    e.printStackTrace();
                }

                LoginView loginView = new LoginView();
                RegistrarUsuarioView registrarUsuarioView = new RegistrarUsuarioView();

                registrarUsuarioView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e){
                        loginView.setVisible(true);
                    }
                });

                UsuarioController usuarioController = new UsuarioController(usuarioDAO, carritoDAO, loginView, registrarUsuarioView,  memoriaController.getMemoria(), memoriaController.getRuta());

                loginView.setVisible(true);

                loginView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {

                        Usuario usuarioAuntenticado = usuarioController.getUsuarioAutenticado();

                        if (usuarioAuntenticado != null) {

                            PrincipalView principalView = usuarioController.getPrincipalView();  //ya tiene listeners

                            //Recupera el handler ya inicializado despues del login
                            MensajeInternacionalizacionHandler mih = usuarioController.getMih();

                            CarritoView carritoView = new CarritoView();
                            carritoView.setMensajeHandler(mih);
                            principalView.getjDesktopPane().add(carritoView);

                            ListarCarritosView listarCarritosView = new ListarCarritosView();
                            listarCarritosView.setMensajeHandler(mih);
                            principalView.getjDesktopPane().add(listarCarritosView);

                            ListarCarritoAdminView listarCAdmin = new ListarCarritoAdminView();
                            listarCAdmin.setMensajeHandler(mih);
                            principalView.getjDesktopPane().add(listarCAdmin);

                            //vista de registrar productos con el mih
                            ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                            productoAnadirView.setMensajeHandler(mih);
                            principalView.getjDesktopPane().add(productoAnadirView);

                            //vista de listar productos
                            ProductoListaView productoListaView = new ProductoListaView();
                            productoListaView.setMensajeHandler(mih);
                            principalView.getjDesktopPane().add(productoListaView);

                            ProductoController productoController = new ProductoController(mih, productoDAO, productoAnadirView, productoListaView, carritoView);
                            CarritoController carritoController = new CarritoController(mih, carritoDAO, usuarioDAO, carritoView, productoDAO, usuarioAuntenticado);

                            EliminarProductoView eliminarProductoView = new EliminarProductoView(productoController);
                            eliminarProductoView.setMensajeHandler(mih);
                            productoController.setEliminarProductoView(eliminarProductoView);
                            principalView.getjDesktopPane().add(eliminarProductoView);

                            ModificarProductoView modificarProductoView = new ModificarProductoView(productoController);
                            modificarProductoView.setMensajeHandler(mih);

                            productoController.setModificarProductoView(modificarProductoView);
                            principalView.getjDesktopPane().add(modificarProductoView);

                            carritoController.vincularListarCarritos(listarCarritosView, principalView.getjDesktopPane());
                            carritoController.configurarEventosListarCarritoAdmin(listarCAdmin, principalView.getjDesktopPane());

                            //ocultar vistas cuando ingresa un usuario
                            if (usuarioAuntenticado.getRol().equals(Rol.USUARIO)) {
                                principalView.deshabilitarMenusAdministrador();
                            } else principalView.deshabilitarMenusUsuario();

                            String texto = mih.get("mensaje.bienvenido") + ": " + usuarioAuntenticado.getCedula();
                            principalView.mostrarMensaje(texto);

                            // menú cuenta usuario
                            principalView.getMenuItemCuentaUsuario().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    usuarioController.abrirCuentaUsuario();
                                }
                            });

                            // menu crear producto
                            principalView.getMenuItemCrearProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!productoAnadirView.isVisible()) {
                                        productoAnadirView.setVisible(true);
                                    }
                                }
                            });

                            // menu buscar producto
                            principalView.getMenuItemBuscarProducto().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!productoListaView.isVisible()) {
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
                                    if (!carritoView.isVisible()) {
                                        carritoView.setVisible(true);
                                    }
                                }
                            });

                            principalView.getMenuItemListarMisCarritos().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!listarCarritosView.isVisible()) {
                                        listarCarritosView.setVisible(true);
                                    }
                                }
                            });

                            principalView.getMenuItemListarCarritosPorUsuario().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!listarCAdmin.isVisible()) {
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