package com.example.moduloseguridad.controller;

import com.example.moduloseguridad.Aplicacion;
import com.example.moduloseguridad.model.Alerts;
import com.example.moduloseguridad.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroController {

    private Aplicacion aplicacion=new Aplicacion();
    private UserDAO userDAO = new UserDAO();
    private Alerts alerta = new Alerts();
    ObservableList<String> tipoDocumento = FXCollections.observableArrayList();


    @FXML
    private TextField correotxt;

    @FXML
    private TextField numeroCCtxt;

    @FXML
    private TextField nombretxt;

    @FXML
    private ComboBox<String> tipoDocumentoCB=new ComboBox<String>();

    @FXML
    private Label labelContrasenia=new Label("Debe contener al menos una letra mayúscula, un digito ");

    @FXML
    private PasswordField contraseñatxt;

    @FXML
    private PasswordField repetirContraseñatxt;

    @FXML
    private Button registrarbtn;

    @FXML
    private Button volverbtn;

    public void initialize(){

        tipoDocumento.add("DNI");
        tipoDocumento.add("Pasaporte");

        labelContrasenia.setText("ERROR:La contraseña debe contener al menos una letra mayúscula, un digito , un caracter especial y un tamaño de 8");
        labelContrasenia.setVisible(false);
        labelContrasenia.setStyle("-fx-text-fill: red;");

        tipoDocumentoCB.setItems(tipoDocumento);

    }

    @FXML
    void registro(ActionEvent event) throws SQLException {


        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=.*[a-zA-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contraseñatxt.getText());

        if (nombretxt.getText().isEmpty()||numeroCCtxt.getText().isEmpty()||correotxt.getText().isEmpty()||contraseñatxt.getText().isEmpty()){
            alerta.alertWarning("ERROR","CAMPOS VACÍOS");
        }else {
            if (validarContraseñas() == false) {
                alerta.alertWarning("ERROR", "La Contraseña no coincide");
            }else{
                if (!matcher.matches()) {
                    labelContrasenia.setVisible(true);
                }else {
                    userDAO.RegisterUser(nombretxt.getText(), tipoDocumentoCB.getValue(), numeroCCtxt.getText(), correotxt.getText(), contraseñatxt.getText());
                    limpiarCampos();

                    aplicacion.mostrarVentanaLogin();

                    Stage stage = (Stage) registrarbtn.getScene().getWindow();
                    stage.close();
                }
            }
        }
    }

    private boolean validarContraseñas() {
        if (repetirContraseñatxt.getText().equals(contraseñatxt.getText()))
            return true;
        else return false;
    }

    private void limpiarCampos() {
    nombretxt.clear();
    tipoDocumentoCB.setValue(null);
    numeroCCtxt.clear();
    correotxt.clear();
    contraseñatxt.clear();
    }

    @FXML
    void volver(ActionEvent event) {

        Stage stage = (Stage) volverbtn.getScene().getWindow();
        stage.close();

        aplicacion.mostrarVentanaLogin();

    }

    @FXML
    public void validarTexto(){
        if (contraseñatxt.getText().isEmpty()){
            labelContrasenia.setVisible(false);
        }
    }

    @FXML
    void enviar(ActionEvent event) {

    }

    public void setAplication(Aplicacion aplicacion) {
        // TODO Auto-generated method stub
        this.aplicacion =aplicacion;
    }

}
