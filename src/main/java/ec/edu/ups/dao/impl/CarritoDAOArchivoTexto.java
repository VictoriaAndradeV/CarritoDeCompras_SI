package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Implementación de {@link CarritoDAO} que persiste los carritos de compra
 * en un archivo de texto plano.
 * <p>
 * Cada carrito se almacena en una línea con formato:
 * <pre>
 * codigo,dd/MM/yyyy,prodId1 nombre1 precio1 cantidad1/.../prodIdN nombreN precioN cantidadN/
 * </pre>
 * Permite operaciones de creación, consulta, limpieza y eliminación,
 * así como listado completo o filtrado por usuario.
 * </p>
 */
public class CarritoDAOArchivoTexto implements CarritoDAO {

    /** Ruta del directorio donde se guarda el archivo carrito.txt. */
    private String ruta;
    /** Flujo para escritura de caracteres en el archivo. */
    private FileWriter archivoEscritura;
    /** Buffer que optimiza la escritura en el archivo. */
    private BufferedWriter bw;
    /** Flujo para lectura de caracteres desde el archivo. */
    private FileReader fr;
    /** Buffer que optimiza la lectura del archivo. */
    private BufferedReader lectura;
    /** Archivo que almacena los datos de los carritos. */
    private File archivo;
    /**
     * Construye el DAO y asegura que el archivo de texto exista.
     *
     * @param ruta Directorio donde se ubicará "carrito.txt".
     */
    public CarritoDAOArchivoTexto(String ruta) {
        this.ruta = ruta;

        archivo = new File(ruta, "carrito.txt");
        if(!archivo.exists()) {
            try{
                archivo.createNewFile();
            }catch (IOException e){
            }
        }
    }
    /**
     * Escribe un nuevo carrito al final del archivo.
     * Cada línea representa un carrito con sus items.
     *
     * @param carrito Carrito a persistir.
     */
    @Override
    public void crear(Carrito carrito) {
        try {
            archivoEscritura = new FileWriter(archivo, true);
            bw = new BufferedWriter(archivoEscritura); //permite la escritura en el archivo

            String linea = carrito.getCodigo() + "," + carrito.getFechaCreacion().get(Calendar.DAY_OF_MONTH) + "/" +
                    carrito.getFechaCreacion().get(Calendar.MONTH) + "/" +
                    carrito.getFechaCreacion().get(Calendar.YEAR) + ",";

            for(ItemCarrito i: carrito.obtenerItems()){
                linea += i.getProducto().getCodigo() + " " + i.getProducto().getNombre() + " " + i.getProducto().getPrecio() + " " +
                        i.getCantidad() + "/";
            }

            bw.write(linea);
            bw.newLine();
            bw.close();
            archivoEscritura.close();
        } catch (FileNotFoundException e1){
            System.out.println("Ruta de archivo no encontrada: " + ruta);
        } catch (IOException e) {
            System.out.println("Error al crear producto: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error General");
        }
    }
    /**
     * Busca un carrito existente por su código.
     *
     * @param codigo Código del carrito.
     * @return Carrito encontrado o {@code null} si no existe.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        for (Carrito c : listarTodos()) {
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }
    /**
     * Limpia (vacía) el contenido de un carrito específico y actualiza el archivo.
     *
     * @param carrito Carrito con código a limpiar.
     */
    @Override
    public void limpiar(Carrito carrito) {
        List<Carrito> todos = listarTodos();
        for(Carrito c: todos){
            if (c.getCodigo() == carrito.getCodigo()){
                for (ItemCarrito i: c.obtenerItems()){
                    c.obtenerItems().remove(i);
                }
            }
        }
        sobrescribirArchivo(todos);
    }
    /**
     * Elimina un carrito por su código y actualiza el archivo.
     *
     * @param codigo Código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        List<Carrito> todos = listarTodos();
        for (Carrito c: todos){
            if(c.getCodigo() == codigo){
                todos.remove(c);
                break;
            }
        }
        sobrescribirArchivo(todos);
    }
    /**
     * Lee todas las líneas del archivo y reconstruye objetos {@link Carrito}.
     *
     * @return Lista de carritos almacenados.
     */
    @Override
    public List<Carrito> listarTodos() {
        List<Carrito> lista = new ArrayList<>();

        try {
            fr = new FileReader(archivo);
            lectura = new BufferedReader(fr);
            String linea;
            while ((linea = lectura.readLine()) != null) {
                String[] parts = linea.split(",");
                if (parts.length >= 3) {
                    try {
                        int codigo = Integer.parseInt(parts[0]);
                        String[] fecha = parts[1].split("-");
                        String[] items = parts[2].split("-");
                        // Este constructor o setters puede lanzar tus excepciones
                        Carrito c = new Carrito();
                        List<ItemCarrito> itemsLista = new ArrayList<>();
                        c.setCodigo(codigo);
                        c.setCalender(Integer.parseInt(fecha[0]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[2]));
                        for (String s: items){
                            String[] productoCarrito = s.split(" ");
                            Producto p = new Producto(Integer.parseInt(productoCarrito[0]),
                                    productoCarrito[1], Double.parseDouble(productoCarrito[2]));
                            ItemCarrito item = new ItemCarrito(p, Integer.parseInt(productoCarrito[3]));
                            itemsLista.add(item);
                        }
                        c.setItems(itemsLista);
                        lista.add(c);
                    } catch (Exception e) {
                        System.out.println("Omitido por código inválido: " + e.getMessage());
                    }
                }
            }
            lectura.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }
    /**
     * Filtra los carritos pertenecientes a un usuario dado.
     *
     * @param nombreDeUsuario Identificador del usuario.
     * @return Lista de carritos de ese usuario.
     */
    @Override
    public List<Carrito> listarPorUsuario(String nombreDeUsuario) {
        List<Carrito> todos = listarTodos();
        List<Carrito> filtrado = new ArrayList<>();
        for (Carrito c: todos){
            if (c.getUsuario().equals(nombreDeUsuario))
                filtrado.add(c);
        }

        return filtrado;
    }

    @Override
    public void eliminarPorUsuario(String nombreDeUsuario) {

    }
    /**
     * Reescribe todo el archivo con la lista completa de carritos dada.
     *
     * @param todos Lista de carritos para persistir.
     */
    private void sobrescribirArchivo(List<Carrito> todos) {
        try {
            archivoEscritura = new FileWriter(archivo, false);
            bw = new BufferedWriter(archivoEscritura);
            for (Carrito c : todos) {
                String linea = c.getCodigo() + "," + c.getFechaCreacion().get(Calendar.DAY_OF_MONTH) + "/" +
                        c.getFechaCreacion().get(Calendar.MONTH) + "/" +
                        c.getFechaCreacion().get(Calendar.YEAR) + ",";

                for(ItemCarrito i: c.obtenerItems()){
                    linea += i.getProducto().getCodigo() + " " + i.getProducto().getNombre() + " " + i.getProducto().getPrecio() + " " +
                            i.getCantidad() + "/";
                }
                bw.write(linea);
                bw.newLine();
            }
            bw.close();
            archivoEscritura.close();
        } catch(FileNotFoundException e1) {
            System.out.println("Ruta de archivo no encontrada");
        } catch (IOException e) {
            System.out.println("Error al sobrescribir productos: " + e.getMessage());
        } catch(Exception e3) {
            System.out.println("Error General");
        }
    }
}
