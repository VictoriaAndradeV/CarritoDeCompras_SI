package ec.edu.ups.modelo;

import ec.edu.ups.util.*;
import java.util.Date;
import java.util.List;

/**
 * Representa a un usuario registrado en el sistema
 *
 * <p>
 * Cada usuario tiene un conjunto de datos personales: cédula, nombre, correo, teléfono,
 * fecha de nacimiento y credenciales de acceso (nombre de usuario y contraseña).
 * Además, incluye un rol que determina sus permisos en el sistema
 * ADMINISTRADOR o USUARIO
 * </p>
 *
 * Contiene una lista de respuestas de seguridad, las cuales son las
 * respuestas que ha dado el usuario a las tres preguntas de seguridad
 * solicitadas, para recuperar su contraseña
 */
public class Usuario {

    /**
     * Atributos del objeto Usuario
     */
    private String cedula; //nuevo usuario es cedula
    private String contrasenia;
    private Rol rol;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String email;
    private String telefono;

    // Nuevo campo para las respuestas de seguridad
    private List<RespuestaDeSeguridad> respuestasSeguridad;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Constructor por defecto. Inicializa el manejador de internacionalización
     * con el idioma español y región EC.
     */
    public Usuario() {
        this.mih = new MensajeInternacionalizacionHandler("es", "EC");
    }

    public Usuario(String cedula, String contrasenia, Rol rol, String nombre, String apellido,
                   Date fechaNacimiento, String email, String telefono)throws ExcepcionContrasenia,
                    ExcepcionCedula, ExcepcionNomApe, ExcepcionCorreo, ExcepcionTelefono {
        this.mih = new MensajeInternacionalizacionHandler("es", "EC");
        setCedula(cedula);
        setContrasenia(contrasenia);
        setNombre(nombre);
        setApellido(apellido);
        setEmail(email);
        setTelefono(telefono);
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCedula() {
        return cedula;
    }
    /**
     * Valida y asigna la cédula del usuario.
     *
     * @param cedula Cédula que se trabaja como username del usuario
     * @throws ExcepcionCedula Si es nula, vacía, no tiene 10 dígitos
     */
    public void setCedula(String cedula) throws ExcepcionCedula {
        // Verifica que el campo no esté vacío y que tenga exactamente 10 dígitos numéricos
        if(cedula == null ||cedula.isEmpty())
            throw new ExcepcionCedula(mih.get("excepcion.cedula.vacia"));
        if (!cedula.matches("\\d{10}")) {
            throw new ExcepcionCedula(mih.get("excepcion.longi.max"));
        }
        int suma = 0;
        int numero;

        // Se recorre hasta el penúltimo dígito (posición 0 a 8)
        for (int i = 0; i < 9; i++) {
            numero = Integer.parseInt(String.valueOf(cedula.charAt(i)));

            if (i % 2 == 0) { // posiciones impares (índice par)
                numero *= 2;
                if (numero > 9) {
                    numero = numero - 9;
                }
            }
            suma += numero;
        }
        // Calcula el dígito verificador esperado
        int resultado;
        if (suma % 10 == 0) {
            resultado = 0;
        } else {
            resultado = 10 - (suma % 10);
        }
        //Se compara con el décimo dígito de la cédula
        int digitoVerificador = Integer.parseInt(String.valueOf(cedula.charAt(9)));
        if (resultado != digitoVerificador) {
            throw new ExcepcionCedula(mih.get("excepcion.cedula.no.valida"));
        }

        this.cedula = cedula;
    }

    public String getContrasenia() {
        return contrasenia;
    }
    /**
    * Valida y asigna la contraseña.
    *
    * @param contrasenia Contraseña a validar.
    * @throws ExcepcionContrasenia Si no es nula, vacía, menor de 6 caracteres,
    */
    public void setContrasenia(String contrasenia) throws ExcepcionContrasenia {
        if (contrasenia == null || contrasenia.isEmpty()) {
            throw new ExcepcionContrasenia(mih.get("excepcion.contra.vacia"));
        }
        String contra = contrasenia.trim();
        if (contra.length() < 6) { //valida longitud minima
            throw new ExcepcionContrasenia(mih.get("excepcion.longi"));
        }
        if (!contra.matches(".*[A-Z].*")) {
            throw new ExcepcionContrasenia(mih.get("excepcion.mayus"));
        }
        if (!contra.matches(".*[a-z].*")) {
            throw new ExcepcionContrasenia(mih.get("excepcion.min"));
        }
        if (!contra.matches(".*[@_\\-*].*")) {
            throw new ExcepcionContrasenia(mih.get("excepcion.caracteres"));
        }
        this.contrasenia = contra;
    }
    /**
     * Valida cadenas de texto para nombre y apellido.
     *
     * @param campo Texto a validar.
     * @return Texto validado sin espacios sobrantes.
     * @throws ExcepcionNomApe Si es nulo, vacío o longitud fuera de rango o contiene caracteres inválidos.
     */
    private String validarTexto(String campo) throws ExcepcionNomApe {
        if (campo == null || campo.isEmpty()) {
            throw new ExcepcionNomApe(mih.get("excepcion.nom.ape.vacio"));
        }
        String t = campo.trim();
        if (t.length() < 3 || t.length() > 25) {
            throw new ExcepcionNomApe(mih.get("excepcion.longitud"));
        }
        if (!t.matches("^[A-Za-z]+$")) {
            throw new ExcepcionNomApe(mih.get("excepcion.carac"));
        }
        return t;
    }

    public void setNombre(String nombre) throws ExcepcionNomApe {
        this.nombre = validarTexto(nombre);
    }
    public void setApellido(String apellido) throws ExcepcionNomApe {
        this.apellido = validarTexto(apellido);
    }
    /**
     * Valida y asigna el correo electrónico.
     *
     * @param email Correo a validar.
     * @throws ExcepcionCorreo Si es nulo, vacío, con formato inválido o demasiado largo.
     */
    public void setEmail(String email) throws ExcepcionCorreo {
        if (email == null) {
            throw new ExcepcionCorreo(mih.get("excepcion.correo.vacio"));
        }
        String correo = email.trim();
        //^ $ con esto nos aseguramos que el texto del correo se valide con las condiciones
        //[A-Za-z0-9._%+-] valida la parte de usuario del correo
        //@[A-Za-z0-9.] valida la primera parte del dominio del correo @ -> gmail.
        //[A-Za-z]{2,} es para la parte .com que requiere de un minimo de 2 caracteres
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.]+\\.[A-Za-z]{2,}$";
        if (!correo.matches(emailRegex)) {
            throw new ExcepcionCorreo(mih.get("excepcion.correo.formato"));
        }
        //longitud maxima del correo
        if (correo.length() > 100) {
            throw new ExcepcionCorreo(mih.get("excepcion.correo.longi"));
        }
        this.email = correo;
    }
    /**
     * Valida y asigna el teléfono (10 dígitos).
     *
     * @param telefono Teléfono a validar.
     * @throws ExcepcionTelefono Si es nulo o no cumple el patrón de 10 dígitos.
     */
    public void setTelefono(String telefono) throws ExcepcionTelefono {
        if (telefono == null) {
            throw new ExcepcionTelefono(mih.get("excepcion.telef.vacio"));
        }
        String telef = telefono.trim();
        if (!telef.matches("\\d{10}")) {
            throw new ExcepcionTelefono(mih.get("excpecion.longitud"));
        }
        this.telefono = telef;
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

    public String getApellido() {
        return apellido;
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

    public String getTelefono() {
        return telefono;
    }

    public List<RespuestaDeSeguridad> getRespuestasSeguridad() {
        return respuestasSeguridad;
    }

    public void setRespuestasSeguridad(List<RespuestaDeSeguridad> respuestasSeguridad) {
        this.respuestasSeguridad = respuestasSeguridad;
    }
}
