package com.example.moduloseguridad.controller;

import com.example.moduloseguridad.Aplicacion;
import com.example.moduloseguridad.model.Alerts;
import com.example.moduloseguridad.model.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class RecuperarContraseñaController {

    private Aplicacion aplicacion = new Aplicacion();
    UserDAO userDAO = new UserDAO();
    String correo;
    Alerts alerta = new Alerts();
    @FXML
    private TextField codigoRecuperaciontxt;

    @FXML
    private Button verificarbtn;

    @FXML
    private Button restablecerbtn;

    @FXML
    private TextField nuevaContraseña2txt;

    @FXML
    private Hyperlink ingresar;

    @FXML
    private TextField nuevaContraseñatxt;


    public void initialize(){
    restablecerbtn.setDisable(true);
    nuevaContraseñatxt.setDisable(true);
    nuevaContraseña2txt.setDisable(true);
    }

    @FXML
    void verificar(ActionEvent event) throws SQLException {

        if (codigoRecuperaciontxt.getText().isEmpty()){
            alerta.alertWarning("ERROR","CAMPOS VACÍOS");
        }else {
            if (codigoRecuperaciontxt.getText().equals(userDAO.recuperarCodigoRecuperacion(correo))){
                alerta.alertInformation("FELICIDADES","CODIGO CORRECTO");
                able();
                //Se restablece la contraseña de recuperacion del usuario una vez se verifique que esta si le
                // llego al usuario y es correcta
                codigoRecuperaciontxt.clear();
                userDAO.setPassRestore(correo);
            }else {
                alerta.alertWarning("ERROR","EL CÓDIGO INGRESADO NO ES IGUAL");
                codigoRecuperaciontxt.clear();
            }
        }

    }

    @FXML
    void restablecer(ActionEvent event) throws SQLException {
        if (nuevaContraseñatxt.getText().isEmpty()||nuevaContraseña2txt.getText().isEmpty()){
            alerta.alertWarning("ERROR","CAMPOS VACÍOS");
        }else{
            if(validarContraseñas()){
                //Guarda en la BD la nueva contraseña
                userDAO.setUserPass(nuevaContraseñatxt.getText(),correo);
                aplicacion.mostrarVentanaLogin();
            }
        }
    }

    /**
     * Método que verifica que las dos contraseñas ingresadas sean identicas
     * @return
     */
    private boolean validarContraseñas() {
        if (nuevaContraseña2txt.getText().equals(nuevaContraseñatxt.getText()))
            return true;
        else return false;
    }

    @FXML
    void Ingresar(ActionEvent event) {
        aplicacion.mostrarVentanaLogin();

    }


    public void setAplicacion(Aplicacion aplicacion, String correo) {
        this.aplicacion=aplicacion;
        this.correo=correo;
    }

    /**
     * Método que habilita los compos de texto para el ingreso de la contraseña y el boton
     */
    public void able(){
        restablecerbtn.setDisable(false);
        nuevaContraseñatxt.setDisable(false);
        nuevaContraseña2txt.setDisable(false);
    }
}
