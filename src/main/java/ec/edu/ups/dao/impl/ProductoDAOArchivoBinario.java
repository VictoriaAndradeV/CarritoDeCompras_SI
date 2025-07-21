package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementación de {@link ProductoDAO} que guarda productos en un archivo binario.
 * Cada producto ocupa un bloque fijo de bytes, lo que permite lectura/escritura directa
 * sin cargar todo el fichero en memoria.
 *
 * En el constructor se crea el fichero "producto.dat" si no existe.
 *
 * @author Victoria
 */
public class ProductoDAOArchivoBinario implements ProductoDAO {

    private String ruta;
    private static final int RECORD_SIZE = 120;
    private static final int NOMBRE_SIZE = 50;
    private File archivo;
    /**
     * Inicializa el DAO con la carpeta indicada y asegura el archivo binario.
     *
     * @param ruta directorio donde se almacenará "producto.dat"
     */
    public ProductoDAOArchivoBinario(String ruta) {
        this.ruta = ruta;

        archivo = new File(ruta, "producto.dat");
        System.out.printf(archivo.getAbsolutePath());
        if(!archivo.exists()) {
            try{
                archivo.createNewFile();
            }catch (IOException e){
            }
        }
    }
    /**
     * Agrega un producto al final del archivo.
     *
     * @param producto instancia de {@link Producto} a escribir
     */
    @Override
    public void crear(Producto producto) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());
            writeProducto(raf, producto);
        } catch (IOException e) {
            System.err.println("Error al crear producto en archivo binario: " + e.getMessage());
        }
    }
    /**
     * Busca un producto por su código recorriendo registros.
     *
     * @param codigo identificador del producto
     * @return producto encontrado o null si no existe
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codLeido = raf.readInt();
                if (codLeido == codigo) {
                    raf.seek(i * RECORD_SIZE);
                    return readProducto(raf);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }
        return null;
    }
    /**
     * Filtra productos cuyo nombre contiene el texto dado (sin diferenciar mayúsculas).
     *
     * @param nombre fragmento de nombre a buscar
     * @return lista de productos que coinciden
     */
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                Producto p = readProducto(raf);
                if (p.getNombre().toUpperCase().contains(nombre.toUpperCase())) {
                    productosEncontrados.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar producto por nombre: " + e.getMessage());
        }
        return productosEncontrados;
    }
    /**
     * Actualiza un registro existente sobrescribiendo su bloque.
     *
     * @param producto instancia con el mismo código y datos nuevos
     */
    @Override
    public void actualizar(Producto producto) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long numRegistros = raf.length() / RECORD_SIZE; //cantidad de productos
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codLeido = raf.readInt();
                if (codLeido == producto.getCodigo()) {
                    raf.seek(i * RECORD_SIZE);
                    writeProducto(raf, producto);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }
    /**
     * Elimina un registro moviendo los siguientes bloques hacia arriba.
     *
     * @param codigo código del producto a eliminar
     */
    @Override
    public void eliminar(int codigo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            int registroAEliminar = -1;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                if (raf.readInt() == codigo) {
                    registroAEliminar = i;
                    break;
                }
            }

            if (registroAEliminar != -1) {
                for (long j = registroAEliminar + 1; j < numRegistros; j++) {
                    raf.seek(j * RECORD_SIZE);
                    byte[] buffer = new byte[RECORD_SIZE];
                    raf.readFully(buffer);
                    raf.seek((j - 1) * RECORD_SIZE);
                    raf.write(buffer);
                }
                raf.setLength(raf.length() - RECORD_SIZE);
            }
        } catch (IOException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }
    /**
     * Lee todos los registros y devuelve la lista completa.
     *
     * @return lista con todos los productos
     */
    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                productos.add(readProducto(raf));
            }
        } catch (IOException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }

    private void writeProducto(RandomAccessFile raf, Producto producto) throws IOException {
        raf.writeInt(producto.getCodigo());
        raf.writeDouble(producto.getPrecio());

        StringBuffer sb = new StringBuffer(producto.getNombre() != null ? producto.getNombre() : "");
        sb.setLength(NOMBRE_SIZE);
        raf.writeChars(sb.toString());

        long bytesRestantes = RECORD_SIZE - (raf.getFilePointer() % RECORD_SIZE);
        if (bytesRestantes < RECORD_SIZE) {
            raf.write(new byte[(int)bytesRestantes]);
        }
    }

    private Producto readProducto(RandomAccessFile raf) throws IOException {
        int codigo = raf.readInt();
        double precio = raf.readDouble();

        StringBuilder nombreBuilder = new StringBuilder();
        for (int i = 0; i < NOMBRE_SIZE; i++) {
            nombreBuilder.append(raf.readChar());
        }
        String nombre = nombreBuilder.toString().trim();

        try {
            return new Producto(codigo, nombre, precio);
        } catch (Exception e) {
            System.err.println("Error al validar datos del producto leído: " + e.getMessage());
            return null;
        }
    }
}