package ec.edu.ups.vista;

import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.ProductoControllerGestionar;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                ProductoAnadirView productoView = new ProductoAnadirView();
                ProductoListaView productoListaView = new ProductoListaView();
                ProductoGestionarView productoGestionarView = new ProductoGestionarView();

                ProductoDAO productoDAO = new ProductoDAOMemoria();

                new ProductoController(productoDAO, productoView, productoListaView);
                new ProductoControllerGestionar(productoDAO, productoGestionarView);

            }
        });
    }
}
