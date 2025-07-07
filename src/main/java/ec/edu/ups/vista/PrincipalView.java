package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class PrincipalView extends JFrame {
    private MensajeInternacionalizacionHandler mih;
    private JDesktopPane jDesktopPane = new MiJDesktopPane();

    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenu menuSesion;
    private JMenu menuSalirALogin;

    //diferentes menus para producto
    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemActualizarProducto;
    private JMenuItem menuItemBuscarProducto;

    //opciones para carrito
    private JMenuItem menuItemCrearCarrito;
    private JMenuItem menuItemListarMisCarritos;
    private JMenuItem menuItemListarCarritosPorUsuario;

    //opciones para editar la sesion del usuario
    private JMenuItem menuItemCuentaUsuario;
    private JMenuItem menuItemListarUsuarios; //solo para admin

    //opciones para salir
    private JMenuItem menuItemSalir;
    private JMenuItem menuItemSalirALogin;

    public PrincipalView() {
        jDesktopPane = new MiJDesktopPane(); //fondo con imagen
        menuBar = new JMenuBar();

        menuProducto = new JMenu("Producto");
        menuCarrito = new JMenu("Carrito");
        menuSesion = new JMenu("Cuenta");
        menuSalirALogin = new JMenu("Opciones Salida");

        //Menu Producto
        menuItemCrearProducto = new JMenuItem("Crear Producto");
        menuItemEliminarProducto = new JMenuItem("Eliminar Producto");
        menuItemActualizarProducto = new JMenuItem("Modificar/Actualizar Producto");
        menuItemBuscarProducto = new JMenuItem("Buscar/Listar Productos");

        //Menu Carrito
        menuItemCrearCarrito = new JMenuItem("Crear Carrito");
        menuItemListarMisCarritos = new JMenuItem("Mis Carritos");
        menuItemListarCarritosPorUsuario = new JMenuItem("Listar Carritos por Usuario");

        //Menu de la cuenta
        menuItemCuentaUsuario = new JMenuItem("Información de Cuenta");
        menuItemListarUsuarios = new JMenuItem("Listar Usuarios"); //para admin

        //salir cuenta
        menuItemSalir = new JMenuItem("Salir");
        menuItemSalirALogin = new JMenuItem("Cerrar sesión");

        menuBar.add(menuProducto);
        menuBar.add(menuCarrito);
        menuBar.add(menuSesion);
        menuBar.add(menuSalirALogin);

        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);

        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemListarMisCarritos);
        menuCarrito.addSeparator();
        menuCarrito.add(menuItemListarCarritosPorUsuario);

        menuSesion.add(menuItemCuentaUsuario);
        menuSesion.add(menuItemListarUsuarios);

        menuSalirALogin.add(menuItemSalir);
        menuSalirALogin.add(menuItemSalirALogin);

        //idioma por defecto
        mih = new MensajeInternacionalizacionHandler("es", "EC");
        actualizarTextos();

        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Carrito de Compras En Línea");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    //Nuevo constructor que recibe el handler ya inicializado
    public PrincipalView(MensajeInternacionalizacionHandler mih) {
        this();
        this.mih = mih;
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(mih.get("app.titulo"));
        menuProducto.setText(mih.get("menu.producto"));
        menuCarrito.setText(mih.get("menu.carrito"));
        menuSesion.setText(mih.get("menu.sesion"));
        menuSalirALogin .setText(mih.get("menu.salir.opciones"));

        //items del producto
        menuItemCrearProducto.setText(mih.get("menu.producto.crear"));
        menuItemEliminarProducto.setText(mih.get("menu.producto.eliminar"));
        menuItemActualizarProducto.setText(mih.get("menu.producto.actualizar"));
        menuItemBuscarProducto .setText(mih.get("menu.producto.buscar"));

        //items del carrito
        menuItemCrearCarrito.setText(mih.get("menu.carrito.crear"));
        menuItemListarMisCarritos.setText(mih.get("menu.carrito.listarMisCarritos"));
        menuItemListarCarritosPorUsuario.setText(mih.get("menu.carrito.listarPorUsuario"));

        //items de Cuenta
        menuItemCuentaUsuario .setText(mih.get("menu.cuenta.usuario"));
        menuItemListarUsuarios.setText(mih.get("menu.cuenta.admin"));

        menuItemSalir.setText(mih.get("menu.salir.todos"));
        menuItemSalirALogin .setText(mih.get("menu.salir.login"));
    }

    public void deshabilitarMenusAdministrador() {
        getMenuItemCrearProducto().setEnabled(false);
        //getMenuItemBuscarProducto().setEnabled(false);
        getMenuItemActualizarProducto().setEnabled(false);
        getMenuItemEliminarProducto().setEnabled(false);
        getMenuItemListarCarritosPorUsuario().setEnabled(false); //usuario no puede listar los carritos de demas usuarios
        getMenuItemListarUsuarios().setEnabled(false);
    }

    public void deshabilitarMenusUsuario(){
        getMenuItemCrearCarrito().setEnabled(false);
        getMenuItemListarMisCarritos().setEnabled(false);
        getMenuItemCuentaUsuario().setEnabled(false);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //GETTERS Y SETTERS
    public JMenuItem getMenuItemSalir() {
        return menuItemSalir;
    }

    public JMenuItem getMenuItemSalirALogin() {
        return menuItemSalirALogin;
    }

    public MensajeInternacionalizacionHandler getMih() {
        return mih;
    }

    public void setMih(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
    }

    public JMenuItem getMenuItemCrearProducto() {
        return menuItemCrearProducto;
    }

    public JMenuItem getMenuItemEliminarProducto() {
        return menuItemEliminarProducto;
    }

    public JMenuItem getMenuItemActualizarProducto() {
        return menuItemActualizarProducto;
    }

    public JMenuItem getMenuItemBuscarProducto() {
        return menuItemBuscarProducto;
    }

    public JMenuItem getMenuItemCrearCarrito() {
        return menuItemCrearCarrito;
    }
    public JMenuItem getMenuItemListarMisCarritos() {
        return menuItemListarMisCarritos;
    }
    public JMenuItem getMenuItemListarCarritosPorUsuario() {
        return menuItemListarCarritosPorUsuario;
    }
    public JMenuItem getMenuItemCuentaUsuario() {
        return menuItemCuentaUsuario;
    }
    public JMenuItem getMenuItemListarUsuarios() {
        return menuItemListarUsuarios;
    }
    public JDesktopPane getjDesktopPane() {
        return jDesktopPane;
    }

}