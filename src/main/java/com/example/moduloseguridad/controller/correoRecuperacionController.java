package com.example.moduloseguridad.controller;

import com.example.moduloseguridad.Aplicacion;
import com.example.moduloseguridad.model.Alerts;
import com.example.moduloseguridad.model.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class correoRecuperacionController {

    private Aplicacion aplicacion=new Aplicacion();
    private Validar validar=new Validar();
    private Alerts alertas = new Alerts();

    @FXML
    private TextField correoRecuperaciontxt;

    @FXML
    private Button enviarbtn;

    @FXML
    private Hyperlink registro;

    @FXML
    private Button volverbtn;

    public void initialize(){

    }

    @FXML
    void enviar(ActionEvent event) throws SQLException, IOException {

        //Validar que el correo esté registrado en la base de datos
        if(validarCorreo()) {
            //-----------------------ENVIO CORREO ---------------------------------
            //Código aleatorio que se enviará al usuario y se almacenará en la BD
            String codigo=validar.getCodigo().generarCadenaAleatoria(8);//El codigo es de 8 caracteres
//           ********ACTIVAR****************
            validar.enviarCorreoRecuperacion(correoRecuperaciontxt.getText(), codigo);
//            *********************

            //envío el correo a recuperarContraseñaController para que este sea buscado y posteriormente validado
            //RecuperarContraseñaController recuperarContraseñaController= new RecuperarContraseñaController(correoRecuperaciontxt.getText());
            aplicacion.mostrarVentanaRecuperarContraseña(correoRecuperaciontxt.getText());

        }

        Stage stage = (Stage) enviarbtn.getScene().getWindow();
        stage.close();
    }

    /**
     * Método que valida un correo esté registrado en la base de datos de CAMU
     * @return true si el correo está en la BD, false de lo contrario
     * @throws SQLException
     */
    private boolean validarCorreo() throws SQLException {

        if (correoRecuperaciontxt.getText().isEmpty()) {
            alertas.alertWarning("ERROR", "CAMPOS VACÍOS");
            return false;
        } else {
            if (validar.alertExist(validar.emailExist(correoRecuperaciontxt.getText()), correoRecuperaciontxt.getText())) {
                return true;
            } else return false;
        }
    }

    @FXML
    void registro(ActionEvent event) {

        aplicacion.mostrarVentanaRegistro();

        Stage stage = (Stage) registro.getScene().getWindow();
        stage.close();
    }

    @FXML
    void volver(ActionEvent event) {

        aplicacion.mostrarVentanaLogin();

        Stage stage = (Stage) volverbtn.getScene().getWindow();
        stage.close();
    }

    public void setAplication(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

}
