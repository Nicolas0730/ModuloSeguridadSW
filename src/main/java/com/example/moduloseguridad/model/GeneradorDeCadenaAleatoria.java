package com.example.moduloseguridad.model;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class GeneradorDeCadenaAleatoria {

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new SecureRandom();

    /**
     * Genera una cadena de caracteres aleatoria de la longitud especificada.
     * @param longitud Longitud de la cadena a generar.
     * @return Cadena aleatoria de longitud especificada.
     */
    public String generarCadenaAleatoria(int longitud) {
        Objects.requireNonNull(CHARSET, "El conjunto de caracteres no puede ser nulo");
        if (longitud < 1) {
            throw new IllegalArgumentException("La longitud debe ser mayor o igual a 1");
        }
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indice = RANDOM.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(indice));
        }
        return sb.toString();
    }


}
