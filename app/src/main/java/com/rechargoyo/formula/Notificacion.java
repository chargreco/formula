package com.rechargoyo.formula;

public class Notificacion {
    private String notificacion;
    private boolean visto;

    public Notificacion(String notificacion, int visto) {
        this.notificacion = notificacion;
        this.visto = (visto == 1? true : false);
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public boolean isVisto() {
        return visto;
    }
}
