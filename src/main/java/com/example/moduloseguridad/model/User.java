package com.example.moduloseguridad.model;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    private String nombre;
    private String tipoDocumento; //DNI, PASAPORTE
    private String documento;
    private String correo;
    private String contrasenia;


    public User(){
    }

    public User(String nombre, String tipoDocumento,String documento, String corrreo, String pass) throws NoSuchAlgorithmException {
        this.nombre=nombre;
        this.correo=corrreo;
//        this.contrasenia = BCrypt.hashpw(pass,BCrypt.gensalt());
        this.contrasenia = hashPassword(pass); //ENCRIPTACIÓN DE LA CONTRASEÑA
        this.tipoDocumento=tipoDocumento;
        this.documento=documento;
    }
    public String getCorreo() {return correo;}
    public String getContrasenia() {return contrasenia;}
    public String getNombre() { return nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasenia(String contrasenia) {this.contrasenia = contrasenia;}
    public String getDocumento() {return documento;}
    public String getTipoDocumento() {return tipoDocumento;}


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
