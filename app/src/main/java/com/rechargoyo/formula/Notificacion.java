package com.rechargoyo.formula;

/**
 * Modelo de datos que representa una notificación enviada al usuario.
 * Almacena el mensaje informativo y el estado de lectura (visto / no visto).
 *
 * @author Eduardo
 * @version 1.0
 */
public class Notificacion {
    /** Contenido en texto del mensaje de notificación. */
    private String notificacion;

    /** Estado de lectura (true = leída/vista, false = no leída). */
    private boolean visto;

    /**
     * Construye una nueva instancia de Notificacion.
     *
     * @param notificacion Texto del mensaje.
     * @param visto        Entero recibido del backend (1 si fue vista, 0 si está pendiente).
     */
    public Notificacion(String notificacion, int visto) {
        this.notificacion = notificacion;
        this.visto = (visto == 1);
    }

    /**
     * Obtiene el texto de la notificación.
     *
     * @return Cadena con el mensaje.
     */
    public String getNotificacion() {
        return notificacion;
    }

    /**
     * Establece el texto del mensaje de notificación.
     *
     * @param notificacion Nuevo texto del mensaje.
     */
    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    /**
     * Indica si la notificación ya fue vista por el usuario.
     *
     * @return true si ya fue leída; false en caso contrario.
     */
    public boolean isVisto() {
        return visto;
    }
}
