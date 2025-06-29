package ec.edu.ups.modelo;

public class Usuario {
    private String usuario;
    private String contrasenia;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String nombreDeUsuario, String contrasenia, Rol rol) {
        this.usuario = nombreDeUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

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

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreDeUsuario='" + usuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", rol=" + rol +
                '}';
    }
}
