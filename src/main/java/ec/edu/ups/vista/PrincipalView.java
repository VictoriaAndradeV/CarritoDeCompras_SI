package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class PrincipalView extends JFrame {
    private MensajeInternacionalizacionHandler mih;

    private JMenuBar menuBar;

    private JMenu menuProducto;
    private JMenu menuCarrito;
    private JMenu menuSesion;

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

    private JDesktopPane jDesktopPane;

    public PrincipalView() {
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();

        menuProducto = new JMenu("Producto");
        menuCarrito = new JMenu("Carrito");
        menuSesion = new JMenu("Cuenta");

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

        menuBar.add(menuProducto);
        menuBar.add(menuCarrito);
        menuBar.add(menuSesion);

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
        this();               //llama al constructor por defecto
        this.mih = mih;       //asigna el handler
        actualizarTextos();   //pinta los textos con el idioma adecuado
    }

    public void actualizarTextos() {
        setTitle(mih.get("app.titulo"));
        menuProducto.setText(mih.get("menu.producto"));
        menuCarrito.setText(mih.get("menu.carrito"));
        menuSesion.setText(mih.get("menu.sesion"));

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
    }

    public void deshabilitarMenusAdministrador() {
        getMenuItemCrearProducto().setEnabled(false);
        getMenuItemBuscarProducto().setEnabled(false);
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
    public MensajeInternacionalizacionHandler getMih() {
        return mih;
    }

    public void setMih(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
    }

    public JMenuItem getMenuItemCrearProducto() {
        return menuItemCrearProducto;
    }

    public JMenu getMenuProducto() {
        return menuProducto;
    }

    public void setMenuProducto(JMenu menuProducto) {
        this.menuProducto = menuProducto;
    }

    public JMenu getMenuCarrito() {
        return menuCarrito;
    }

    public void setMenuCarrito(JMenu menuCarrito) {
        this.menuCarrito = menuCarrito;
    }

    public JMenu getMenuSesion() {
        return menuSesion;
    }

    public void setMenuSesion(JMenu menuSesion) {
        this.menuSesion = menuSesion;
    }

    public void setMenuItemCrearProducto(JMenuItem menuItemCrearProducto) {
        this.menuItemCrearProducto = menuItemCrearProducto;
    }

    public JMenuItem getMenuItemEliminarProducto() {
        return menuItemEliminarProducto;
    }

    public void setMenuItemEliminarProducto(JMenuItem menuItemEliminarProducto) {
        this.menuItemEliminarProducto = menuItemEliminarProducto;
    }

    public JMenuItem getMenuItemActualizarProducto() {
        return menuItemActualizarProducto;
    }

    public void setMenuItemActualizarProducto(JMenuItem menuItemActualizarProducto) {
        this.menuItemActualizarProducto = menuItemActualizarProducto;
    }

    public JMenuItem getMenuItemBuscarProducto() {
        return menuItemBuscarProducto;
    }

    public void setMenuItemBuscarProducto(JMenuItem menuItemBuscarProducto) {
        this.menuItemBuscarProducto = menuItemBuscarProducto;
    }

    public JMenuItem getMenuItemCrearCarrito() {
        return menuItemCrearCarrito;
    }

    public void setMenuItemCrearCarrito(JMenuItem menuItemCrearCarrito) {
        this.menuItemCrearCarrito = menuItemCrearCarrito;
    }

    public JMenuItem getMenuItemListarMisCarritos() {
        return menuItemListarMisCarritos;
    }

    public void setMenuItemListarMisCarritos(JMenuItem menuItemListarMisCarritos) {
        this.menuItemListarMisCarritos = menuItemListarMisCarritos;
    }

    public JMenuItem getMenuItemListarCarritosPorUsuario() {
        return menuItemListarCarritosPorUsuario;
    }

    public void setMenuItemListarCarritosPorUsuario(JMenuItem menuItemListarCarritosPorUsuario) {
        this.menuItemListarCarritosPorUsuario = menuItemListarCarritosPorUsuario;
    }

    public JMenuItem getMenuItemCuentaUsuario() {
        return menuItemCuentaUsuario;
    }

    public void setMenuItemCuentaUsuario(JMenuItem menuItemCuentaUsuario) {
        this.menuItemCuentaUsuario = menuItemCuentaUsuario;
    }

    public JMenuItem getMenuItemListarUsuarios() {
        return menuItemListarUsuarios;
    }

    public void setMenuItemListarUsuarios(JMenuItem menuItemListarUsuarios) {
        this.menuItemListarUsuarios = menuItemListarUsuarios;
    }

    public JDesktopPane getjDesktopPane() {
        return jDesktopPane;
    }

    public void setjDesktopPane(JDesktopPane jDesktopPane) {
        this.jDesktopPane = jDesktopPane;
    }
}