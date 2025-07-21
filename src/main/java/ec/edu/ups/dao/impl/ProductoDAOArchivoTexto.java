package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.util.ExcepcionProductoCodigo;
import ec.edu.ups.util.ExcepcionProductoNombre;
import ec.edu.ups.util.ExcepcionProductoPrecio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementación de {@link ProductoDAO} que persiste productos en un archivo de texto.
 * Cada línea del fichero almacena un producto con formato: código,nombre,precio.
 * Permite operaciones básicas: crear, buscar, actualizar, eliminar y listar.
 *
 * En el constructor se asegura la existencia del archivo "productos.txt".
 *
 * @author Victoria
 */
public class ProductoDAOArchivoTexto implements ProductoDAO {

    private String ruta;
    private FileWriter archivoEscritura;
    private BufferedWriter bw; //permite la escritura
    private FileReader fr;
    private BufferedReader lectura;
    private File archivo;
    /**
     * Inicializa el DAO usando la carpeta indicada.
     * Crea el archivo "productos.txt" si no existe.
     *
     * @param ruta directorio donde se guardará el archivo
     */
    public ProductoDAOArchivoTexto(String ruta) {
        this.ruta = ruta;

        archivo = new File(ruta, "productos.txt");
        if(!archivo.exists()) {
            try{
                archivo.createNewFile();
            }catch (IOException e){
            }
        }
    }
    /**
     * Añade un producto al final del fichero.
     *
     * @param producto datos del producto a guardar
     */
    @Override
    public void crear(Producto producto) {
        try {
            archivoEscritura = new FileWriter(archivo, true);
            bw = new BufferedWriter(archivoEscritura); //permite la escritura en el archivo
            // Formato: codigo,nombre,precio
            String linea = producto.getCodigo() + "," + producto.getNombre() + "," + producto.getPrecio();
            bw.write(linea);
            bw.newLine();
            bw.close();
            archivoEscritura.close();
        } catch (FileNotFoundException e1){
            System.out.println("Ruta de archivo no encontrada");
        } catch (IOException e) {
            System.out.println("Error al crear producto: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Error General");
        }
    }
    /**
     * Busca un producto por su código comparando cada línea del fichero.
     *
     * @param codigo identificador del producto
     * @return producto encontrado, o null si no existe
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto p : listarTodos()) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }
    /**
     * Filtra productos cuyo nombre contenga el texto dado (ignorando mayúsculas).
     *
     * @param nombre fragmento de nombre a buscar
     * @return lista de productos coincidentes
     */
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : listarTodos()) {
            if (p.getNombre().toUpperCase().contains(nombre.toUpperCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }
    /**
     * Reemplaza los datos de un producto existente y guarda los cambios en el fichero.
     *
     * @param producto instancia con el mismo código y datos actualizados
     */
    @Override
    public void actualizar(Producto producto) {
        List<Producto> todos = listarTodos();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getCodigo() == producto.getCodigo()) {
                todos.set(i, producto);
                break;
            }
        }
        sobrescribirArchivo(todos);
    }
    /**
     * Elimina el producto con el código dado y reescribe el fichero.
     *
     * @param codigo identificador del producto a eliminar
     */
    @Override
    public void eliminar(int codigo) {
        List<Producto> todos = listarTodos();
        todos.removeIf(p -> p.getCodigo() == codigo);
        sobrescribirArchivo(todos);
    }
    /**
     * Lee todas las líneas del fichero, crea objetos Producto y los devuelve.
     *
     * @return lista de productos registrados
     */
    @Override
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        try {
            fr = new FileReader(archivo);
            lectura = new BufferedReader(fr);
            String linea;
            while ((linea = lectura.readLine()) != null) {
                String[] parts = linea.split(",");
                if (parts.length >= 3) {
                    try {
                        int codigo = Integer.parseInt(parts[0]);
                        String nombre = parts[1];
                        double precio = Double.parseDouble(parts[2]);
                        // Este constructor o setters puede lanzar tus excepciones
                        Producto p = new Producto(codigo, nombre, precio);
                        lista.add(p);
                    } catch (ExcepcionProductoCodigo e) {
                        System.out.println("Omitido por código inválido: " + e.getMessage());
                    } catch (ExcepcionProductoNombre e) {
                        System.out.println("Omitido por nombre inválido: " + e.getMessage());
                    } catch (ExcepcionProductoPrecio e) {
                        System.out.println("Omitido por precio inválido: " + e.getMessage());
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
     * Reescribe el fichero completo con la lista de productos dada.
     *
     * @param productos lista de productos a guardar
     */
    private void sobrescribirArchivo(List<Producto> productos) {
        try {
            archivoEscritura = new FileWriter(archivo, false);
            bw = new BufferedWriter(archivoEscritura);
            for (Producto p : productos) {
                String linea = p.getCodigo() + "," + p.getNombre() + "," + p.getPrecio();
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
