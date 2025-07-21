package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.modelo.RespuestaDeSeguridad;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.*;

import java.io.*;
import java.util.*;

public class UsuarioDAOArchivoTexto implements UsuarioDAO {
    private String ruta;
    private FileWriter archivoEscritura;
    private BufferedWriter bw;
    private File archivo;

    public UsuarioDAOArchivoTexto(String ruta) {
        this.ruta = ruta;

        archivo = new File(ruta, "usuario.txt");
        if(!archivo.exists()) {
            try{
                archivo.createNewFile();
            }catch (IOException e){
            }
        }
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario u : listarTodos()) {
            if (u.getCedula().equals(username) && u.getContrasenia().equals(contrasenia)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) {

        File parent = archivo.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(usuario.getFechaNacimiento());
        String base = String.join(";",
                usuario.getCedula(),
                usuario.getContrasenia(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getTelefono(),
                calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.MONTH) + " " +
                        calendar.get(Calendar.DAY_OF_MONTH)
        );

        // 3) Agregar preguntas sólo si hay al menos 3 respuestas
        List<RespuestaDeSeguridad> resp = usuario.getRespuestasSeguridad();
        String linea = base;
        if (resp != null && resp.size() >= 3) {
            String preguntas = resp.get(0).getRespuesta() + "," + resp.get(0).getPregunta().getClave()
                    + "," + resp.get(1).getRespuesta() + "," + resp.get(1).getPregunta().getClave()
                    + "," + resp.get(2).getRespuesta() + "," + resp.get(2).getPregunta().getClave();
            linea += ";" + preguntas;
        }

        // 4) Escribir al archivo con try-with-resources (auto‑close y flush)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
            System.out.println("Guardando usuario en: " + archivo.getAbsolutePath());
            System.out.println("Línea escrita: " + linea);
            writer.write(linea);
            writer.newLine();
            // no es necesario writer.flush() ni writer.close(): lo hace el try-with-resources
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Usuario buscarPorUsername(String username) {
        for (Usuario u : listarTodos()) {
            if (u.getCedula().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String username) {
        List<Usuario> todos = listarTodos();

        for(Usuario u: todos){
            if(u.getCedula().equals(username)){
                todos.remove(u);
            }
        }
        // Reescribir archivo
        try {
            archivoEscritura = new FileWriter(archivo, false);
            bw = new BufferedWriter(archivoEscritura);
            for (Usuario u : todos) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(u.getFechaNacimiento());
                List<RespuestaDeSeguridad> respuesta = u.getRespuestasSeguridad();
                String linea = String.join(";",
                        u.getCedula(),
                        u.getContrasenia(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getEmail(),
                        u.getRol().name(),
                        u.getTelefono(),
                        calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH),
                        respuesta.get(0).getRespuesta() + "," + respuesta.get(0).getPregunta().getClave() + "," + respuesta.get(1).getRespuesta() + "," + respuesta.get(1).getPregunta().getClave() + "," + respuesta.get(2).getRespuesta() + "," + respuesta.get(2).getPregunta().getClave()

                );
                bw.write(linea);
                bw.newLine();
            }
            bw.close();
            archivoEscritura.close();
        } catch (IOException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> todos = listarTodos();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getCedula().equals(usuario.getCedula())){
                todos.set(i, usuario);
                break;
            }
        }

        // Reescribir archivo con la lista actualizada
        try {
            archivoEscritura = new FileWriter(archivo, false);
            bw = new BufferedWriter(archivoEscritura);
            for (Usuario u : todos) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(u.getFechaNacimiento());
                List<RespuestaDeSeguridad> respuesta = u.getRespuestasSeguridad();
                String linea = String.join(";",
                        u.getCedula(),
                        u.getContrasenia(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getEmail(),
                        u.getRol().name(),
                        u.getTelefono(),
                        calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH),
                        respuesta.get(0).getRespuesta() + "," + respuesta.get(0).getPregunta().getClave() + "," + respuesta.get(1).getRespuesta() + "," + respuesta.get(1).getPregunta().getClave() + "," + respuesta.get(2).getRespuesta() + "," + respuesta.get(2).getPregunta().getClave()

                );
                bw.write(linea);
                bw.newLine();
            }
            bw.close();
            archivoEscritura.close();
        } catch (IOException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] parts = linea.split(";");
                // Aseguramos al menos 8 columnas: campos básicos sin preguntas
                if (parts.length >= 8) {
                    Usuario u = new Usuario();

                    // Validación de campos que pueden lanzar Excepcion*
                    try {
                        u.setCedula(parts[0]);
                        u.setContrasenia(parts[1]);
                        u.setNombre(parts[2]);
                        u.setApellido(parts[3]);
                        u.setEmail(parts[4]);
                        u.setTelefono(parts[6]);
                    } catch (ExcepcionCedula e) {
                        System.out.println(e.getMessage());
                    } catch (ExcepcionContrasenia e) {
                        System.out.println(e.getMessage());
                    } catch (ExcepcionNomApe e) {
                        System.out.println(e.getMessage());
                    } catch (ExcepcionCorreo e) {
                        System.out.println(e.getMessage());
                    } catch (ExcepcionTelefono e) {
                        throw new RuntimeException(e);
                    }

                    // Rol (no lanza excepciones personalizadas)
                    try {
                        u.setRol(Rol.valueOf(parts[5]));
                    } catch (IllegalArgumentException iae) {
                        // rol inválido, puedes asignar uno por defecto o ignorar
                        u.setRol(Rol.USUARIO);
                    }

                    // Fecha de nacimiento
                    String[] fecha = parts[7].split(" ");
                    int año = Integer.parseInt(fecha[0]) - 1900;
                    int mes = Integer.parseInt(fecha[1]);
                    int dia = Integer.parseInt(fecha[2]);
                    u.setFechaNacimiento(new Date(año, mes, dia));

                    // Preguntas de seguridad (columna 9, índice 8)
                    List<RespuestaDeSeguridad> respuestas = new ArrayList<>();
                    if (parts.length > 8) {
                        String[] datosPreg = parts[8].split(",");
                        if (datosPreg.length >= 6) {
                            respuestas.add(new RespuestaDeSeguridad(
                                    new PreguntaSeguridad(datosPreg[1]), datosPreg[0]
                            ));
                            respuestas.add(new RespuestaDeSeguridad(
                                    new PreguntaSeguridad(datosPreg[3]), datosPreg[2]
                            ));
                            respuestas.add(new RespuestaDeSeguridad(
                                    new PreguntaSeguridad(datosPreg[5]), datosPreg[4]
                            ));
                        }
                    }
                    u.setRespuestasSeguridad(respuestas);

                    // Agregar usuario a la lista
                    lista.add(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo de usuarios: " + e.getMessage());
        }
        return lista;
    }


}
