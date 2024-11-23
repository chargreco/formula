package com.rechargoyo.formula;

import android.widget.EditText;

public class Pregunta1a1 {

    public Pregunta1a1 (){

    }

    public Pregunta1a1 (String qust, String asw){
        this.qust = qust;
        this.asw = asw;
    }


    private int numPregunta;
    private String qust;
    private String asw;

    public void setQust(String qust){
        this.qust=qust;
    }

    public String getQust(){
        return this.qust;
    }

    public void setAsw(String asw){
        this.asw=asw;
    }

    public String getAsw(){
        return this.asw;
    }

    public int getNumPregunta() {
        return numPregunta;
    }

    public void setNumPregunta(int numPregunta) {
        this.numPregunta = numPregunta;
    }


}
