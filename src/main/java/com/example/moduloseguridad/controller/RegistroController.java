package com.example.moduloseguridad.controller;

import com.example.moduloseguridad.Aplicacion;
import com.example.moduloseguridad.model.Alerts;
import com.example.moduloseguridad.model.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

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
    private PasswordField contraseñatxt;

    @FXML
    private Button registrarbtn;

    @FXML
    private Button volverbtn;

    public void initialize(){

        tipoDocumento.add("DNI");
        tipoDocumento.add("Pasaporte");

        tipoDocumentoCB.setItems(tipoDocumento);

    }

    @FXML
    void registro(ActionEvent event) throws SQLException {

//        Stage stage = (Stage) registrarbtn.getScene().getWindow();
//        stage.close();

        if (nombretxt.getText().isEmpty()||numeroCCtxt.getText().isEmpty()||correotxt.getText().isEmpty()||contraseñatxt.getText().isEmpty()){
            alerta.alertWarning("ERROR","CAMPOS VACÍOS");
        }else {
            userDAO.RegisterUser(nombretxt.getText(), tipoDocumentoCB.getValue(), numeroCCtxt.getText(), correotxt.getText(), contraseñatxt.getText());
            limpiarCampos();
            //FALTA QUE SE CIERRE LA PESTAÑA REGISTRO ANTES DE LLAMAR LA NUEA VISTA****************
            aplicacion.mostrarVentanaLogin();
        }
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
    void enviar(ActionEvent event) {

    }

    public void setAplication(Aplicacion aplicacion) {
        // TODO Auto-generated method stub
        this.aplicacion =aplicacion;
    }

}
