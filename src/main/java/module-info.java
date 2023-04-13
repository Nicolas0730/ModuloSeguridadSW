module com.example.moduloseguridad {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires java.mail;


    opens com.example.moduloseguridad to javafx.fxml;
    exports com.example.moduloseguridad;
    exports com.example.moduloseguridad.controller;
    opens com.example.moduloseguridad.controller to javafx.fxml;
}