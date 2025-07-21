package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.ExcepcionProductoCodigo;
import ec.edu.ups.util.ExcepcionProductoNombre;
import ec.edu.ups.util.ExcepcionProductoPrecio;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
/**
 * Controlador encargado de gestionar la lógica de negocio relacionada con productos,
 * incluyendo creación, búsqueda, listado, eliminación y modificación.
 * <p>
 * Orquesta las interacciones entre las vistas de producto (añadir, listar, eliminar,
 * modificar) y el DAO de productos, manejando validaciones y mensajes
 * internacionalizados mediante {@link MensajeInternacionalizacionHandler}.
 * </p>
 */
public class ProductoController {

    private final ProductoDAO productoDAO;
    private final CarritoView carritoView;

    private final ProductoAnadirView productoAnadirView;
    private final ProductoListaView productoListaView;

    private EliminarProductoView eliminarProductoView;
    private ModificarProductoView modificarProductoView;

    private final MensajeInternacionalizacionHandler mih;
    /**
     * Constructor que inyecta dependencias y configura listeners iniciales.
     *
     * @param mih Handler de internacionalización para textos.
     * @param productoDAO DAO para persistencia de productos.
     * @param productoAnadirView Vista para añadir productos.
     * @param productoListaView Vista para listar/buscar productos.
     * @param carritoView Vista de carrito para búsquedas rápidas.
     */
    public ProductoController(MensajeInternacionalizacionHandler mih, ProductoDAO productoDAO, ProductoAnadirView productoAnadirView,
                              ProductoListaView productoListaView, CarritoView carritoView) {
        this.mih = mih;
        this.productoDAO = productoDAO;
        this.productoAnadirView = productoAnadirView;
        this.productoListaView = productoListaView;
        this.carritoView = carritoView;
        this.configurarEventosEnVistas();
    }

    /**
     * Registra los listeners para botones de las vistas de añadir, listar y carrito.
     */
    private void configurarEventosEnVistas(){
        productoAnadirView.getBtnAceptar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        productoListaView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });

        productoListaView.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductos();
            }
        });

        productoListaView.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productoListaView.getTxtBuscar().setText("");
                // vacía la tabla
                productoListaView.cargarDatos(Collections.emptyList());
            }
        });

        carritoView.getBuscarButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorCodigo();
            }
        });
    }

    /**
     * Registra listeners para búsqueda y confirmación de eliminación.
     */
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

    // EVENTOS Modificar Producto
    private void configurarEventosModificar() {
        modificarProductoView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoPorNombreParaModificar();
            }
        });

        modificarProductoView.getBtnModificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarYModificarProducto();
            }
        });
    }

    /**
     * Lee datos desde la vista de añadir, valida y persiste un nuevo producto.
     * Muestra mensajes de error o éxito según el caso.
     */
    private void guardarProducto() {
        //leemos lo ingresado como string de los textField
        String codigoStr = productoAnadirView.getCampoCodigo().getText().trim();
        String nombre = productoAnadirView.getCampoNombre().getText().trim();
        String precioStr = productoAnadirView.getCampoPrecio().getText().trim();

        Producto producto;
        try {
            //creamos un nuevo prodcuto
            producto = new Producto();
            producto.setCodigo(Integer.parseInt(codigoStr));
            producto.setNombre(nombre);
            producto.setPrecio(precioStr);
        } catch (NumberFormatException e) {
            // Error al convertir el código a entero
            productoAnadirView.mostrarMensaje(mih.get("producto.mensajeError.codigo"));
            return;
        } catch (ExcepcionProductoCodigo e) {
            productoAnadirView.mostrarMensaje(e.getMessage());
            return;
        } catch (ExcepcionProductoNombre e) {
            productoAnadirView.mostrarMensaje(e.getMessage());
            return;
        } catch (ExcepcionProductoPrecio e) {
            productoAnadirView.mostrarMensaje(e.getMessage());
            return;
        }

        //Validaciones de unicidad siguen aquí
        if (productoDAO.buscarPorCodigo(producto.getCodigo()) != null) {
            String msg = MessageFormat.format(mih.get("producto.mensajeError.codigoExistente"),producto.getCodigo()
            );
            productoAnadirView.mostrarMensaje(msg);
            return;
        }
        //verificar que el producto ingresado no exista
        for (Producto p : productoDAO.listarTodos()) {
            if (p.getNombre().equalsIgnoreCase(producto.getNombre())) {
                String msg = MessageFormat.format(mih.get("producto.mensajeError.nombreExistente"),producto.getNombre());
                productoAnadirView.mostrarMensaje(msg);
                return;
            }
        }
        //se crean los nuevos productos
        productoDAO.crear(producto);
        productoAnadirView.mostrarMensaje(mih.get("producto.mensaje.guardado"));
        productoAnadirView.limpiarCampos();
        productoAnadirView.mostrarProductos(productoDAO.listarTodos());
    }

    /**
     * Busca productos por nombre desde la vista de listado y actualiza la tabla.
     */
    private void buscarProducto() {
        // obtenemos lo ingresado por el usuario
        String nombre = productoListaView.getTxtBuscar().getText().trim();

        if (nombre.isEmpty()) {
            productoListaView.mostrarMensaje(mih.get("producto.mensajeError.campoVacio"));
            productoListaView.cargarDatos(Collections.emptyList());
            return;
        }
        // Llamo al DAO
        List<Producto> productos = productoDAO.buscarPorNombre(nombre);

        // cuando no hay el producto buscado muestro mensaje
        if (productos.isEmpty()) {
            productoListaView.mostrarMensaje(mih.get("producto.mensajeError.productoNoEncontrado"));
        }
        productoListaView.cargarDatos(productos);
    }

    /**
     * Obtiene todos los productos y los muestra en la vista de listado.
     */
    private void listarProductos() {
        List<Producto> productos = productoDAO.listarTodos();
        productoListaView.cargarDatos(productos);
    }

    /**
     * Busca productos por nombre en la vista de eliminación y carga la tabla.
     */
    private void buscarProductoPorNombreParaEliminar() {
        String nombre = eliminarProductoView.getCampoNombre().getText().trim();

        if (nombre.isEmpty()) {
            eliminarProductoView.mostrarMensaje(mih.get("producto.mensajeError.campoVacio"));
            return;
        }
        List<Producto> productos = productoDAO.buscarPorNombre(nombre);

        if (productos.isEmpty()) {
            eliminarProductoView.mostrarMensaje(mih.get("producto.mensajeError.productoNoEncontrado"));
        }
        eliminarProductoView.cargarTabla(productos);
    }

    /**
     * Muestra diálogo de confirmación y elimina el producto seleccionado.
     */
    private void confirmarYEliminarProducto() {
        String codTxt = eliminarProductoView.getTxtCodigoEliminar().getText().trim();

        if (!codTxt.matches("\\d+")) {
            eliminarProductoView.mostrarMensaje(mih.get("producto.mensajeError"));
            return;
        }

        int codigo = Integer.parseInt(codTxt);
        int opcion = JOptionPane.showConfirmDialog(eliminarProductoView,
                MessageFormat.format(mih.get("producto.mensajeConfirmar.eliminar"),codigo),
                mih.get("producto.titulo.mensajeAccion"),
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = eliminarProducto(codigo);
            if (eliminado) {
                eliminarProductoView.removerFila(codigo);
                eliminarProductoView.mostrarMensaje(mih.get("producto.mensajeExito.eliminar"));
            } else {
                eliminarProductoView.mostrarMensaje(mih.get("producto.mensajeError.eliminar"));
            }
        }
    }

    /**
     * Elimina un producto del DAO por código.
     *
     * @param codigo Código del producto a eliminar.
     * @return {@code true} si se eliminó correctamente; {@code false} si no existía.
     */
    private boolean eliminarProducto(int codigo) {
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto != null) {
            productoDAO.eliminar(codigo);
            return true;
        }
        return false;
    }

    // Metodo que realiza la modificación real en el DAO
    private boolean modificarProductoPorNombre(String nombreOriginal, String nuevoNombre, double nuevoPrecio) {
        List<Producto> productos = productoDAO.buscarPorNombre(nombreOriginal);
        if (productos.isEmpty()) {
            return false;
        }
        int producto = modificarProductoView.getTblProductos().getSelectedRow();

        if (producto == -1){
            return  false;
        }

        Producto p = productos.get(producto);//Se escoge el producto seleccionado de la tabla por el usuario

        try { //controla el ingreso del nuevo nombre del producto
            p.setNombre(nuevoNombre);
            //Convierte precio en string
            String precioStr = Double.toString(nuevoPrecio);//se convierte a string para poder validar el formato
            p.setPrecio(precioStr);

            productoDAO.actualizar(p);//envia el objeto modificado al dao para que quede guardado en la
            return true;              //base de datos
        } catch (ExcepcionProductoNombre e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                "Error al modificar", JOptionPane.ERROR_MESSAGE);
        } catch (ExcepcionProductoPrecio e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),
            "Error al modificar", JOptionPane.ERROR_MESSAGE);
        }
        return false;

    }

    /**
     * Busca productos por nombre en la vista de modificación y carga la tabla.
     */
    private void buscarProductoPorNombreParaModificar() {
        String nombre = modificarProductoView.getTxtNombreBuscar().getText().trim();

        if (nombre.isEmpty()) {
            modificarProductoView.mostrarMensaje(mih.get("producto.mensajeError.campoVacio"));
            return;
        }
        List<Producto> productos = productoDAO.buscarPorNombre(nombre);

        if (productos.isEmpty()) {
            modificarProductoView.mostrarMensaje(mih.get("producto.mensajeError.productoNoEncontrado"));
        }
        modificarProductoView.cargarTabla(productos);
    }

    /**
     * Confirma datos ingresados y aplica la modificación al producto.
     */
    private void confirmarYModificarProducto() {
        String nombreBuscar = modificarProductoView.getTxtNombreBuscar().getText().trim();
        String nuevoNombre = modificarProductoView.getTxtNombre().getText().trim();
        String nuevoPrecioTexto = modificarProductoView.getTxtPrecio().getText().trim();

        //si los campos llenados por el usuario estan vacios
        if (nombreBuscar.isEmpty() || nuevoNombre.isEmpty() || nuevoPrecioTexto.isEmpty()) {
            modificarProductoView.mostrarMensaje(mih.get("producto.mensajeError.campoModificar"));
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(
                modificarProductoView,mih.get("producto.mensajeConfirmar.modificar"),mih.get("producto.titulo.mensajeAccion"),
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {return;}

        try {
            double nuevoPrecio = Double.parseDouble(nuevoPrecioTexto);
            Producto auxiliar = new Producto();
            auxiliar.setNombre(nuevoNombre);
            auxiliar.setPrecio(nuevoPrecio);
            // Si no hubo excepción, procedemos con la modificación
            boolean modificado = modificarProductoPorNombre(nombreBuscar,auxiliar.getNombre(),auxiliar.getPrecio());

            if (modificado) {
                modificarProductoView.mostrarMensaje(mih.get("producto.mensajeExito.modificar"));
                limpiarCamposModificar();
            } else {
                modificarProductoView.mostrarMensaje(mih.get("producto.mensajeError.modificar"));
            }

        } catch (NumberFormatException e) {
            modificarProductoView.mostrarMensaje(mih.get("producto.mensajeError.precio"));
        } catch (ExcepcionProductoNombre e) {
            modificarProductoView.mostrarMensaje(e.getMessage());
        } catch (ExcepcionProductoPrecio e) {
            modificarProductoView.mostrarMensaje(e.getMessage());
        }
    }

    //limpiar CAMPOS DE MODIFICACION
    private void limpiarCamposModificar() {
        modificarProductoView.getTxtNombreBuscar().setText("");
        modificarProductoView.getTxtNombre().setText("");
        modificarProductoView.getTxtPrecio().setText("");
        modificarProductoView.getModelo().setRowCount(0);
    }

    //para buscar el producto al crear el carrito
    private void buscarProductoPorCodigo() {
        int codigo = Integer.parseInt(carritoView.getTxtCodigo().getText());
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        if (producto == null) {
            carritoView.mostrarMensaje("No se encontro el producto");
            carritoView.getTxtNombre().setText("");
            carritoView.getTxtPrecio().setText("");
        } else {
            carritoView.getTxtNombre().setText(producto.getNombre());
            carritoView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
        }
    }

    public void setModificarProductoView(ModificarProductoView modificarProductoView) {
        this.modificarProductoView = modificarProductoView;
        configurarEventosModificar(); // Conecta los botones
    }

    public void setEliminarProductoView(EliminarProductoView eliminarProductoView) {
        this.eliminarProductoView = eliminarProductoView;
        configurarEventosEliminar(); // Conecta los botones
    }
}
