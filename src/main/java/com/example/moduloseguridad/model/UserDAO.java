package com.example.moduloseguridad.model;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    User user = new User();
    Alerts alerta = new Alerts();

    public UserDAO(){

    }

    //INSERT INTO users (nombre, tipoDocumento, documento, correo, contrasenia) VALUES ('Juan Perez', 'DNI', '12345678', 'juanperez@mail.com', 'contraseña123');

//    CREATE TABLE users (
//    id INT AUTO_INCREMENT PRIMARY KEY,
//    nombre VARCHAR(50) NOT NULL,
//    tipoDocumento ENUM('DNI', 'Pasaporte') NOT NULL,
//    documento VARCHAR(10) NOT NULL,
//    correo VARCHAR(50) NOT NULL UNIQUE,
//    contrasenia VARCHAR(255) NOT NULL,
//    recuperacion_contrasenia VARCHAR(255) DEFAULT NULL);


    /**
     * Busca en la base de datos si un usuario existe por su email
     * @param email
     * @return true si existe
     * @throws SQLException
     */
    public boolean userExists(String email) throws SQLException {
        boolean exists = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE correo = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return exists;
    }


    /**
     * Método que devuelve un usuario de la BD dado su correo (que es unico)
     * @param email del usuario a buscar
     * @return usuario de ser encontrado
     * @throws SQLException
     */
    public User recuperarUsuario(String email) throws SQLException {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE correo = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("nombre"),rs.getString("tipoDocumento"),
                        rs.getString("documento"), rs.getString("correo"), rs.getString("contrasenia"));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return user;
    }

    /**
     * Método que actualiza la contraseña de un usuario en la base de datos
     * @param pass contraseña nueva encriptada en
     * @param email del usuario que va a actualizar
     * @throws SQLException
     */
    public void setUserPass(String pass, String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE users SET contrasenia = ? WHERE correo = ?");
            String hashedPass = hashPassword(pass); //Encripta antes de escribir en la BD
            stmt.setString(1, hashedPass);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Método que registra un usuario en la base de datos. Este método es invocado en la clase RegistroController
     * y recibe la información necesaria para registrar un usuario en la BD. En caso de que el usuario se registre
     * con exito en la BD, se lanzara una alerta confirmando. Si el correo ya está asociado a una cuenta, se lanzará
     * una alerta indicando que no se pudo registrar el usuario
     * @param nombre del usuario
     * @param tipoDoc de identificación (DNI o Pasaporte)
     * @param documento de identificación
     * @param correo asociado a la cuenta
     * @param contrasenia para ingresar a los servicios de CAMU
     * @throws SQLException
     */
    public void RegisterUser(String nombre, String tipoDoc, String documento, String correo,String contrasenia) throws SQLException {

        if (!userExists(correo)) {

                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    conn = Database.getConnection();
                    stmt = conn.prepareStatement("INSERT INTO users (nombre, tipoDocumento, documento, correo, contrasenia) VALUES (?,?,?,?,?)");

                    stmt.setString(1, nombre);
                    stmt.setString(2, tipoDoc);
                    stmt.setString(3, documento);
                    stmt.setString(4, correo);


                    String hashedPass = hashPassword(contrasenia); //Encriptacion de la contraseña
                    stmt.setString(5, hashedPass);

                    stmt.executeUpdate();

                    alerta.alertInformation("USUARIO REGISTRADO CON EXITO","El usuario "+nombre+" ha sido registrado en las bases de datos de CAMU, ahora podrá ingresar.");

                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                }
        }else{
            alerta.alertWarning("ERROR: CORREO EN USO ","\nEl correo que desea registrar ya está asociado a una cuenta.");
        }
    }


    /**
     * Método que guarda el codigo de recuperación de contraseña para el usuario en la base de datos
     * @param correoElectronico del usuario al que se le va a almacenar el codigo de recuperación
     * @param codigo que será almacenado en la base de datos y será validado cuando el usuario desee registrar
     * su nueva contraseña, ambos codigos deberan coincidir
     */
    public void registrarCodigoRecuperacion(String correoElectronico, String codigo) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE users SET recuperacion_contrasenia = ? WHERE correo = ?");

            stmt.setString(1, codigo);
            stmt.setString(2, correoElectronico);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Método que devuelve el codigo de recuperacion de un usuario dado su correo (UNIQUE)
     * @param email del usuario al que se le va a devolver su codigo de recuperación
     * @return codigo de recuperación
     * @throws SQLException
     */
    public String recuperarCodigoRecuperacion(String email) throws SQLException {
        String codigoRecu = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE correo = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next()) {
                codigoRecu=rs.getString("recuperacion_contrasenia");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return codigoRecu;
    }


    /**
     * Método que restaura la contraseña de recuperación del usuario
     * @param email del usuario al que se le va a restablecer la contraseña de recuperacion
     * @throws SQLException
     */
    public void setPassRestore( String email) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = Database.getConnection();
            stmt = conn.prepareStatement("UPDATE users SET recuperacion_contrasenia = ? WHERE correo = ?");
            stmt.setString(1, null);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }


    /**
     * Método que devuelve el codigo
     * @param correo
     * @return
     * @throws SQLException
     */
    public String validarLogin(String correo) throws SQLException {

        String pass = "null";

        if (userExists(correo)){ //El usuario registrado con el correo existe en la BD

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                conn = Database.getConnection();
                stmt = conn.prepareStatement("SELECT * FROM users WHERE correo = ?");
                stmt.setString(1, correo);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    pass=rs.getString("contrasenia");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        }
        return pass;
    }

    /**
     * Método que encripta una contraseña en Hash utilizando el algortimo SHA-256
     * @param password Contraseña que será encriptada
     * @return Contraseña encriptada
     * @throws NoSuchAlgorithmException
     */
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
