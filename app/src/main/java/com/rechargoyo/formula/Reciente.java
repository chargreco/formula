package com.rechargoyo.formula;

/**
 * Modelo de datos que representa una entrada en el historial de juegos recientemente jugados.
 * Almacena metadatos del juego, creador, fecha de partida y recuento de likes.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Reciente {
    /** Nombre del juego recientemente jugado. */
    private String nombreJuego;

    /** Tipo de juego (0 a 3). */
    private int tipoJuego;

    /** Nombre de usuario del creador del juego. */
    private String creadorJuego;

    /** Cadena de fecha y hora de la partida recibida del servidor. */
    private String fechaReciente;

    /** Indica si el usuario le dio "me gusta" (1) o no (0). */
    private int dioLike;

    /** Número total de likes recibidos. */
    private int numeroLikes;

    /** Identificador de la entrada en la tabla de recientes. */
    private int idReciente;

    /** Identificador único del juego. */
    private int idJuego;

    /**
     * Construye un objeto Reciente.
     *
     * @param idReciente    ID del registro de recientes.
     * @param nombreJuego   Nombre del juego.
     * @param tipoJuego     Tipo de juego (0 = Cuestionario, 1 = Relacionar, 2 = Tres en raya, 3 = Memorama).
     * @param creadorJuego  Nombre de usuario del creador.
     * @param fechaReciente Fecha de la partida.
     * @param dioLike       1 si el usuario dio like, 0 en caso contrario.
     * @param numeroLikes   Contador de likes.
     * @param idJuego       ID único del juego.
     */
    public Reciente(int idReciente, String nombreJuego, int tipoJuego, String creadorJuego, String fechaReciente, int dioLike, int numeroLikes, int idJuego) {
        this.nombreJuego = nombreJuego;
        this.tipoJuego = tipoJuego;
        this.creadorJuego = creadorJuego;
        this.fechaReciente = fechaReciente;
        this.dioLike = dioLike;
        this.idReciente = idReciente;
        this.numeroLikes = numeroLikes;
        this.idJuego = idJuego;
    }

    /**
     * Establece el estado de "me gusta" del usuario.
     *
     * @param newDioLike 1 si el usuario dio like, 0 si no.
     */
    public void setDioLike(int newDioLike) {
        dioLike = newDioLike;
    }

    /**
     * Obtiene el nombre del juego.
     *
     * @return Título del juego.
     */
    public String getNombreJuego() {
        return nombreJuego;
    }

    /**
     * Obtiene el tipo numérico de juego.
     *
     * @return Entero del tipo de minijuego.
     */
    public int getTipoJuego() {
        return tipoJuego;
    }

    /**
     * Obtiene el creador del juego.
     *
     * @return Nombre de usuario del creador.
     */
    public String getCreadorJuego() {
        return creadorJuego;
    }

    /**
     * Obtiene la fecha de la partida reciente.
     *
     * @return Cadena con la fecha.
     */
    public String getFechaReciente() {
        return fechaReciente;
    }

    /**
     * Comprueba si el usuario le dio "me gusta".
     *
     * @return 1 si dio me gusta, 0 en caso contrario.
     */
    public int getDioLike() {
        return dioLike;
    }

    /**
     * Obtiene el total de likes.
     *
     * @return Número de likes.
     */
    public int getNumeroLikes() {
        return numeroLikes;
    }

    /**
     * Obtiene el ID del registro de reciente.
     *
     * @return ID del reciente.
     */
    public int getIdReciente() {
        return idReciente;
    }

    /**
     * Obtiene el ID del juego.
     *
     * @return ID del juego.
     */
    public int getIdJuego() {
        return idJuego;
    }
}
