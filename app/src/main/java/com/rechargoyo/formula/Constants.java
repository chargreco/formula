package com.rechargoyo.formula;

/**
 * Clase utilitaria que almacena las constantes globales de configuración de la aplicación,
 * incluyendo la dirección URL base para las peticiones al servidor backend PHP/MySQL.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Constants {

    /**
     * URL Base de la API del servidor remoto o local.
     *
     * Configuración habitual:
     * - Emulador de Android Studio: "http://10.0.2.2/formula/"
     * - Dispositivo físico en red Wi-Fi: "http://192.168.0.5/formula/"
     * - Servidor de producción remoto: "https://prototipoformula.fun/"
     */
    public static final String BASE_URL = "http://192.168.0.5/formula/";

    /**
     * Constructor privado para evitar la instanciación de la clase utilitaria.
     */
    private Constants() {
    }
}
