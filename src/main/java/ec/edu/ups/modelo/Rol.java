package ec.edu.ups.modelo;
/**
 * Enums que representa los distintos roles de usuario permitidos en el sistema.
 *
 * <p>Se utiliza para controlar la autorización de funcionalidades según el perfil
 * ADMINISTRADOR puede gestionar usuarios y configuraciones,
 * USUARIO tiene acceso limitado a sus propios datos y a crear
 * carritos de compras).</p>
 */
public enum Rol {
    ADMINISTRADOR,
    USUARIO
}
