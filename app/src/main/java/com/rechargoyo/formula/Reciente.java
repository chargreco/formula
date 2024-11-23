package com.rechargoyo.formula;

public class Reciente {
    private String nombreJuego;
    private int tipoJuego;
    private String creadorJuego;
    private String fechaReciente;
    private int dioLike;
    private int numeroLikes;
    private int idReciente;
    private int idJuego;

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

    public void setDioLike(int newDioLike){
        //if(dioLike>newDioLike) numeroLikes--;
        //else numeroLikes++;
        dioLike = newDioLike;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public int getTipoJuego() {
        return tipoJuego;
    }

    public String getCreadorJuego() {
        return creadorJuego;
    }

    public String getFechaReciente() {
        return fechaReciente;
    }

    public int getDioLike() {
        return dioLike;
    }

    public int getNumeroLikes() {
        return numeroLikes;
    }

    public int getIdReciente(){ return idReciente; }

    public int getIdJuego() { return idJuego; }
}
