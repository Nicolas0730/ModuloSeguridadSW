package com.example.moduloseguridad;

import com.example.moduloseguridad.controller.RecuperarContraseñaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplicacion extends Application {

    private Stage primaryStage = new Stage();

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Constructora CAMU");
        mostrarVentanaLogin();
    }
    public void mostrarVentanaLogin() {
        try {
            primaryStage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("views/login.fxml"));
//            loader.setController(new LoginController());
            AnchorPane vistaIndex = (AnchorPane) loader.load();
            Scene scene = new Scene(vistaIndex);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarVentanaRecuperarContraseña(String correo) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("views/recuperarContraseña.fxml"));

            AnchorPane vistaIndex = (AnchorPane) loader.load();
            RecuperarContraseñaController recuperarContraseñaController = loader.getController();
            recuperarContraseñaController.setAplicacion(this,correo);

            Scene scene = new Scene(vistaIndex);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarPrincipal() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("views/principal.fxml"));

            AnchorPane vistaIndex = (AnchorPane) loader.load();
            Scene scene = new Scene(vistaIndex);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarVentanaRegistro() {
        try {
            primaryStage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("views/registro.fxml"));
//            loader.setController(new RecuperarContraseñaController());
            AnchorPane vistaIndex = (AnchorPane) loader.load();
            Scene scene = new Scene(vistaIndex);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void mostrarVentanaCorreoRecuperacion() {
        try {
            primaryStage.close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("views/correoRecuperacion.fxml"));
//            loader.setController(new RecuperarContraseñaController());
            AnchorPane vistaIndex = (AnchorPane) loader.load();
            Scene scene = new Scene(vistaIndex);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Necesito que este método cierre la pestaña desde donde lo llamen
     */
    public void cerrar(){
        primaryStage.close();
    }
    public static void main(String[] args) {
        launch();
    }

}
