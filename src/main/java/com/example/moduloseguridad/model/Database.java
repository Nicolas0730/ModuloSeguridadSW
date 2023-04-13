package com.example.moduloseguridad.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuración de la base de datos MySQL
 */
public class Database {

    public Database(){

    }

    //Si fuera a pedir un certificado SSL seria:
//    String url = "jdbc:mysql://localhost:3306/mydatabase?useSSL=true&requireSSL=true&verifyServerCertificate=true&trustCertificateKeyStoreUrl=file:///path/to/truststore&trustCertificateKeyStorePassword=your_password";
//    Connection con = DriverManager.getConnection(url, username, password);

    private static final String URL = "jdbc:mysql://localhost:3306/camu?serverTimezone=America/Bogota&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "nicolas";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}