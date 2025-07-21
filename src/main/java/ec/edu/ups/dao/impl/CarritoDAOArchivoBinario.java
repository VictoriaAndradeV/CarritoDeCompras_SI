package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Usuario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Implementación de {@link CarritoDAO} que persiste los carritos en
 * un archivo binario de acceso aleatorio (RandomAccessFile).
 * <p>
 * Cada registro ocupa un tamaño fijo de {@code RECORD_SIZE} bytes,
 * almacenando un entero para el código del carrito seguido de
 * {@code CEDULA_LENGTH} caracteres para la cédula del usuario.
 * </p>
 */
public class CarritoDAOArchivoBinario implements CarritoDAO {

    /** Ruta del directorio donde se ubica el archivo binario. */
    private String ruta;
    /** Tamaño en bytes de cada registro fijo en el archivo. */
    private static final int RECORD_SIZE = 24;
    /** Número de caracteres reservados para almacenar la cédula. */
    private static final int CEDULA_LENGTH = 10;
    /** Archivo binario que contiene los registros de carritos. */
    private File archivo;
    /**
     * Constructor que inicializa la ruta y crea el archivo si no existe.
     *
     * @param ruta Directorio donde se guardará "carrito.dat".
     */
    public CarritoDAOArchivoBinario(String ruta) {
        this.ruta = ruta;
        //creamos el archivo dentro del constructor, en caso de que no exista
        archivo = new File(ruta, "carrito.dat");
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
            }
        }

    }
    /**
     * Persiste un nuevo carrito al final del archivo.
     *
     * @param carrito Carrito a escribir.
     */
    @Override
    public void crear(Carrito carrito) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());
            raf.writeInt(carrito.getCodigo());

            StringBuffer sb = new StringBuffer(carrito.getUsuario().getCedula());
            sb.setLength(CEDULA_LENGTH);
            raf.writeChars(sb.toString());

        } catch (IOException e) {
            System.err.println("Error de E/S al crear carrito: " + e.getMessage());
        }
    }
    /**
     * Busca un carrito por su código recorriendo registros.
     *
     * @param codigo Código del carrito a buscar.
     * @return Carrito encontrado o {@code null} si no existe.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codLeido = raf.readInt();

                if (codLeido == codigo) {
                    StringBuilder cedulaBuilder = new StringBuilder();
                    for (int j = 0; j < CEDULA_LENGTH; j++) {
                        cedulaBuilder.append(raf.readChar());
                    }

                    Carrito carrito = new Carrito();
                    carrito.setCodigo(codLeido);
                    Usuario usuario = new Usuario();
                    usuario.setCedula(cedulaBuilder.toString().trim());
                    carrito.setUsuario(usuario);
                    return carrito;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar carrito: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void limpiar(Carrito carrito) {
    }
    /**
     * Elimina un carrito por código desplazando registros siguientes.
     *
     * @param codigo Código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codLeido = raf.readInt();
                if (codLeido == codigo) {
                    for (long j = i + 1; j < numRegistros; j++) {
                        raf.seek(j * RECORD_SIZE);
                        byte[] buffer = new byte[RECORD_SIZE];
                        raf.readFully(buffer);

                        raf.seek((j - 1) * RECORD_SIZE);
                        raf.write(buffer);
                    }
                    raf.setLength(raf.length() - RECORD_SIZE);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al eliminar carrito: " + e.getMessage());
        }
    }
    /**
     * Lista todos los carritos leyendo cada registro y construyendo objetos.
     *
     * @return Lista de carritos almacenados.
     */
    @Override
    public List<Carrito> listarTodos() {
        List<Carrito> lista = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codigo = raf.readInt();

                StringBuilder cedulaBuilder = new StringBuilder();
                for (int j = 0; j < CEDULA_LENGTH; j++) {
                    cedulaBuilder.append(raf.readChar());
                }

                Carrito carrito = new Carrito();
                carrito.setCodigo(codigo);
                Usuario usuario = new Usuario();
                usuario.setCedula(cedulaBuilder.toString().trim());
                carrito.setUsuario(usuario);
                lista.add(carrito);
            }
        } catch (Exception e) {
            System.err.println("Error al listar carritos: " + e.getMessage());
        }
        return lista;
    }
    /**
     * Filtra los carritos pertenecientes a un usuario específico.
     *
     * @param cedulaDeUsuario Cédula del usuario.
     * @return Lista de carritos para ese usuario.
     */
    @Override
    public List<Carrito> listarPorUsuario(String cedulaDeUsuario) {
        List<Carrito> carritosUsuario = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                int codigo = raf.readInt();

                StringBuilder cedulaLeidaBuilder = new StringBuilder();
                for (int j = 0; j < CEDULA_LENGTH; j++) {
                    cedulaLeidaBuilder.append(raf.readChar());
                }
                String cedulaLeida = cedulaLeidaBuilder.toString().trim();

                if (cedulaLeida.equals(cedulaDeUsuario)) {
                    Carrito carrito = new Carrito();
                    carrito.setCodigo(codigo);
                    Usuario usuario = new Usuario();
                    usuario.setCedula(cedulaLeida);
                    carrito.setUsuario(usuario);
                    carritosUsuario.add(carrito);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar carritos por usuario: " + e.getMessage());
        }
        return carritosUsuario;
    }
    /**
     * Elimina todos los carritos de un usuario y reescribe el archivo.
     *
     * @param cedulaUsuario Cédula del usuario cuyos carritos se eliminarán.
     */
    @Override
    public void eliminarPorUsuario(String cedulaUsuario) {
        List<Carrito> todos = listarTodos();

        Iterator<Carrito> it = todos.iterator();
        while (it.hasNext()) {
            Carrito c = it.next();
            if (c.getUsuario().getCedula().equals(cedulaUsuario)) {
                it.remove();
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.setLength(0);
            for (Carrito carrito : todos) {
                raf.writeInt(carrito.getCodigo());
                StringBuffer sb = new StringBuffer(carrito.getUsuario().getCedula());
                sb.setLength(CEDULA_LENGTH);
                raf.writeChars(sb.toString());
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al eliminar carritos por usuario: " + e.getMessage());
        }
    }
}