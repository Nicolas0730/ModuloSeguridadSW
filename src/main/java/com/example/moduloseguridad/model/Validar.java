package com.example.moduloseguridad.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Validar {

    private Alerts alertas = new Alerts();
    private GeneradorDeCadenaAleatoria codigo=new GeneradorDeCadenaAleatoria();
    UserDAO userDAO=new UserDAO();

    public Validar(){

    }

    /**
     * Método que valida si un usuario existe en la base de datos dado su correo
     * @param correo del usuario a verificar
     * @return true si existe, false de lo contrario
     * @throws SQLException
     */
    public boolean emailExist(String correo) throws SQLException {
        UserDAO userDAO = new UserDAO();
        boolean r=false;

        if(userDAO.userExists(correo)) {
            r = true;
            return r;

        } else {

        return r;}
    }

    /**
     * Método que dispara una alerta si un correo existe o no en la BD
     * Lanza una alerta si el usuario fue registrado en la BD o una alerta de advertencia si no fue encontrado
     * @param exist true si existe en la BD, false de lo contrario
     * @param correo del usuario que se está validando
     * @return true si el usuario existe y fue notificado
     */
    public boolean alertExist(boolean exist, String correo){
          boolean aux=false;
           if (exist){
               aux=true;
               alertas.alertInformation("VALIDACIÓN CON EXITO.","El usuario registrado con el correo "+correo+ " existe en nuestras" + " bases de datos, el código para recuperar su contraseña ha sido enviado a su correo.");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }else alertas.alertWarning("USUARIO NO ENCONTRADO.","El usuario registrado con el correo "+correo+ " no existe en nuestras" +
                " bases de datos.");
        return aux;
    }



    public void enviarCorreoRecuperacion(String correoElectronicoTo, String codigo) throws SQLException {

        final String correoFrom = "recuperacioncontraseniasw@gmail.com";
        final String passFrom = "uswcncvktkhffxsl";//contraseña de aplicación generada para el correo


        String subject = "Código de recuperación de contraseña"; //Asunto del correo

        Session session=configurarSMTP(correoFrom,passFrom);
        crearMensaje(session,correoFrom,passFrom,codigo,subject,correoElectronicoTo);


        //--------------Guardar ese codigo para compararlo cuando el usuario lo ingrese ----------------------------
        userDAO.registrarCodigoRecuperacion(correoElectronicoTo,codigo);

    }

    /**
     * Método encargado de la configuración necesaria para el envio de un mensaje SMTP
     * @param correoFrom correo desde donde se enviarará el correo
     * @param passFrom contraseña de aplicación del correo desde donde se enviará el correo
     * @return Session configurada con los datos recibidos
     */
    public Session configurarSMTP(String correoFrom, String passFrom){

        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.ssl.protocols","TLSv1.2");
        props.setProperty("mail.smtp.user", correoFrom);

        // Creación de la sesión SMTP
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoFrom, passFrom); //
            }
        });

        return session;
    }

    /**
     * Método que se encarga de la creación de un mensaje SMTP
     * @param session con la configuración necesaria para el envío de un mensaje
     * @param correoFrom corrreo desde donde se enviará el correo
     * @param passFrom clave de aplicacion del correo desde donde se enviará el correo
     * @param codigo generado aleatoriamente para la recuperación del correo
     * @param subject Asunto del correo
     * @param correoElectronicoTo Correo a donde será enviado el mensaje
     */
    private void crearMensaje(Session session, String correoFrom, String passFrom, String codigo, String subject, String correoElectronicoTo) {
        try {
            // Creación del mensaje
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoFrom));

            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(correoElectronicoTo)});
            message.setSubject(subject); //Encabezado
            message.setText("Buen día.\nDesde Constructora CAMU atendimos su solicitud para restablecer su contraseña. \n\nEl siguiente codigo le permitirá la recuperación de contraseña: " + codigo); //Contenido del correo

            enviarMensajeSMTP(session,correoFrom,passFrom,message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que envia un mensaje SMTP
     * @param session configurada con la información del correo que enviará el mensaje y el protocolo
     * @param correoFrom Desde donde será enviado el correo
     * @param passFrom Contraseña de aplicación del correo desde donde será enviado el correo
     * @param message Que será enviado al correo destino
     * @throws MessagingException
     */
    private void enviarMensajeSMTP(Session session, String correoFrom, String passFrom, Message message) throws MessagingException {
        Transport mTransport = session.getTransport("smtp");
        mTransport.connect(correoFrom,passFrom);
        Transport.send(message,message.getRecipients(Message.RecipientType.TO));
        mTransport.close();
    }

    public GeneradorDeCadenaAleatoria getCodigo() {
        return codigo;
    }


}
