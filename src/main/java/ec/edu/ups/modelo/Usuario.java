package ec.edu.ups.modelo;

import java.util.Date;
import java.util.List;

public class Usuario {
    private String usuario;
    private String contrasenia;
    private Rol rol;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String email;
    private String telefono;

    // Nuevo campo para las respuestas de seguridad
    private List<RespuestaDeSeguridad> respuestasSeguridad;

    public Usuario() {
    }

    public Usuario(String usuario, String contrasenia, Rol rol, String nombre, String apellido, Date fechaNacimiento, String email, String telefono) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
        this.telefono = telefono;
    }

    //getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<RespuestaDeSeguridad> getRespuestasSeguridad() {
        return respuestasSeguridad;
    }

    public void setRespuestasSeguridad(List<RespuestaDeSeguridad> respuestasSeguridad) {
        this.respuestasSeguridad = respuestasSeguridad;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreDeUsuario='" + usuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol=" + rol +
                '}';
    }
}
