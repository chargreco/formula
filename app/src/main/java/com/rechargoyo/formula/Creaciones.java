package com.rechargoyo.formula;

public class Creaciones {
    private String nombreJuego;
    private int tipoJuego;
    private int dioLike;
    private int numeroLikes;
    private int idJuego;
    private String user;

    public Creaciones(String nombreJuego, int tipoJuego, int dioLike, int numeroLikes, int idJuego, String user) {
        this.nombreJuego = nombreJuego;
        this.tipoJuego = tipoJuego;
        this.dioLike = dioLike;
        this.numeroLikes = numeroLikes;
        this.idJuego = idJuego;
        this.user = user;
    }

    public void setDioLike(int newDioLike){
        dioLike = newDioLike;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public int getTipoJuego() {
        return tipoJuego;
    }

    public int getDioLike() {
        return dioLike;
    }

    public int getNumeroLikes() {
        return numeroLikes;
    }

    public int getIdJuego() { return idJuego; }

     public String getUser(){ return user; }
}
