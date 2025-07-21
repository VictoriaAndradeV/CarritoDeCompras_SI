package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.modelo.RespuestaDeSeguridad;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioDAOArchivoBinario implements UsuarioDAO {

    private String ruta;
    private static final int RECORD_SIZE = 888;

    private static final int CEDULA_SIZE = 10;
    private static final int CONTRASENIA_SIZE = 20;
    private static final int NOMBRE_SIZE = 25;
    private static final int APELLIDO_SIZE = 25;
    private static final int EMAIL_SIZE = 50;
    private static final int TELEFONO_SIZE = 10;

    private static final int MAX_PREGUNTAS = 3;
    private static final int PREGUNTA_CLAVE_SIZE = 48;
    private static final int RESPUESTA_SIZE = 50;

    private File archivo;

    public UsuarioDAOArchivoBinario(String ruta) {
        this.ruta = ruta;

        archivo = new File(ruta, "usuarios.dat");
        if(!archivo.exists()) {
            try{
                archivo.createNewFile();
            }catch (IOException e){
            }
        }
    }

    @Override
    public void crear(Usuario usuario) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            raf.seek(raf.length());
            writeUsuario(raf, usuario);
        } catch (IOException e) {
            System.err.println("Error al crear usuario en archivo binario: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                String cedula = readFixedString(raf, CEDULA_SIZE);
                if (cedula.equals(username)) {
                    raf.seek(i * RECORD_SIZE);
                    return readUsuario(raf);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al buscar usuario en archivo binario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        Usuario usuario = buscarPorUsername(username);
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuario) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                String cedula = readFixedString(raf, CEDULA_SIZE);
                if (cedula.equals(usuario.getCedula())) {
                    raf.seek(i * RECORD_SIZE);
                    writeUsuario(raf, usuario);
                    return;
                }
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar usuario en archivo binario: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String username) {
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "rw")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            int registroAEliminar = -1;

            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                if (readFixedString(raf, CEDULA_SIZE).equals(username)) {
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
            System.err.println("Error al eliminar usuario en archivo binario: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(archivo, "r")) {
            long numRegistros = raf.length() / RECORD_SIZE;
            for (int i = 0; i < numRegistros; i++) {
                raf.seek(i * RECORD_SIZE);
                usuarios.add(readUsuario(raf));
            }
        } catch (IOException e) {
            System.err.println("Error al listar usuarios de archivo binario: " + e.getMessage());
        }
        return usuarios;
    }

    private void writeUsuario(RandomAccessFile raf, Usuario usuario) throws IOException {
        writeFixedString(raf, usuario.getCedula(), CEDULA_SIZE);
        writeFixedString(raf, usuario.getContrasenia(), CONTRASENIA_SIZE);
        writeFixedString(raf, usuario.getNombre(), NOMBRE_SIZE);
        writeFixedString(raf, usuario.getApellido(), APELLIDO_SIZE);
        writeFixedString(raf, usuario.getEmail(), EMAIL_SIZE);
        writeFixedString(raf, usuario.getTelefono(), TELEFONO_SIZE);
        raf.writeInt(usuario.getRol().ordinal());
        raf.writeLong(usuario.getFechaNacimiento().getTime());

        List<RespuestaDeSeguridad> respuestas = usuario.getRespuestasSeguridad();
        for (int i = 0; i < MAX_PREGUNTAS; i++) {
            if (respuestas != null && i < respuestas.size()) {
                RespuestaDeSeguridad r = respuestas.get(i);
                writeFixedString(raf, r.getPregunta().getClave(), PREGUNTA_CLAVE_SIZE);
                writeFixedString(raf, r.getRespuesta(), RESPUESTA_SIZE);
            } else {
                writeFixedString(raf, "", PREGUNTA_CLAVE_SIZE);
                writeFixedString(raf, "", RESPUESTA_SIZE);
            }
        }

        long bytesEscritos = raf.getFilePointer() % RECORD_SIZE;
        if (bytesEscritos > 0 && bytesEscritos < RECORD_SIZE) {
            raf.write(new byte[(int)(RECORD_SIZE - bytesEscritos)]);
        }
    }

    private Usuario readUsuario(RandomAccessFile raf) throws IOException {
        String cedula = readFixedString(raf, CEDULA_SIZE);
        String contrasenia = readFixedString(raf, CONTRASENIA_SIZE);
        String nombre = readFixedString(raf, NOMBRE_SIZE);
        String apellido = readFixedString(raf, APELLIDO_SIZE);
        String email = readFixedString(raf, EMAIL_SIZE);
        String telefono = readFixedString(raf, TELEFONO_SIZE);
        int rolOrdinal = raf.readInt();
        Rol rol = Rol.values()[rolOrdinal];
        long fechaMillis = raf.readLong();
        Date fecha = new Date(fechaMillis);

        List<RespuestaDeSeguridad> respuestas = new ArrayList<>();
        for (int i = 0; i < MAX_PREGUNTAS; i++) {
            String clavePregunta = readFixedString(raf, PREGUNTA_CLAVE_SIZE);
            String textoRespuesta = readFixedString(raf, RESPUESTA_SIZE);
            if (!clavePregunta.isEmpty()) {
                PreguntaSeguridad p = new PreguntaSeguridad(clavePregunta);
                RespuestaDeSeguridad r = new RespuestaDeSeguridad(p, textoRespuesta);
                respuestas.add(r);
            }
        }

        Usuario usuario = new Usuario();
        try {
            usuario.setCedula(cedula);
            usuario.setContrasenia(contrasenia);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            usuario.setRol(rol);
            usuario.setFechaNacimiento(fecha);
            usuario.setRespuestasSeguridad(respuestas);
        } catch (Exception e) {
            System.err.println("Error al validar datos del usuario leído: " + e.getMessage());
        }
        return usuario;
    }

    private void writeFixedString(RandomAccessFile raf, String s, int size) throws IOException {
        //if else si s es null, se guarda s caso contraio guardo un caracter vacio
        StringBuffer sb = new StringBuffer(s != null ? s : "");
        sb.setLength(size);
        raf.writeChars(sb.toString());
    }

    private String readFixedString(RandomAccessFile raf, int size) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(raf.readChar());
        }
        return sb.toString().trim();
    }
}