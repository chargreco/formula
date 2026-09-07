package com.rechargoyo.formula;

/**
 * Modelo de datos que representa un juego o formulario creado por un usuario.
 * Contiene información sobre el título del juego, su tipo, cantidad de "me gusta" e identificador único.
 *
 * @author Eduardo
 * @version 1.0
 */
public class Creaciones {
    /** Nombre o título del juego creado. */
    private String nombreJuego;

    /** Tipo de juego (0 = Cuestionario Simple, 1 = Relacionar Columnas, 2 = Tres en Raya, 3 = Memorama). */
    private int tipoJuego;

    /** Indica si el usuario actual ha dado "me gusta" al juego (1 = Sí, 0 = No). */
    private int dioLike;

    /** Número total de "me gusta" acumulados por el juego. */
    private int numeroLikes;

    /** Identificador único del juego en el servidor. */
    private int idJuego;

    /** Nombre de usuario del creador del juego. */
    private String user;

    /**
     * Construye un objeto de la entidad Creaciones.
     *
     * @param nombreJuego Nombre del juego creado.
     * @param tipoJuego   Tipo de minijuego (0 a 3).
     * @param dioLike     Indica si el usuario le dio like (1) o no (0).
     * @param numeroLikes Total de likes acumulados.
     * @param idJuego     Identificador ID único en la base de datos.
     * @param user        Nombre de usuario del creador.
     */
    public Creaciones(String nombreJuego, int tipoJuego, int dioLike, int numeroLikes, int idJuego, String user) {
        this.nombreJuego = nombreJuego;
        this.tipoJuego = tipoJuego;
        this.dioLike = dioLike;
        this.numeroLikes = numeroLikes;
        this.idJuego = idJuego;
        this.user = user;
    }

    /**
     * Actualiza el estado de "me gusta" asignado por el usuario.
     *
     * @param newDioLike 1 si el usuario dio me gusta, 0 en caso contrario.
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
     * Obtiene el tipo numérico de minijuego.
     *
     * @return Código numérico del tipo de juego (0, 1, 2 o 3).
     */
    public int getTipoJuego() {
        return tipoJuego;
    }

    /**
     * Obtiene el estado de "me gusta" del usuario actual.
     *
     * @return 1 si el usuario dio like, 0 si no.
     */
    public int getDioLike() {
        return dioLike;
    }

    /**
     * Obtiene el total de likes acumulados.
     *
     * @return Número total de likes.
     */
    public int getNumeroLikes() {
        return numeroLikes;
    }

    /**
     * Obtiene el ID del juego.
     *
     * @return Identificador único del juego.
     */
    public int getIdJuego() {
        return idJuego;
    }

    /**
     * Obtiene el nombre de usuario del creador.
     *
     * @return Nombre del usuario creador.
     */
    public String getUser() {
        return user;
    }
}
