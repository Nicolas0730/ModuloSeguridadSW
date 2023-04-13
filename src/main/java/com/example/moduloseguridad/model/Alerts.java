package com.example.moduloseguridad.model;

import javafx.scene.control.Alert;

public class Alerts {

    public Alerts(){

    }

    public void alertInformation(String title, String context){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);

        alert.showAndWait();
    }

    public void alertWarning(String title, String context){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);

        alert.showAndWait();
    }
}
