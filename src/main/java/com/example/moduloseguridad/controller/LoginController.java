package com.example.moduloseguridad.controller;

import com.example.moduloseguridad.Aplicacion;
import com.example.moduloseguridad.model.Alerts;
import com.example.moduloseguridad.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginController {
    private Aplicacion aplicacion=new Aplicacion();
    private UserDAO userDAO=new UserDAO();
    Alerts alerta = new Alerts();

    @FXML
    private Hyperlink contraseñaPerdida;
    @FXML
    private TextField usuariotxt;
    @FXML
    private PasswordField contraseñatxt;
    @FXML
    private Button iniciarSesionbtn;
    @FXML
    private Hyperlink registro;

    public void initialize(){

    }


    @FXML
    void iniciarSesion(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException {

        if (contraseñatxt.getText().isEmpty()||usuariotxt.getText().isEmpty()){

            alerta.alertWarning("ERROR","HAY CAMPOS VACÍOS.");


        }else{
            String con=userDAO.validarLogin(usuariotxt.getText()); //Clave traida de la base de datos
            String conn = hashPassword(contraseñatxt.getText());

            System.out.println("1 "+con);
            System.out.println("2 "+conn);
            if (conn!=null&&con.equals(conn)) {
                alerta.alertInformation("ACCESO CORRECTO", "");
            }else {
                alerta.alertWarning("ERROR ", "Los datos proporcionados son incorrectos");
                usuariotxt.clear();
                contraseñatxt.clear();
            }
        }

    }

    @FXML
    void recuperarContraseña(ActionEvent event) {
        aplicacion.cerrar();
        aplicacion.mostrarVentanaCorreoRecuperacion();

    }

    @FXML
    void registro(ActionEvent event) {

        aplicacion.mostrarVentanaRegistro();

    }


    public void setAplication(Aplicacion aplicacion) {

        this.aplicacion = aplicacion;

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
